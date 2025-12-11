package com.micaserito.app.ui.Main.Profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.micaserito.app.R
import com.micaserito.app.ui.Auth.AuthActivity
import com.micaserito.app.ui.Extra.Security.SecurityActivity
import com.micaserito.app.ui.Extra.Ticket.TicketActivity

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Configurar el ViewPager y TabLayout (Las Pestañas)
        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)

        // Conectar el adaptador que creamos
        val adapter = ProfileStateAdapter(this)
        viewPager.adapter = adapter

        // Sincronizar las pestañas con el contenido
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Publicaciones"
                1 -> tab.text = "Catálogo"
            }
        }.attach()

        // 2. Configurar Datos del Usuario (Simulado por ahora)
        val tvName = view.findViewById<TextView>(R.id.tvProfileName)
        tvName.text = "Bryan Developer" // Tu nombre aquí

        // 3. Configurar el Botón de Ajustes (Para cumplir con Tickets y Seguridad)
        // NOTA: Este ID 'ivSettings' debe existir en profile_header.xml (Paso 3)
        val btnSettings = view.findViewById<ImageView>(R.id.ivSettings)

        btnSettings.setOnClickListener {
            showPopupMenu(it)
        }
    }

    // Función para mostrar el menú flotante con tus opciones requeridas
    private fun showPopupMenu(view: View) {
        val popup = PopupMenu(requireContext(), view)
        // Agregamos las opciones directamente en código para no crear más XMLs por ahora
        popup.menu.add(0, 1, 0, "Mis Tickets / Pedidos")
        popup.menu.add(0, 2, 0, "Seguridad y Privacidad")
        popup.menu.add(0, 3, 0, "Cerrar Sesión")

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                1 -> { // Ir a Tickets
                    startActivity(Intent(requireContext(), TicketActivity::class.java))
                    true
                }
                2 -> { // Ir a Seguridad
                    startActivity(Intent(requireContext(), SecurityActivity::class.java))
                    true
                }
                3 -> { // Cerrar Sesión
                    val intent = Intent(requireContext(), AuthActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }
}