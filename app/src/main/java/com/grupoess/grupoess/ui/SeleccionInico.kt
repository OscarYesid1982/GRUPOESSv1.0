package com.grupoess.grupoess.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.grupoess.grupoess.MainActivity
import com.grupoess.grupoess.R
import com.grupoess.grupoess.SplashActivity
import com.grupoess.grupoess.mDialog
import com.grupoess.grupoess.model.User
import com.grupoess.grupoess.ui.home.HomeActivity
import com.grupoess.grupoess.ui.login.LoginActivity
import com.grupoess.grupoess.ui.login.RegistroActivity
import com.grupoess.grupoess.ui.productos.Historico_Compras_Activity
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_seleccion_inico.*
import org.json.JSONArray
import org.json.JSONObject

class SeleccionInico : AppCompatActivity() {

    private val tiempo: Long = 5000

    var mDialog: android.app.AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccion_inico)

        mDialog = SpotsDialog.Builder()
            .setContext(this)
            .setMessage("Espere un momento")
            .setCancelable(false).build()

            boton_ir_home.setOnClickListener {
                mDialog?.show()
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
                finish()
            }
            boton_ir_hist_compras.setOnClickListener {
                mDialog?.show()
                val i = Intent(this, Historico_Compras_Activity::class.java)
                startActivity(i)
                finish()
            }
        Handler().postDelayed({
            //com.grupoess.grupoess.mDialog?.show()
            mDialog?.show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, tiempo)
    }
}