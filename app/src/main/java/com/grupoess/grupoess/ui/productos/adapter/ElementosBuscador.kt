package com.grupoess.grupoess.ui.productos.adapter

class ElementosBuscador {

    private lateinit var sname: String

    constructor(sname: String) {
        this.setName(sname)
    }

    fun getSname(): String
    {
        return sname
    }

    fun setName(sname: String)
    {
        this.sname = sname
    }

}