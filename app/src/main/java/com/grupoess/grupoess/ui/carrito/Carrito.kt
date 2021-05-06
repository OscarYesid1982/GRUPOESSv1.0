package com.grupoess.grupoess.ui.carrito

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
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.grupoess.grupoess.adapater.ProductosAdaptador2
import com.grupoess.grupoess.R
import com.grupoess.grupoess.model.Productos_object
import com.grupoess.grupoess.model.Seleccion
import com.grupoess.grupoess.model.User
import com.grupoess.grupoess.model.convertir_utd8
import com.grupoess.grupoess.ui.carrito.adapter.AdaptersCarrito
import com.grupoess.grupoess.ui.carrito.adapter.ProductosCarAdaptador
import com.grupoess.grupoess.ui.login.Datos_Usuario
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.carrito_activity.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_produc_car.*
import org.json.JSONArray
import org.json.JSONObject


class Carrito : AppCompatActivity(), AdapterView.OnItemClickListener{

    var total = 0
    var envio = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.carrito_activity)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val spiOpcEntrega = findViewById<Spinner>(R.id.carrito_spinner)
        val opcionEntrega = resources.getStringArray(R.array.opciones_entrega)
        val adaptadorEntrega = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, opcionEntrega)
        spiOpcEntrega.adapter = adaptadorEntrega

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            val sendIntent = Intent()
            sendIntent.setAction(Intent.ACTION_VIEW)
            var uri = "whatsapp://send?phone=+573014451499"+"&text= Hola soy "+
                    User().get_correo().toString()+" Necesito información."
            sendIntent.setData(Uri.parse(uri))
            startActivity(sendIntent)
        }

        traer_productos_Carrito()
        pagar()
    }
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        TODO("Not yet implemented")
    }

    private fun traer_productos_Carrito() {
        var u = User()

        car_dirPedido.text = u.get_direccion()

        //se consulta el servicio
        var queue = Volley.newRequestQueue(this)
        var url = "https://imbcol.com/grupoess/traer_carrito.php"
        val postRequest: StringRequest = object : StringRequest(Request.Method.POST, url,
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
                params["id_user"] = u.get_id()
                return params
            }
        }
        queue.add(postRequest)
    }

    @SuppressLint("WrongConstant")
    private fun covertir_json2(response: String) {
        var data_arraylist: ArrayList<Productos_object> = ArrayList()
        val data_ini = JSONObject(response)
        val data = JSONArray(data_ini["data"].toString())
        //var cat = Seleccion();
        var data_utf8 = convertir_utd8();

        for (i in 0 until data.length()) {
            val data_product = JSONObject(data.getJSONObject(i).toString())


            data_arraylist.add(
                    Productos_object(
                            data_utf8.get_text(data_product["imagen"].toString()),
                            data_utf8.get_text(data_product["name"].toString()),
                            data_product["id_wordpress"].toString().toInt(),
                            data_utf8.get_text(data_product["descripcion"].toString()),
                            data_utf8.get_text(data_product["precio"].toString()),
                            data_utf8.get_text(data_product["nombre_categoria"].toString()),
                            data_utf8.get_text(data_product["cantidad"].toString())
                    )
            )

            total += (data_utf8.get_text(data_product["precio"].toString()).toInt() *
                    data_utf8.get_text(data_product["cantidad"].toString()).toInt())


        }

        car_TProductos.text = "$ " + total.toString() + " COP"

        envio = total * 0.25

        car_costoEnvio.text = "$ " + envio.toString() + " COP"

        car_TotalPagar.text = "$ " + (total + envio).toDouble().toString() + " COP"


        fr_Car_Productos.layoutManager = LinearLayoutManager(this, 1, false)
        fr_Car_Productos.adapter = ProductosCarAdaptador(data_arraylist, this)

    }

    fun pagar(){
        car_btnPagar.setOnClickListener {
            var u = User()
            if(u.get_direccion() == ""){
                Toast.makeText(this,"Para realizar su compra debe estar regitrado", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Datos_Usuario::class.java)
                startActivityForResult(intent, 0)
                this.finish()
            }
            else{
                //Log.i("aaa","https://imbcol.com/grupoess/pago.php?id="+u.get_id() )
                val uri: Uri = Uri.parse("https://imbcol.com/grupoess/pago.php?id="+u.get_id())
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }
        }

    }
}
