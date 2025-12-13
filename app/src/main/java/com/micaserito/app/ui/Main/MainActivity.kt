package com.micaserito.app.ui.Main

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
// Eliminamos: import androidx.appcompat.widget.PopupMenu // Ya no usamos PopupMenu
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.micaserito.app.R
// NUEVOS IMPORTS
import com.micaserito.app.data.api.MockData // Para simular la sesión
import com.micaserito.app.ui.Extra.Menu.ActionMenuBottomSheet // El menú flotante (Ruta corregida)

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var tvTitle: TextView

    // 1. Simulación del tipo de usuario (Vendedor o Comprador)
    private val currentUserType = MockData.getFakeSession().tipoUsuario

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
            // Llama a la nueva función de menú Bottom Sheet
            showActionMenu()
        }
    }

    fun navigateTo(fragmentId: Int) {
        if (navController.currentDestination?.id != fragmentId) {
            navController.navigate(fragmentId)
        }
    }

    // El método ahora no requiere el parámetro 'anchor'
    private fun showActionMenu() {
        // 1. Determinamos si el usuario es vendedor
        val isSeller = currentUserType == "vendedor"

        // 2. Usamos el BottomSheet y le pasamos el estado
        val bottomSheet = ActionMenuBottomSheet.newInstance(isSeller)
        bottomSheet.show(supportFragmentManager, ActionMenuBottomSheet.TAG)
    }


}