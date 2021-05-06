package com.grupoess.grupoess

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.grupoess.adapater.CategoriaAdaptador
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.analytics.FirebaseAnalytics
import com.grupoess.grupoess.adapater.ProductosAdaptador2
import com.grupoess.grupoess.model.Categorias_object
import com.grupoess.grupoess.model.Productos_object2
import com.grupoess.grupoess.model.User
import com.grupoess.grupoess.model.convertir_utd8
import com.grupoess.grupoess.ui.carrito.Carrito
import com.grupoess.grupoess.ui.home.adapter.IntroSlide
import com.grupoess.grupoess.ui.home.adapter.SliderHomeAdapter
import com.grupoess.grupoess.ui.login.Datos_Usuario
import com.grupoess.grupoess.ui.login.LoginActivity
import com.grupoess.grupoess.ui.productos.Buscar_Productos
import com.grupoess.grupoess.ui.productos.Historico_Compras_Activity
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.sliderhome.*
import org.json.JSONArray
import org.json.JSONObject


enum class ProviderType{
    BASIC,
    GOOGLE
}

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {

            val sendIntent = Intent()
            sendIntent.setAction(Intent.ACTION_VIEW)
            var uri = "whatsapp://send?phone=+573014451499"+"&text= Hola soy "+
                    User().get_correo().toString()+" /n Necesito información."
            sendIntent.setData(Uri.parse(uri))
            startActivity(sendIntent)
        }

        //Analytics Event
        val analytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("Mensaje", "Integración Completa Ejemplo Enero")
        analytics.logEvent("InitScreen", bundle)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        traer_categorias()
        traer_producFavoritos()
        traer_slider()
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
            R.id.Search -> {
                val intent = Intent(this, Buscar_Productos::class.java)
                startActivityForResult(intent, 0)
            }
            /*
            R.id.logOut -> {

                finish()
            }

             */
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
                return params
            }
        }
        queue.add(postRequest)
    }

    @SuppressLint("WrongConstant")
    private fun covertir_json(response: String) {
        var data_arraylist: ArrayList<Categorias_object> = ArrayList()
        val data_ini = JSONObject(response)
        val data = JSONArray(data_ini["data"].toString())
        var data_utf8 = convertir_utd8()

        for (i in 0 until data.length()) {
            val data_categpry = JSONObject(data.getJSONObject(i).toString())

            data_arraylist.add(
                    Categorias_object(
                            data_categpry["img"].toString(),
                            data_utf8.get_text(data_categpry["name"].toString()),
                            data_categpry["id_wordpress"].toString().toInt(), 0
                    )
            )

        }

        frHome_recyclerCategorias.layoutManager = LinearLayoutManager(this, 0, false)
        frHome_recyclerCategorias.adapter = CategoriaAdaptador(data_arraylist, this)


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
        frHome_recyclerProductos.layoutManager = LinearLayoutManager(this, 1, false)
        frHome_recyclerProductos.adapter = ProductosAdaptador2(data_arraylist, this)

    }

    private fun traer_slider(){
        //se consulta el servicio
        var queue = Volley.newRequestQueue(this)
        var url = "https://imbcol.com/grupoess/traer_slider.php"
        val postRequest: StringRequest = object : StringRequest(Request.Method.POST, url,
                Response.Listener { response -> // response
                    //el texto que viene lo convertimos de string a json
                    covertir_jsonSlider(response)
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

    private fun covertir_jsonSlider(response: String?) {
        val data_ini = JSONObject(response)
        val data = JSONArray(data_ini["data"].toString())
        var data_categpry = JSONObject(data.getJSONObject(0).toString())
        var list = mutableListOf(
                IntroSlide(
                        "Imagen Slider 1",
                        "Descripción de la primera imagen como Slider",
                        data_categpry["Imagen"].toString()
                )
        );

        for (i in 1 until data.length()) {
            data_categpry = JSONObject(data.getJSONObject(i).toString())

            list.addAll(
                    listOf(
                            IntroSlide(
                                    "Imagen Slider 1",
                                    "Descripción de la primera imagen como Slider",
                                    data_categpry["Imagen"].toString()
                            )
                    )
            )
        }

        val introSliderAdapter = SliderHomeAdapter(list)
        // Config Slider Home
        introSliderViewPager2.adapter = introSliderAdapter
    }

}