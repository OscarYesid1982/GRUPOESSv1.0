package com.grupoess.grupoess.ui.productos
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.example.grupoess.adapater.CategoriaAdaptadorSeleccionProducto
import com.example.grupoess.adapater.ProductosSimilaresAdaptador
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.grupoess.grupoess.R
import com.grupoess.grupoess.model.*
import com.grupoess.grupoess.ui.carrito.Carrito
import com.grupoess.grupoess.ui.home.adapter.IntroSlide
import com.grupoess.grupoess.ui.home.adapter.SliderHomeAdapter
import com.grupoess.grupoess.ui.login.Datos_Usuario
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.seleccion_producto.*
import kotlinx.android.synthetic.main.sliderhome.introSliderViewPager2
import org.json.JSONArray
import org.json.JSONObject

class Seleccion_Producto : AppCompatActivity() {

    var mDialog: android.app.AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.seleccion_producto)

        mDialog = SpotsDialog.Builder()
            .setContext(this)
            .setMessage("Espere un momento")
            .setCancelable(false).build()

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

        productosSimilares()

        //traer_slider()

        var cat = Seleccion();
        traer_producto_seleccionado(cat.get_id_producto())


        seleccion_producto_id_compra.setOnClickListener {
            mDialog?.show()


            if(SP_spinnerCantidad.getSelectedItem().toString() != "Elija cantidad"){
                var cat = Seleccion();
                var u = User()

                //se consulta el servicio
                var queue = Volley.newRequestQueue(this)
                queue.getCache().clear();
                var url = "https://imbcol.com/grupoess/registrar_seleccion_usuario.php"
                val postRequest: StringRequest = object : StringRequest(
                        Request.Method.POST, url,
                        Response.Listener { response -> // response
                            //el texto que viene lo convertimos de string a json
                            comprobar_respuesta(response);
                        },
                        Response.ErrorListener { // error
                            Log.i("Alerta", "Error al intentar cargar las variables contacte con el administrador")
                        }
                ) {
                    override fun getParams(): Map<String, String> {
                        val params: MutableMap<String, String> = HashMap()
                        params["clave"] = "R3J1cG9Fc3M"
                        params["id_usuario"] = u.get_id()
                        params["id_producto"] = cat.get_id_producto().toString()
                        params["cantidad"] = SP_spinnerCantidad.getSelectedItem().toString()
                        return params
                    }
                }
                queue.add(postRequest)


            }
            else{
                Toast.makeText(this, "Debe seleccionar cantidad de producto a comprar",
                        Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.Car -> {
                mDialog?.show()
                val intent = Intent(this, Carrito::class.java)
                startActivityForResult(intent, 0)
                finish()
            }
            R.id.Usuario -> {
                mDialog?.show()
                val intent = Intent(this, Datos_Usuario::class.java)
                startActivityForResult(intent, 0)
            }
            R.id.HistoricoCompras -> {
                mDialog?.show()
                val intent = Intent(this, Historico_Compras_Activity::class.java)
                startActivityForResult(intent, 0)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun comprobar_respuesta(response: String?) {

        Toast.makeText(this, "Producto agregado al carrito", Toast.LENGTH_SHORT).show()
        mDialog?.dismiss()
    }

    private fun traer_producto_seleccionado(id: Int) {
        //se consulta el servicio
        var queue = Volley.newRequestQueue(this)
        queue.getCache().clear();
        var url = "https://imbcol.com/grupoess/traer_producto_seleccion.php"
        val postRequest: StringRequest = object : StringRequest(
                Request.Method.POST, url,
                Response.Listener { response -> // response
                    //el texto que viene lo convertimos de string a json
                    covertir_json(response, queue)
                },
                Response.ErrorListener { // error
                    Log.i("Alerta", "Error al intentar cargar las variables contacte con el administrador")
                }
        ) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["clave"] = "R3J1cG9Fc3M"
                params["id"] = id.toString()
                return params
            }
        }
        queue.add(postRequest)
        traer_categorias()
    }

    private fun covertir_json(response: String, queue: RequestQueue) {
        val data_ini = JSONObject(response)
        val data = JSONArray(data_ini["data"].toString())
        var data_utf8 = convertir_utd8();

        for (i in 0 until data.length()) {
            val data_product = JSONObject(data.getJSONObject(i).toString())
            seleccion_producto_id_titulo.text = data_utf8.get_text(data_product["name"].toString())
            seleccion_producto_id_descripcion.text = data_utf8.get_text(data_product["descripcion"].toString())
            /*Picasso.with(this)
                    .load(data_product["imagen"].toString())
                    .error(R.drawable.ic_launcher_background) //en caso que la url no sea válida muestro otra imagen
                    .into(seleccion_producto_id_imagen)
                    ;
             */

            traer_slider()
            //traer_ImagenProducto()


            seleccion_producto_valor_Producto.text = data_utf8.get_text("$ " + data_product["precio"].toString())

            val spinnerCantidad = findViewById<Spinner>(R.id.SP_spinnerCantidad)
            val opcionCantidad = resources.getStringArray(R.array.opciones_Cantidad)
            var valor_total = findViewById<TextView>(R.id.seleccion_producto_valor_total)

            val adaptadorCantidad = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, opcionCantidad)
            spinnerCantidad.adapter = adaptadorCantidad

            spinnerCantidad.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                ) {
                    //Toast.makeText(this@Seleccion_Producto, opcionCantidad[position],Toast.LENGTH_SHORT).show()
                    if(opcionCantidad[position] != "Elija cantidad") {
                        valor_total.text = "$ " + (opcionCantidad[position].toInt() * data_product["precio"].toString().toInt()).toString()
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        }
        queue.stop()
    }

    private fun productosSimilares() {
        var cat = Seleccion();
        //se consulta el servicio
        var queue = Volley.newRequestQueue(this)
        queue.getCache().clear();
        var url = "https://imbcol.com/grupoess/traer_productos_similares.php"
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
                params["id"] = cat.get_id_producto().toString()
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
        var data_utf8 = convertir_utd8();

        for (i in 0 until data.length()) {
            val data_categpry = JSONObject(data.getJSONObject(i).toString())

            data_arraylist.add(
                    Categorias_object(
                            data_categpry["imagen"].toString(),
                            data_utf8.get_text(data_categpry["name"].toString()),
                            data_categpry["id_wordpress"].toString().toInt(),
                            1
                    )
            )

        }
        selProd_recProdSimilares.layoutManager = LinearLayoutManager(this, 0, false)
        selProd_recProdSimilares.adapter = ProductosSimilaresAdaptador(data_arraylist, this)
    }

    private fun traer_categorias() {
        //se consulta el servicio
        var queue = Volley.newRequestQueue(this)
        var url = "https://imbcol.com/grupoess/traer_categorias.php"
        val postRequest: StringRequest = object : StringRequest(
                Request.Method.POST, url,
                Response.Listener { response -> // response
                    //el texto que viene lo convertimos de string a json
                    covertir_json3(response)
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
    private fun covertir_json3(response: String) {
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
                            data_categpry["id_wordpress"].toString().toInt(),0
                    )
            )
        }
        seleccion_rc_categorias.layoutManager = LinearLayoutManager(this, 0, false)
        seleccion_rc_categorias.adapter = CategoriaAdaptadorSeleccionProducto(data_arraylist, this)
    }
/*
    private fun traer_ImagenProducto2() {
        var cat = Seleccion();
        //se consulta el servicio
        var queue = Volley.newRequestQueue(this)
        var url = "https://imbcol.com/grupoess/traer_imagen_seleccion.php"
        val postRequest: StringRequest = object : StringRequest(
                Request.Method.POST, url,
                Response.Listener { response -> // response
                    //el texto que viene lo convertimos de string a json
                    covertir_json_ImagenProducto(response)
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
                params["id"] = cat.get_id_producto().toString()
                return params
            }
        }
        queue.add(postRequest)
    }
    @SuppressLint("WrongConstant")
    private fun covertir_json_ImagenProducto2(response: String) {
        var data_arraylist: ArrayList<Imagenes_SeleccionProducto> = ArrayList()
        val data_ini = JSONObject(response)
        val data = JSONArray(data_ini["data"].toString())
        var data_utf8 = convertir_utd8();
        for (i in 0 until data.length()) {
            val data_category = JSONObject(data.getJSONObject(i).toString())
            data_arraylist.add(
                    Imagenes_SeleccionProducto(
                            data_category["imagen"].toString()
                    )
            )
        }
        selProducto_recycler.layoutManager = LinearLayoutManager(this, 0, false)
        selProducto_recycler.adapter = ImagenAdaptadorSeleccionProducto(data_arraylist, this)
    }
 */
    /*
    private fun traer_ImagenProducto(){
        var cat = Seleccion();
        //se consulta el servicio
        var queue = Volley.newRequestQueue(this)
        var url = "https://imbcol.com/grupoess/traer_imagen_seleccion.php"
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
                params["id"] = cat.get_id_producto().toString()
                return params
            }
        }
        queue.add(postRequest)
    }
    private fun covertir_jsonSlider(response: String?) {
        val data_ini = JSONObject(response)
        val data = JSONArray(data_ini["data"].toString())
        var data_categpry = JSONObject(data.getJSONObject(0).toString())
        Log.i("Dataa", data.getJSONObject(0).toString())
        var list = mutableListOf(
            IntroSlide(
                "Imagen Slider 1",
                "Descripción de la primera imagen como Slider",
                data_categpry["imagen"].toString()
            )
        );
        for (i in 1 until data.length()) {
            data_categpry = JSONObject(data.getJSONObject(i).toString())
            list.addAll(
                listOf(
                    IntroSlide(
                        "Imagen Slider 1",
                        "Descripción de la primera imagen como Slider",
                        data_categpry["imagen"].toString()
                    )
                )
            )
        }
        val introSliderAdapter = SliderHomeAdapter(list)
        // Config Slider Home
        introSliderViewPager2.adapter = introSliderAdapter
    }
    /*
    private fun covertir_json_ImagenProducto(response: String?) {
        val data_ini = JSONObject(response)
        val data = JSONArray(data_ini["data"].toString())
        var data_category = JSONObject(data.getJSONObject(0).toString())
        var list = mutableListOf(
                ImagenSeleccionProductoSlide(
                        data_category["imagen"].toString()
                )
        );
        for (i in 1 until data.length()) {
            data_category = JSONObject(data.getJSONObject(i).toString())
            list.addAll(
                    listOf(
                            ImagenSeleccionProductoSlide(
                                   data_category["imagen"].toString()
                            )
                    )
            )
        }
        val introSliderAdapter = ImagenAdaptadorSeleccionProducto(list, this)
        // Config Slider Home
        slider_Seleccion_Producto.adapter = introSliderAdapter
    }
     */
     */

    private fun traer_slider(){
        var cat = Seleccion();

        //se consulta el servicio
        var queue = Volley.newRequestQueue(this)
        var url = "https://imbcol.com/grupoess/traer_imagen_seleccion.php"
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
                params["id"] = cat.get_id_producto().toString()
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
                        data_categpry["imagen"].toString()
                )
        );

        for (i in 1 until data.length()) {
            data_categpry = JSONObject(data.getJSONObject(i).toString())

            list.addAll(
                    listOf(
                            IntroSlide(
                                    "Imagen Slider 1",
                                    "Descripción de la primera imagen como Slider",
                                    data_categpry["imagen"].toString()
                            )
                    )
            )
        }

        val introSliderAdapter = SliderHomeAdapter(list)
        // Config Slider Home
        introSliderViewPager2.adapter = introSliderAdapter


    }

}