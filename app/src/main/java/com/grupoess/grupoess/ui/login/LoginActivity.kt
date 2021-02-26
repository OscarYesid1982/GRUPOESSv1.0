package com.grupoess.grupoess.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions



import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.grupoess.grupoess.MainActivity
import com.grupoess.grupoess.ProviderType
import com.grupoess.grupoess.R
import com.grupoess.grupoess.model.User
import com.grupoess.grupoess.ui.home.HomeActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.login_Condiciones2
import org.json.JSONArray
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private  val GOOGLE_SIGN_IN = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (leer_user() == true){

            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }


        login_Condiciones2.setOnClickListener {
            val uri: Uri = Uri.parse("https://grupoess.com/terminos-y-condiciones/")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        login_botonCrearCuenta.setOnClickListener {
            val i = Intent(this, RegistroActivity::class.java)
            startActivity(i)
            finish()
        }
        login_BotonGoogle.setOnClickListener {
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleClient:GoogleSignInClient = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut()

            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)

        }


        //setup
        inicioSesion()
        sesion()
    }

    override fun onStart() {
        super.onStart()
        login_LinearLayout.visibility = View.VISIBLE
    }

    private fun inicioSesion() {

        login_botonIniciosesion.setOnClickListener {
            if (login_Email.text.isNotEmpty() && login_Contrasena.text.isNotEmpty()) {
                //Login Firebase
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(login_Email.text.toString(),
                        login_Contrasena.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showHome(it.result?.user?.email ?: "", ProviderType.BASIC)
                    } else {

                        showAlert()
                    }
                }

            }
        }
    }
    fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando el Usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog : AlertDialog = builder.create()
        dialog.show()
    }
    fun showHome(email:String, provider: ProviderType){
        val homeIntent:Intent = Intent(this, MainActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
            guardarUsuario(email) //Guarda en Base de datos
        }
        startActivity(homeIntent)
    }
    private fun guardar_data(data: String, id: String) {

        val sharpref = getPreferences(Context.MODE_WORLD_READABLE)
        val editor = sharpref.edit()
        editor.putString("user", data)
        editor.commit()


        Log.i("qqqq", id.toString())

        try {
            val data_arrayList = JSONArray(id)

            val data_user = JSONObject(data_arrayList.getJSONObject(0).toString())


            val u = User()
            u.set_user(data_user["id"].toString(), data_user["nombre"].toString(),data_user["apellido"].toString(),
                    data_user["direccion"].toString(),data_user["telefono"].toString(),data_user["correo"].toString(),
                    data_user["fecha_ultimo_ingreso"].toString())
        }
        catch (e: Exception){


            val data_arrayList = JSONObject(id)
            val u = User()
            u.set_user(data_arrayList["id"].toString(), "","",
                    "","",data,
                    "")
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)

                if (account != null){
                    val credential: AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)

                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful) {
                            showHome(account.email ?: "", ProviderType.GOOGLE)
                        } else {
                            showAlert()
                        }
                    }
                }
            }catch (e: ApiException){
                showAlert()
            }



        }
    }

    private fun sesion() {
        val prefs : SharedPreferences = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email:String? = prefs.getString("email", null)
        val provider:String? = prefs.getString("provider", null)

        if(email != null && provider != null){
            login_LinearLayout.visibility = View.INVISIBLE
            showHome(email, ProviderType.valueOf(provider))
        }
    }

    private fun leer_user(): Boolean? {
        val sharpref = getPreferences(Context.MODE_PRIVATE)
        val valor = sharpref.getString("user", "vacio")

        if(valor != "vacio"){
            guardarUsuario(valor.toString())

            return true
        }else{
            return false
        }


    }
    private fun guardarUsuario(correo:String){
        //se consulta el servicio
        var queue = Volley.newRequestQueue(this)
        queue.getCache().clear();
        var url = "https://imbcol.com/grupoess/logueo.php"

        val postRequest: StringRequest = object : StringRequest(
                Request.Method.POST, url,
                Response.Listener { response -> // response

                    guardar_data(correo,response) //Guarda en cache
                },
                Response.ErrorListener { // error
                    Log.i("Alerta","Error al intentar cargar las variables contacte con el administrador")
                }
        ) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["clave"] = "R3J1cG9Fc3M"
                params["nombre"] = ""
                params["apellido"] = ""
                params["direccion"] = ""
                params["telefono"] = ""
                params["correo"] = correo
                params["clave_user"] = ""
                return params
            }
        }
        queue.add(postRequest)
    }


}