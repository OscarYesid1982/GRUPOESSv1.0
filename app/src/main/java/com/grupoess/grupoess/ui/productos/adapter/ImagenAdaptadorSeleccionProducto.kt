package com.example.grupoess.adapater

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.grupoess.grupoess.R
import com.grupoess.grupoess.ui.home.adapter.IntroSlide
import com.grupoess.grupoess.ui.productos.adapter.ImagenSeleccionProductoSlide
import com.squareup.picasso.Picasso

/*
class ImagenAdaptadorSeleccionProducto(val datos: ArrayList<Imagenes_SeleccionProducto>, val context:Context) : RecyclerView.Adapter<ImagenAdaptadorSeleccionProducto.ViewHolder>() {

    override fun getItemCount() = datos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //holder.nombre.text = datos.get(position).name
        Picasso.with(context)
            .load( datos.get(position).icons )
            .error( R.drawable.ic_launcher_background ) //en caso que la url no sea válida muestro otra imagen
            .into( holder.nombre )
        ;

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.item_imegen_seleccion_producto, parent, false);
        return ViewHolder(view)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val nombre: ImageView = itemView.findViewById(R.id.selecProduc_imagen)
        //val contenedor: androidx.cardview.widget.CardView = itemView.findViewById(R.id.item_imagen_seleccion_producto)
    }
}

 */

class ImagenAdaptadorSeleccionProducto (private val introSlides: List<ImagenSeleccionProductoSlide>,
                                        private val context: Context ):
        RecyclerView.Adapter<ImagenAdaptadorSeleccionProducto.IntroSlideViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntroSlideViewHolder {
        return  IntroSlideViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.item_imegen_seleccion_producto, parent, false)
        )
    }

    override fun onBindViewHolder(holder: IntroSlideViewHolder, position: Int) {
        holder.bind(introSlides[position])
    }

    override fun getItemCount(): Int {
        return introSlides.size
    }

    inner class IntroSlideViewHolder(view: View): RecyclerView.ViewHolder(view){

        private  val textTittle = view.findViewById<TextView>(R.id.textTittle)
        private  val textDescription = view.findViewById<TextView>(R.id.textDescription)
        private  val imageIcon = view.findViewById<ImageView>(R.id.selecProduc_imagen)

        fun bind (introSlide: ImagenSeleccionProductoSlide){

            Picasso.with(context)
                    .load(introSlide.icon)
            .error( R.drawable.ic_launcher_background ) //en caso que la url no sea válida muestro otra imagen
            .into(imageIcon)
            ;

        }
    }
}

