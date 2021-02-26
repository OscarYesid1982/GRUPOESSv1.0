package com.grupoess.grupoess.ui.productos.adapter

import android.content.Context
import android.view.View
import android.view.ViewConfiguration.get
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.grupoess.grupoess.R
import com.grupoess.grupoess.model.Productos_object
import com.squareup.picasso.Picasso

class LanguageAdaptersProductos(var context: Context, var arrayList: ArrayList<Productos_object>):BaseAdapter() {

    override fun getItem(position: Int): Any {
        return arrayList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return  position.toLong()
    }

    override fun getCount(): Int {
        return arrayList.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {


        var view: View = View.inflate(context, R.layout.card_view_productos, null)
        var icons: ImageView = view.findViewById(R.id.Imageview_Producto)
        var names: TextView = view.findViewById(R.id.NombreProducto)
        var descrip: TextView = view.findViewById(R.id.descripcionCorta)



        var listItem: Productos_object = arrayList.get(position)

        Picasso.with(context)
                .load( listItem.icons)
                .error( R.drawable.ic_launcher_background ) //en caso que la url no sea válida muestro otra imagen
                .into( icons);


        descrip.text = listItem.descrip
        names.text = listItem.name


        return view
    }
}