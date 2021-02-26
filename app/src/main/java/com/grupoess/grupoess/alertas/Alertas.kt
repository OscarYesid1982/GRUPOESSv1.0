package com.grupoess.grupoess.alertas

import android.content.Context
import androidx.appcompat.app.AlertDialog

class Alertas {
    fun mensaje(titulo: String?, mensaje: String?, boton: String?, main: Context?) {
        val builder = AlertDialog.Builder(
            main!!
        )
        builder.setTitle(titulo)
        builder.setMessage(mensaje)
        builder.setPositiveButton(boton, null)
        val dialog = builder.create()
        dialog.show()
    }
}