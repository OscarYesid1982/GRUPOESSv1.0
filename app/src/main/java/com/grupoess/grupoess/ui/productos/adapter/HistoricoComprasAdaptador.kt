package com.grupoess.grupoess.ui.productos.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.recyclerview.widget.RecyclerView
import com.grupoess.grupoess.R
import com.grupoess.grupoess.model.Historico_Productos_object
import com.grupoess.grupoess.model.Seleccion
import com.grupoess.grupoess.ui.carrito.Carrito
import com.grupoess.grupoess.ui.carrito.Historico_Carrito
import com.grupoess.grupoess.ui.productos.Historico_Compras_Activity
import com.grupoess.grupoess.ui.productos.Seleccion_Producto
import com.squareup.picasso.Picasso


class HistoricoComprasAdaptador(val datos: ArrayList<Historico_Productos_object>, val context: Context)
    : RecyclerView.Adapter<HistoricoComprasAdaptador.ViewHolder>()  {

    override fun getItemCount() = datos.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.fecha.text = datos.get(position).fecha
        holder.cantidad.text = datos.get(position).cant_productos
        holder.valor.text = datos.get(position).valor_total
        holder.contenedor.setOnClickListener {

            val s = Seleccion()
            s.set_id_producto(datos.get(position).id.toString().toInt())

            Toast.makeText(context,s._id_producto.toString(),Toast.LENGTH_SHORT).show()

            val intent = Intent(context, Historico_Carrito::class.java)
            context.startActivity(intent);
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.item_historico_compras, parent, false);
        return ViewHolder(view)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val fecha: TextView = itemView.findViewById(R.id.item_hist_fechaCompra)
        val cantidad: TextView = itemView.findViewById(R.id.item_hist_cantProd)
        val valor: TextView = itemView.findViewById(R.id.item_hist_valorCompra)
        val contenedor: androidx.cardview.widget.CardView = itemView.findViewById(R.id.item_linear_Historico_Compras)
    }


}
