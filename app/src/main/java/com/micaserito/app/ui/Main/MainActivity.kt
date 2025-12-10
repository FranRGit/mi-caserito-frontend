package com.micaserito.app.ui.Main

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.micaserito.app.R

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var tvTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        tvTitle = findViewById(R.id.tvScreenTitle)
        val btnHome = findViewById<ImageButton>(R.id.btnHome)
        val btnDiscover = findViewById<ImageButton>(R.id.btnDiscover)
        val btnPlus = findViewById<ImageButton>(R.id.btnPlus)
        val btnChat = findViewById<ImageButton>(R.id.btnChat)
        val btnProfile = findViewById<ImageButton>(R.id.btnProfile)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            tvTitle.text = destination.label ?: "Mi Caserito"
        }

        btnHome.setOnClickListener { navigateTo(R.id.nav_home) }
        btnDiscover.setOnClickListener { navigateTo(R.id.nav_discover) }
        btnChat.setOnClickListener { navigateTo(R.id.nav_chat) }
        btnProfile.setOnClickListener { navigateTo(R.id.nav_profile) }

        btnPlus.setOnClickListener {
            showActionMenu()
        }
    }

    private fun navigateTo(fragmentId: Int) {
        // Solo navega si no estamos ya en esa pantalla
        if (navController.currentDestination?.id != fragmentId) {
            navController.navigate(fragmentId)
        }
    }

    private fun showActionMenu() {
        // Mensaje simple para probar que funciona
        Toast.makeText(this, "Abrir Menú: Reportes / Tickets", Toast.LENGTH_SHORT).show()

    }
}