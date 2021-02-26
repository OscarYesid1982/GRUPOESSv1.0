package com.grupoess.grupoess.adapater

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.grupoess.grupoess.R
import com.grupoess.grupoess.model.Productos_object
import com.grupoess.grupoess.model.Productos_object2
import com.grupoess.grupoess.model.Seleccion
import com.grupoess.grupoess.ui.productos.Seleccion_Producto
import com.squareup.picasso.Picasso


class ProductosAdaptador2(val datos: ArrayList<Productos_object2>, val context:Context) : RecyclerView.Adapter<ProductosAdaptador2.ViewHolder>() {


    lateinit var list: MutableList<Productos_object2>
    lateinit var listFiltered: MutableList<Productos_object2>

    override fun getItemCount() = datos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.nombre.text = datos.get(position).name1
        Picasso.with(context)
                .load( datos.get(position).icons1 )
                .error( R.drawable.ic_launcher_background ) //en caso que la url no sea válida muestro otra imagen
                .into( holder.imagen );

        holder.nombre2.text = datos.get(position).name2
        Picasso.with(context)
                .load( datos.get(position).icons2 )
                .error( R.drawable.ic_launcher_background ) //en caso que la url no sea válida muestro otra imagen
                .into( holder.imagen2 );

        holder.contenedor.setOnClickListener {

            //se consulta el servicio
            var queue = Volley.newRequestQueue(context)
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
                    params["id"] = datos.get(position).id1.toString()
                    return params
                }
            }
            queue.add(postRequest)

            //Toast.makeText(context, datos.get(position).id.toString(),Toast.LENGTH_LONG).show() //Dirige a Descricion Producto
            val s = Seleccion()
            s.set_id_producto(datos.get(position).id1.toString().toInt())
            val intent = Intent(context, Seleccion_Producto::class.java)
            context.startActivity(intent);
        }
        holder.contenedor2.setOnClickListener {

            //se consulta el servicio
            var queue = Volley.newRequestQueue(context)
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
                    params["id"] = datos.get(position).id2.toString()
                    return params
                }
            }
            queue.add(postRequest)

            //Toast.makeText(context, datos.get(position).id.toString(),Toast.LENGTH_LONG).show() //Dirige a Descricion Producto
            val s = Seleccion()
            s.set_id_producto(datos.get(position).id2.toString().toInt())
            val intent = Intent(context, Seleccion_Producto::class.java)
            context.startActivity(intent);
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.item_productos, parent, false);
        return ViewHolder(view)
    }



    fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var charString: String = constraint.toString()
                if (charString.isEmpty()){
                    listFiltered = list
                }
                else{
                    var filteredList: MutableList<Productos_object2> = mutableListOf()
                    for (s: Productos_object2 in list){
                        if (s.name1.toString().toLowerCase().contains(charString.toLowerCase())){
                            filteredList.add(s)
                        }
                    }
                    listFiltered = filteredList
                }
                var filterResults: FilterResults = FilterResults()
                filterResults.values = listFiltered
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                listFiltered = results!!.values as MutableList<Productos_object2>
                notifyDataSetChanged()
            }

        }
    }
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val nombre: TextView = itemView.findViewById(R.id.NombreProducto_Producto)
        val imagen: ImageView = itemView.findViewById(R.id.Imageview_Producto_Producto)
        val contenedor: androidx.cardview.widget.CardView = itemView.findViewById(R.id.cardv1)

        val nombre2: TextView = itemView.findViewById(R.id.NombreProducto_Producto2)
        val imagen2: ImageView = itemView.findViewById(R.id.Imageview_Producto_Producto2)
        val contenedor2: androidx.cardview.widget.CardView = itemView.findViewById(R.id.cardv2)
    }
}
