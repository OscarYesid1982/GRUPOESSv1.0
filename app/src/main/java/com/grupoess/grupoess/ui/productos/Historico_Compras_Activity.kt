package com.grupoess.grupoess.ui.productos

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.grupoess.grupoess.R
import com.grupoess.grupoess.adapater.ProductosAdaptador2
import com.grupoess.grupoess.model.*
import com.grupoess.grupoess.ui.carrito.Carrito
import com.grupoess.grupoess.ui.carrito.adapter.ProductosCarAdaptador
import com.grupoess.grupoess.ui.login.Datos_Usuario
import com.grupoess.grupoess.ui.productos.adapter.HistoricoComprasAdaptador
import kotlinx.android.synthetic.main.activity_historico__compras_.*
import kotlinx.android.synthetic.main.carrito_activity.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.frHome_recyclerProductos
import org.json.JSONArray
import org.json.JSONObject

class Historico_Compras_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historico__compras_)

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            val sendIntent = Intent()
            sendIntent.setAction(Intent.ACTION_VIEW)
            var uri = "whatsapp://send?phone=+573014451499"+"&text= Hola soy "+
                    User().get_correo().toString()+" Necesito información."
            sendIntent.setData(Uri.parse(uri))
            startActivity(sendIntent)
        }
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        traer_producFavoritos()
        traer_Historico_Compras()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.Car -> {
                val intent = Intent(this, Carrito::class.java)
                startActivityForResult(intent, 0)

            }
            R.id.Usuario -> {
                val intent = Intent(this, Datos_Usuario::class.java)
                startActivityForResult(intent, 0)
            }
            R.id.HistoricoCompras -> {
                val intent = Intent(this, Historico_Compras_Activity::class.java)
                startActivityForResult(intent, 0)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun traer_producFavoritos() {
        //se consulta el servicio
        var queue = Volley.newRequestQueue(this)
        var url = "https://imbcol.com/grupoess/traer_productos.php"
        val postRequest: StringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response -> // response
                //el texto que viene lo convertimos de string a json
                covertir_json2(response)
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
                return params
            }
        }
        queue.add(postRequest)
    }

    @SuppressLint("WrongConstant")
    private fun covertir_json2(response: String) {
        var data_arraylist: ArrayList<Productos_object2> = ArrayList()
        val data_ini = JSONObject(response)
        val data = JSONArray(data_ini["data"].toString())
        //var cat = Seleccion();
        var data_utf8 = convertir_utd8();

        var imagen = ""
        var id = ""
        var nombre = ""

        for (i in 0 until data.length()) {

            val data_product = JSONObject(data.getJSONObject(i).toString())

            if (i%2 == 0){
                imagen = data_utf8.get_text(data_product["imagen"].toString())
                id  = data_product["id_wordpress"].toString()
                nombre = data_utf8.get_text(data_product["name"].toString())
            }
            else{
                data_arraylist.add(
                    Productos_object2(
                        data_utf8.get_text(data_product["imagen"].toString()),
                        data_utf8.get_text(data_product["name"].toString()),
                        data_product["id_wordpress"].toString().toInt(),
                        imagen,
                        nombre,
                        id.toInt()

                    )

                )
            }
        }
        frHome_recyclerProductos.layoutManager = LinearLayoutManager(this, 0, false)
        frHome_recyclerProductos.adapter = ProductosAdaptador2(data_arraylist, this)

    }

    private fun traer_Historico_Compras() {
        var u = User()

//        car_dirPedido.text = u.get_direccion()

        //se consulta el servicio
        var queue = Volley.newRequestQueue(this)
        var url = "https://imbcol.com/grupoess/historico_compras.php"
        val postRequest: StringRequest = object : StringRequest(Request.Method.POST, url,
            Response.Listener { response -> // response
                //el texto que viene lo convertimos de string a json
                covertir_json(response)

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
                params["id"] = u.get_id()
                return params
            }
        }
        queue.add(postRequest)
    }

    @SuppressLint("WrongConstant")
    private fun covertir_json(response: String) {
        var u = User()
        var data_arraylist: ArrayList<Historico_Productos_object> = ArrayList()
        val data_ini = JSONObject(response)
        val data = JSONArray(data_ini["data"].toString())
        
        var data_utf8 = convertir_utd8();

        for (i in 0 until data.length()) {
            val data_product = JSONObject(data.getJSONObject(i).toString())


            data_arraylist.add(
                Historico_Productos_object(
                    data_utf8.get_text(data_product["fecha_compra"].toString()),
                    data_utf8.get_text(data_product["id"].toString()).toInt(),
                    data_utf8.get_text(data_product["total_productos"].toString()),
                    "$ " + data_utf8.get_text(data_product["valor"].toString()) + " COP",
                )
            )
        }

        hist_RC_compras.layoutManager = LinearLayoutManager(this, 1, false)
        hist_RC_compras.adapter = HistoricoComprasAdaptador(data_arraylist, this)

    }


}