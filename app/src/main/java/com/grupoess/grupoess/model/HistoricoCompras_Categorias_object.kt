package com.grupoess.grupoess.model

class HistoricoCompras_Categorias_object {
    var idUser: Int ? = 0
    var idProducto: String ? = ""
    var cantidad: String ? = null
    var estCompra: String ? = null
    var idCompra: Int ? = 0
    var id: Int ? = 0
    var fechaActualizacion: String ? = null
    var idWordpress: String ? = null
    var name: String ? = null
    var descripcion: String ? = null
    var idCategoria: String ? = null
    var icon: String ? = null
    var precion: String ? = null

    constructor(idUser: Int?, idProducto: String?, cantidad: String?, estCompra: String?, idCompra: Int?, id: Int?, fechaActualizacion: String?, idWordpress: String?, name: String?, descripcion: String?, idCategoria: String?, icon: String?, precion: String?) {
        this.idUser = idUser
        this.idProducto = idProducto
        this.cantidad = cantidad
        this.estCompra = estCompra
        this.idCompra = idCompra
        this.id = id
        this.fechaActualizacion = fechaActualizacion
        this.idWordpress = idWordpress
        this.name = name
        this.descripcion = descripcion
        this.idCategoria = idCategoria
        this.icon = icon
        this.precion = precion
    }
}

