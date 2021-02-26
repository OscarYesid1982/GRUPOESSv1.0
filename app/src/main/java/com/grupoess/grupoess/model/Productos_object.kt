package com.grupoess.grupoess.model

class Productos_object {
    var icons: String ? = ""
    var name: String ? = null
    var id: Int ? = 0
    var descrip: String ? =""
    var precio: String? = ""
    var categoria: String? = ""
    var cantidad: String? = ""

    constructor(icons: String?, name: String?, id: Int?, descrip: String?, precio :String?, categoria: String?, cantidad: String?) {
        this.icons = icons
        this.name = name
        this.id = id
        this.descrip = descrip
        this.precio = precio
        this.categoria = categoria
        this.cantidad = cantidad
    }
}

