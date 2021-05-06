package com.grupoess.grupoess.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.view.get
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.grupoess.grupoess.R
import com.grupoess.grupoess.model.User
import com.grupoess.grupoess.ui.carrito.Carrito
import kotlinx.android.synthetic.main.activity_datos__usuario.*

class Datos_Usuario : AppCompatActivity() {

    var genero= ""
    val u = User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datos__usuario)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        du_CorreoUsuario.text = u.get_correo().toString()

        val spinnerMes = findViewById<Spinner>(R.id.du_spinnerMes)
        val opcionMes = resources.getStringArray(R.array.opciones_Mes)
        val adaptadorMes = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, opcionMes)
        spinnerMes.adapter = adaptadorMes

        val spinnerDia = findViewById<Spinner>(R.id.du_spinnerDia)
        val opcionDia = resources.getStringArray(R.array.opciones_Dia)
        val adaptadorDia = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, opcionDia)
        spinnerDia.adapter = adaptadorDia



        //Opciones RadioGroup
        du_RadioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.du_radio_H){
                genero = "1"

            }
            if (checkedId == R.id.du_radio_M){

                genero = "0"
            }
        }

        du_actualizarDatos.setOnClickListener {
            actualizarUsuario()

            val intent = Intent(this, Carrito::class.java)
            startActivityForResult(intent, 0)
            Toast.makeText(this, "Datos actualizados, gracias!",Toast.LENGTH_SHORT).show()
            this.finish()
        }
    }

    private fun actualizarUsuario(){
        //se consulta el servicio
        var queue = Volley.newRequestQueue(this)
        queue.getCache().clear();
        var url = "https://imbcol.com/grupoess/actualizar_usuario.php"

        val postRequest: StringRequest = object : StringRequest(
                Request.Method.POST, url,
                Response.Listener { response -> // response


                },
                Response.ErrorListener { // error
                   // Log.i("Alerta","Error al intentar cargar las variables contacte con el administrador")
                }
        ) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()

                params["clave"] = "R3J1cG9Fc3M"
                params["id"] = u.get_id()
                params["nombre"] = du_nombreUsuario.text.toString()
                params["apellido"] = du_apellidoUsuario.text.toString()
                params["telefono"] = du_celularUsuario.text.toString()
                params["direccion"] = du_direccionUsuario.text.toString()
                params["cumplea"] = du_spinnerMes.getSelectedItem().toString()+"/"+du_spinnerDia.getSelectedItem().toString()
                params["genero"] = genero

                u.set_user(u.get_id(), du_nombreUsuario.text.toString(),du_apellidoUsuario.text.toString(),du_direccionUsuario.text.toString(),
                        du_celularUsuario.text.toString(),u.get_correo(),u.get_fecha_ultimo_ingreso())
                return params
            }
        }
        queue.add(postRequest)
    }
}