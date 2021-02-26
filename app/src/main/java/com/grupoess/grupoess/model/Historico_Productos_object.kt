package com.grupoess.grupoess.model

class Historico_Productos_object {
    var fecha: String ? = ""
    var id: Int ? = 0
    var cant_productos: String ? =""
    var valor_total: String? = ""

    constructor(fecha: String?, id: Int?, cant_productos: String?, valor_total: String?) {
        this.fecha = fecha
        this.id = id
        this.cant_productos = cant_productos
        this.valor_total = valor_total
    }
}

