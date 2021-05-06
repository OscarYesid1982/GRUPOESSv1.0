package com.grupoess.grupoess

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.grupoess.grupoess.ui.carrito.Carrito
import com.grupoess.grupoess.ui.login.Datos_Usuario
import com.grupoess.grupoess.ui.login.LoginActivity
import com.grupoess.grupoess.ui.login.RegistroActivity
import com.grupoess.grupoess.ui.productos.Historico_Compras_Activity
import com.grupoess.grupoess.ui.productos.Productos
import dmax.dialog.SpotsDialog

var mDialog: android.app.AlertDialog? = null

@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {
    private val tiempo: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mDialog = SpotsDialog.Builder()
            .setContext(this)
            .setMessage("Espere un momento")
            .setCancelable(false).build()


        Handler().postDelayed({
            mDialog?.show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, tiempo)
    }


}