package com.grupoess.grupoess.ui.productos

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuItemCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.grupoess.adapater.CategoriaAdaptador
import com.example.grupoess.adapater.ProductosAdaptador
import com.example.grupoess.adapater.ProductosSimilaresAdaptador
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.grupoess.grupoess.R
import com.grupoess.grupoess.SplashActivity
import com.grupoess.grupoess.adapater.ProductosAdaptador2
import com.grupoess.grupoess.model.*


import com.grupoess.grupoess.ui.productos.adapter.LanguageAdaptersProductos
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.frHome_recyclerCategorias
import kotlinx.android.synthetic.main.productos.*
import org.json.JSONArray
import org.json.JSONObject


class Buscar_Productos : AppCompatActivity()
//        ,AdapterView.OnItemClickListener
{

   // var sname: Array<Productos_object2> = arrayOf("Oscar","Maria","Orlando","Osvaldo","Marisol","Pedro","Pedrosito",
   //         "Mariela","Marielita","Jose","Josesito","Alexandra","Alex","Alexa M","Alexa F","Oscar G","Maria H","Orlando Z")

    lateinit var adapter : Productos_object2
    lateinit var searchView: SearchView
  //  lateinit var toolbar: Toolbar
    lateinit var recyclerview : RecyclerView
    var list: ArrayList<Productos_object2> = ArrayList()
    lateinit var layoutManager: RecyclerView.LayoutManager


    private var arrayList:ArrayList<Productos_object> ? = null
    private var gridView:GridView ? = null
    private var languageAdapters: LanguageAdaptersProductos? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.buscar_productos)

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
               // adapter.filter.filter("")
                return false
            }

        })


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
        //traer_categorias()


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

        var recyclerview = findViewById(R.id.recyclerHome) as RecyclerView
        var layoutManager = LinearLayoutManager(this)
        recyclerview.layoutManager = layoutManager
        recyclerview.setHasFixedSize(true)




        for (i in 0 until data.length()) {

            val data_product = JSONObject(data.getJSONObject(i).toString())

            val s = Seleccion()

            data_arraylist.add(Productos_object(
                    data_utf8.get_text(data_product["imagen"].toString()),
                    data_utf8.get_text(data_product["name"].toString()),
                    data_utf8.get_text(data_product["id_wordpress"].toString()).toInt(),
                    data_utf8.get_text(data_product["descripcion"].toString()),
                    "", "",""))



            list.add(Productos_object2(
                    data_utf8.get_text(data_product["imagen"].toString()),
                    data_utf8.get_text(data_product["name"].toString()),
                    data_utf8.get_text(data_product["id_wordpress"].toString()).toInt(),

                    data_utf8.get_text(data_product["imagen"].toString()),
                    data_utf8.get_text(data_product["name"].toString()),
                    data_utf8.get_text(data_product["id_wordpress"].toString()).toInt()
                    ))
        }

        //adapter = Productos_object2(list,this)
        recyclerview.adapter=ProductosAdaptador2(list, this)


    }
/*
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
            R.id.Compras -> {
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

                data_arraylist.add(Productos_object(
                        data_utf8.get_text(data_product["imagen"].toString()),
                        data_utf8.get_text(data_product["name"].toString()),
                        data_utf8.get_text(data_product["id_wordpress"].toString()).toInt(),
                        data_utf8.get_text(data_product["descripcion"].toString()),
                        "", "",""))

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

 */

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        var item: MenuItem = menu!!.findItem(R.id.Search)
        searchView = MenuItemCompat
                .getActionView(item) as SearchView
        MenuItemCompat.setOnActionExpandListener(item, object : MenuItemCompat.OnActionExpandListener{
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                toolbar.setBackgroundColor(Color.GRAY)
                (searchView.findViewById(androidx.appcompat.R.id.search_src_text) as EditText)
                        .setHintTextColor(Color.BLACK)
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                toolbar.setBackgroundColor(resources.getColor(R.color.design_default_color_on_primary))
                searchView.setQuery("", false)
                return true
            }
        })
        searchView.maxWidth = Int.MAX_VALUE
        //searchname(searchView)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item!!.itemId == R.id.Search){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (!searchView.isIconified){
            searchView.isIconified = true
            return
        }
        super.onBackPressed()
    }
}