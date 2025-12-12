package com.micaserito.app.ui.Main

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
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
            showActionMenu(it)
        }
    }

    private fun navigateTo(fragmentId: Int) {
        if (navController.currentDestination?.id != fragmentId) {
            navController.navigate(fragmentId)
        }
    }

    private fun showActionMenu(anchor: View) {
        val popup = PopupMenu(this, anchor)
        popup.menuInflater.inflate(R.menu.plus_button_menu, popup.menu)

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_security -> {
                    navigateTo(R.id.nav_security)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }
}