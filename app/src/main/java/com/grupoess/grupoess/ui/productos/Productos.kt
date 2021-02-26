package com.grupoess.grupoess.ui.productos

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.grupoess.adapater.CategoriaAdaptador
import com.example.grupoess.adapater.ProductosSimilaresAdaptador
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.grupoess.grupoess.R
import com.grupoess.grupoess.SplashActivity
import com.grupoess.grupoess.model.*
import com.grupoess.grupoess.ui.carrito.Carrito
import com.grupoess.grupoess.ui.login.Datos_Usuario
import com.grupoess.grupoess.ui.productos.adapter.LanguageAdaptersProductos
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.frHome_recyclerCategorias
import kotlinx.android.synthetic.main.productos.*
import org.json.JSONArray
import org.json.JSONObject


class Productos : AppCompatActivity(), AdapterView.OnItemClickListener {
    private var arrayList:ArrayList<Productos_object> ? = null
    private var gridView:GridView ? = null
    private var languageAdapters: LanguageAdaptersProductos? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.productos)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {

            val sendIntent = Intent()
            sendIntent.setAction(Intent.ACTION_VIEW)
            var uri = "whatsapp://send?phone=+573014451499"+"&text= Hola soy "+
                    User().get_correo().toString()+" Necesito información."
            sendIntent.setData(Uri.parse(uri))
            startActivity(sendIntent)
        }

        //traer las productos
        traer_productos()
        traer_categorias()
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


    private fun traer_categorias() {
        //se consulta el servicio
        var queue = Volley.newRequestQueue(this)
        var url = "https://imbcol.com/grupoess/traer_categorias.php"
        val postRequest: StringRequest = object : StringRequest(
                Request.Method.POST, url,
                Response.Listener { response -> // response
                    //el texto que viene lo convertimos de string a json
                    covertir_json_similares(response)
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
    private fun covertir_json_similares(response: String) {
        var data_arraylist: ArrayList<Categorias_object> = ArrayList()
        val data_ini = JSONObject(response)
        val data = JSONArray(data_ini["data"].toString())
        var data_utf8 = convertir_utd8();

        for (i in 0 until data.length()) {
            val data_categpry = JSONObject(data.getJSONObject(i).toString())

            data_arraylist.add(
                    Categorias_object(
                            data_categpry["img"].toString(),
                            data_utf8.get_text(data_categpry["name"].toString()),
                            data_categpry["id_wordpress"].toString().toInt(),
                        0
                    )
            )

        }

        Produc_Similares.layoutManager = LinearLayoutManager(this, 0, false)
        Produc_Similares.adapter = ProductosSimilaresAdaptador(data_arraylist, this)

    }

    private fun traer_productos(){
        //se consulta el servicio
        var queue = Volley.newRequestQueue(this)
        var url = "https://imbcol.com/grupoess/traer_productos.php"
        val postRequest: StringRequest = object : StringRequest(Request.Method.POST, url,
            Response.Listener { response -> // response
                //el texto que viene lo convertimos de string a json
                covertir_json(response)
            },
            Response.ErrorListener { // error
                Log.i("Alerta","Error al intentar cargar las variables contacte con el administrador")
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

    private fun covertir_json(response: String) {
        var data_arraylist:ArrayList<Productos_object> = ArrayList()
        val data_ini = JSONObject(response)
        val data = JSONArray(data_ini["data"].toString())
        var cat = Seleccion();
        var data_utf8 = convertir_utd8();

        for (i in 0 until data.length()) {
            val data_product = JSONObject(data.getJSONObject(i).toString())

            val s = Seleccion()

            if (data_product["id_categoria"].toString().toInt() == s.get_id_categoria()){
                data_arraylist.add(Productos_object(
                        data_utf8.get_text(data_product["imagen"].toString()),
                        data_utf8.get_text(data_product["name"].toString()),
                        data_utf8.get_text(data_product["id_wordpress"].toString()).toInt(),
                        data_utf8.get_text(data_product["descripcion"].toString()),
                        "", "",""))
            }
        }

        //se toma el grid_view_contet_main
        arrayList = data_arraylist;
        gridView = findViewById(R.id.grid_view_contet_main_Productos)
        languageAdapters = LanguageAdaptersProductos(applicationContext, data_arraylist!!)
        gridView?.adapter = languageAdapters
        gridView?.onItemClickListener = this

    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var items: Productos_object = arrayList!!.get(position)
        var cat = Seleccion();

        cat.set_id_producto(items.id!!)

        //se consulta el servicio
        var queue = Volley.newRequestQueue(this)
        var url = "https://imbcol.com/grupoess/cant_clicks.php"
        val postRequest: StringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response -> // response
                //el texto que viene lo convertimos de string a json

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
                params["id"] = items.id.toString()
                return params
            }
        }
        queue.add(postRequest)
        val intent = Intent(this, Seleccion_Producto::class.java)
        startActivityForResult(intent, 0)
    }
}