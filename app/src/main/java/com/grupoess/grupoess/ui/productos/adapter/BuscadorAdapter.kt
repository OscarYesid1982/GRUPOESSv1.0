package com.grupoess.grupoess.ui.productos.adapter

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.grupoess.grupoess.R
import com.grupoess.grupoess.model.Categorias_object
import com.grupoess.grupoess.model.Productos_object2

class BuscadorAdapter: RecyclerView.Adapter<BuscadorAdapter.Companion.Holder>, Filterable {

    lateinit var list: MutableList<Productos_object2>
    lateinit var con: Context
    lateinit var rv:View
    lateinit var listFiltered: MutableList<Productos_object2>

    constructor(list: MutableList<Productos_object2>, con: Context) : super() {
        this.list = list
        this.con = con
        this.listFiltered = list
    }

    companion object {
        class Holder : RecyclerView.ViewHolder {

            lateinit var NombreProducto: TextView
            lateinit var descripcionCorta: TextView
            lateinit var Imageview_Producto: ImageView



            constructor(rv: View) : super(rv){
                NombreProducto = rv.findViewById(R.id.NombreProducto) as TextView
                descripcionCorta = rv.findViewById(R.id.descripcionCorta) as TextView
                Imageview_Producto = rv.findViewById(R.id.Imageview_Producto) as ImageView
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var holder: Holder
        rv = LayoutInflater.from(parent!!.context).inflate(R.layout.card_view_productos,parent,false)
        holder= Holder(rv)
        return holder
    }

    override fun getItemCount(): Int {
        return listFiltered.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        var st: Productos_object2
        st = listFiltered.get(position)
        holder!!.NombreProducto.setText(st.name1)
        var id: Int = position + 1


        rv.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                Toast.makeText(con,holder.NombreProducto.text.toString(), Toast.LENGTH_SHORT).show()
            }

        })
    }

    @Override
    public override fun getFilter(): Filter {
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
                var filterResults:FilterResults = FilterResults()
                filterResults.values = listFiltered
                return filterResults
            }
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                listFiltered = results!!.values as MutableList<Productos_object2>
                notifyDataSetChanged()
            }
        }
    }
}

