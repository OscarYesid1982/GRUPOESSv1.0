package com.grupoess.grupoess.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.grupoess.grupoess.MainActivity
import com.grupoess.grupoess.R
import com.grupoess.grupoess.SplashActivity
import com.grupoess.grupoess.model.User
import com.grupoess.grupoess.ui.home.HomeActivity
import com.grupoess.grupoess.ui.login.RegistroActivity
import com.grupoess.grupoess.ui.productos.Historico_Compras_Activity
import kotlinx.android.synthetic.main.activity_seleccion_inico.*
import org.json.JSONArray
import org.json.JSONObject

class SeleccionInico : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccion_inico)

            boton_ir_home.setOnClickListener {
                Toast.makeText(this, "Prueba inicio MainActivity", Toast.LENGTH_SHORT).show()
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
                finish()
            }
            boton_ir_hist_compras.setOnClickListener {
                Toast.makeText(this, "Prueba inicio Historico", Toast.LENGTH_SHORT).show()
                val i = Intent(this, Historico_Compras_Activity::class.java)
                startActivity(i)
                finish()
            }
    }
/*
    private fun leer_user(): Boolean? {
        val sharpref = getPreferences(Context.MODE_PRIVATE)
        val valor = sharpref.getString("user", "vacio")

        if (valor != "vacio") {
            guardarUsuario(valor.toString())

            return true
        } else {
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

 */
}