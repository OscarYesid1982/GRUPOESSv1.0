package com.grupoess.grupoess.model

class Categorias_object {
    var icons: String ? = ""
    var name: String ? = null
    var id: Int ? = 0
    var distinguir: Int ? = 0

    constructor(icons: String?, name: String?, id: Int?, distinguir: Int?) {
        this.icons = icons
        this.name = name
        this.id = id
        this.distinguir = distinguir
    }
}

