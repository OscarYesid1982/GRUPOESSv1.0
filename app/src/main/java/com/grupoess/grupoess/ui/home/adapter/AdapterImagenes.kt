package com.grupoess.grupoess.ui.home.adapter
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.grupoess.grupoess.R
import kotlinx.android.synthetic.main.content_main_reciclehome.view.*



class AdapterImagenes(var list: AbstractList<Imagenes>):RecyclerView.Adapter<AdapterImagenes.ViewHolder>() {



    class ViewHolder(view:View): RecyclerView.ViewHolder(view){
        fun bindItems(data:Imagenes){
            val title:TextView=itemView.findViewById(R.id.fragmenteHome_text1)
            val count:TextView=itemView.findViewById(R.id.fragmenteHome_text2)
            val image:ImageView=itemView.findViewById(R.id.fragmenteHome_image1)

            title.text=data.name
            count.text=data.numOfSong.toString()
            Glide.with(itemView.context).load(data.thumbnail).into(image)

            itemView.setOnClickListener {
                Toast.makeText(itemView.context, "Album de ${data.name}",Toast.LENGTH_LONG).show()

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var vista=LayoutInflater.from(parent?.context).inflate(R.layout.content_main_reciclehome,parent,false)

        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: AdapterImagenes.ViewHolder, position: Int) {
        holder.bindItems(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}