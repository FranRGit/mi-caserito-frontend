package com.micaserito.app.ui.Main

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.micaserito.app.R
import com.micaserito.app.data.Local.SessionManager
import com.micaserito.app.ui.Extra.Menu.ActionMenuBottomSheet

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var tvTitle: TextView
    // Variable para el menú inferior
    private lateinit var bottomNavCard: CardView

    private val currentUserType by lazy {
        SessionManager.getUserType(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        tvTitle = findViewById(R.id.tvScreenTitle)

        // 1. REFERENCIAMOS TU CARDVIEW (El menú flotante)
        bottomNavCard = findViewById(R.id.bottomNavCard)

        val btnHome = findViewById<ImageButton>(R.id.btnHome)
        val btnDiscover = findViewById<ImageButton>(R.id.btnDiscover)
        val btnPlus = findViewById<ImageButton>(R.id.btnPlus)
        val btnChat = findViewById<ImageButton>(R.id.btnChat)
        val btnProfile = findViewById<ImageButton>(R.id.btnProfile)

        // 2. EL LISTENER MÁGICO
        navController.addOnDestinationChangedListener { _, destination, _ ->
            navController.addOnDestinationChangedListener { _, destination, _ ->
                tvTitle.text = destination.label ?: "Mi Caserito"

                when (destination.id) {
                    // CASO 1: DETALLE DEL TICKET (Pantalla limpia)
                    R.id.nav_ticket_detail -> {
                        bottomNavCard.visibility = View.GONE  // Ocultamos menú de abajo
                        tvTitle.visibility = View.GONE        // Ocultamos Título Principal
                    }

                    // CASO 2: LISTA DE TICKETS (Mis Compras)
                    R.id.nav_tickets -> {
                        tvTitle.visibility = View.GONE          // Ocultamos Título Principal (¡Importante!)
                    }

                    // CASO 3: CUALQUIER OTRA PANTALLA (Home, Perfil, etc.)
                    else -> {
                        bottomNavCard.visibility = View.VISIBLE // Mostramos menú
                        tvTitle.visibility = View.VISIBLE       // Mostramos Título Principal
                    }
                }
            }
        }

        // Navegación
        btnHome.setOnClickListener { navigateTo(R.id.nav_home) }
        btnDiscover.setOnClickListener { navigateTo(R.id.nav_discover) }
        btnChat.setOnClickListener { navigateTo(R.id.nav_chat) }
        btnProfile.setOnClickListener { navigateTo(R.id.nav_profile) }

        btnPlus.setOnClickListener {
            showActionMenu()
        }
    }

    fun navigateTo(fragmentId: Int) {
        if (navController.currentDestination?.id != fragmentId) {
            navController.navigate(fragmentId)
        }
    }

    private fun showActionMenu() {
        val isSeller = currentUserType == "vendedor"
        val bottomSheet = ActionMenuBottomSheet.newInstance(isSeller)
        bottomSheet.show(supportFragmentManager, ActionMenuBottomSheet.TAG)
    }
}