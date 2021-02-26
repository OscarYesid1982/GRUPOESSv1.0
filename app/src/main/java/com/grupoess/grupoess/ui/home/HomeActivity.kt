package com.grupoess.grupoess.ui.home

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.grupoess.grupoess.R
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_main.*

enum class ProviderType{
    BASIC,
    GOOGLE
}
class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //Setup
        val bundle: Bundle? = intent.extras
        val email: String? = bundle?.getString("email")
        val provider: String? = bundle?.getString("provider")
        setup(email?: "", provider?:"" )

        //Guardado de datos
        val prefs:SharedPreferences.Editor = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.putString("provider", provider)
        prefs.apply ()

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout_Home)
        val navView: NavigationView = findViewById(R.id.nav_view_Home)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home,R.id.nav_carrito,
            //R.id.nav_gallery, R.id.nav_slideshow
        ), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


    }

    private fun setup(email:String, provider: String) {
        //title = "Ventana Home"
        //homeEmail.text = email
        //homePassword.text = provider

        /*
        authBtnCerrarSesion.setOnClickListener {
            //Borrado de datos
            val prefs : SharedPreferences.Editor = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply ()

            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }

         */
    }


}