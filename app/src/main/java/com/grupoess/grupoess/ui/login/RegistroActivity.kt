package com.grupoess.grupoess.ui.login

import android.R.attr.password
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.grupoess.grupoess.ProviderType
import com.grupoess.grupoess.R
import com.grupoess.grupoess.alertas.Alertas
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_registro.*
import org.json.JSONObject


class RegistroActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    var mDialog: android.app.AlertDialog? = null

    var mensaje_Alertas = Alertas()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        mDialog = SpotsDialog.Builder()
            .setContext(this)
            .setMessage("Espere un momento")
            .setCancelable(false).build()

        mAuth = FirebaseAuth.getInstance();

        registro_InicioSesion.setOnClickListener {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            finish()
        }
        registro_Condiciones2.setOnClickListener {
            val uri: Uri = Uri.parse("https://grupoess.com/terminos-y-condiciones/")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        registroUsuario()
    }

    private fun registroUsuario() {

        registro_CrearCuenta.setOnClickListener {

            //Registro Firebase
            if (registro_contrasena.length()<7){
                    mensaje_Alertas.mensaje(
                        "Alerta",
                        "La contraseña debe tener mínimo 7 caracteres",
                        "Aceptar",
                        this
                    )

            }
            else if (registro_Nombre.text.toString() == "" || registro_Apellidos.text.toString() =="" || registro_Email.text.toString() =="") {
                mensaje_Alertas.mensaje(
                    "Alerta",
                    "Debe completar toda la información requerida",
                    "Aceptar",
                    this
                )
            }
            else{
                //Registro Firebase
                mDialog?.show()
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(registro_Email.text.toString(),registro_contrasena.text.toString())
                    .addOnCompleteListener {
                    if (it.isSuccessful) {
                        showHome(it.result?.user?.email ?: "", ProviderType.BASIC)
                    } else {

                        showAlert()
                    }
                }
                //Registro MySql
                var queue = Volley.newRequestQueue(this)
                var url = "https://imbcol.com/grupoess/registrar_usuario_v1.php"
                val postRequest: StringRequest = object : StringRequest(
                    Request.Method.POST, url,
                    Response.Listener { response -> // response
                        //el texto que viene lo convertimos de string a json
                        comprobar_respuesta(response);
                    },
                    Response.ErrorListener { // error
                        Log.i(
                            "Alerta",
                            "Error al intentar cargar las variables contacte con el administrador"
                        )
                    }
                ) {
                    override fun getParams(): Map<String, String> {
                        val params: MutableMap<String, String> = HashMap()
                        params["clave"] = "R3J1cG9Fc3M"
                        params["nombre"] = registro_Nombre.text.toString()
                        params["apellido"] = registro_Apellidos.text.toString()
                        params["direccion"] = "Vacio"
                        params["telefono"] = "Vacio"
                        params["correo"] = registro_Email.text.toString()
                        params["clave_user"] = registro_contrasena.text.toString()

                        return params
                    }
                }
                queue.add(postRequest)
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
    fun showHome(email: String, provider: ProviderType){

        val homeIntent:Intent = Intent(this, LoginActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
    }
    private fun comprobar_respuesta(response: String) {
        val respuesta = JSONObject(response);
        val i = Intent(this, LoginActivity::class.java)
        startActivity(i)
    }
}
