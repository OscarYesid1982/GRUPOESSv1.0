package com.example.grupoess.adapater

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.grupoess.grupoess.R
import com.grupoess.grupoess.model.Categorias_object
import com.grupoess.grupoess.model.Seleccion
import com.grupoess.grupoess.ui.productos.Productos
import com.squareup.picasso.Picasso


class CategoriaAdaptador(val datos: ArrayList<Categorias_object>, val context:Context) : RecyclerView.Adapter<CategoriaAdaptador.ViewHolder>() {

    override fun getItemCount() = datos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nombre.text = datos.get(position).name
        Picasso.with(context)
            .load( datos.get(position).icons )
            .error( R.drawable.ic_launcher_background ) //en caso que la url no sea válida muestro otra imagen
            .into( holder.imagen )

        holder.contenedor.setOnClickListener {
            //Log.i("Alert","Positions: "+ position)
            val s = Seleccion()
            s.set_id_categoria(datos.get(position).id.toString().toInt())

            val intent = Intent(context, Productos::class.java)
            context.startActivity(intent);

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.item_categorias, parent, false);
        return ViewHolder(view)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val nombre: TextView = itemView.findViewById(R.id.NombreProducto)
        val imagen: ImageView = itemView.findViewById(R.id.Imageview_Producto)
        val contenedor: androidx.cardview.widget.CardView = itemView.findViewById(R.id.item_linear)
    }
}
