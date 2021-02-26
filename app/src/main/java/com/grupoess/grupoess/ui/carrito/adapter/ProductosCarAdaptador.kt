package com.grupoess.grupoess.ui.carrito.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.grupoess.grupoess.R
import com.grupoess.grupoess.model.Productos_object
import com.grupoess.grupoess.model.Seleccion
import com.grupoess.grupoess.model.User
import com.grupoess.grupoess.ui.carrito.Carrito
import com.grupoess.grupoess.ui.productos.Seleccion_Producto
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.carrito_activity.*


class ProductosCarAdaptador(val datos: ArrayList<Productos_object>, val context: Context)
    : RecyclerView.Adapter<ProductosCarAdaptador.ViewHolder>()  {

    override fun getItemCount() = datos.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.nombre.text = datos.get(position).name
        holder.cantidad.text = datos.get(position).cantidad


        holder.precio.text = "$ " + (datos.get(position).precio.toString().toInt() *
                datos.get(position).cantidad.toString().toInt())  + " COP"



        Picasso.with(context)
            .load(datos.get(position).icons)
            .error(R.drawable.ic_launcher_background) //en caso que la url no sea válida muestro otra imagen
            .into(holder.imagen);

        holder.contenedor.setOnClickListener {

            val s = Seleccion()
            s.set_id_producto(datos.get(position).id.toString().toInt())
            val intent = Intent(context, Seleccion_Producto::class.java)
            context.startActivity(intent);
        }

        holder.eliminar.setOnClickListener {
            eliminar_productoCompra(datos.get(position).id.toString())
            Toast.makeText(context, "El producto se ha eliminado del carrito", Toast.LENGTH_SHORT).show()

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.item_produc_car, parent, false);
        return ViewHolder(view)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){



        val nombre: TextView = itemView.findViewById(R.id.item_produc_car_NomProduc)
        val imagen: ImageView = itemView.findViewById(R.id.item_car_Producto)
        val cantidad: TextView = itemView.findViewById(R.id.item_produc_car_Cantidad)
        val precio: TextView = itemView.findViewById(R.id.item_produc_car_ValorProdTotal)
        val contenedor: androidx.cardview.widget.CardView = itemView.findViewById(R.id.item_linear_ProductoCar)
        val eliminar: ImageButton = itemView.findViewById(R.id.car_eliminar_producto)
    }

    fun eliminar_productoCompra(id_producto: String){
        val u = User()
            //se consulta el servicio
            var queue = Volley.newRequestQueue(context)
            var url = "https://imbcol.com/grupoess/eliminar_producto_compra.php"
            val postRequest: StringRequest = object : StringRequest(
                Request.Method.POST, url,
                Response.Listener { response -> // response
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
                    params["id_producto"] = id_producto
                    params["id_usuario"] = u.get_id()
                    return params
                }
            }
            queue.add(postRequest)

    }
}
