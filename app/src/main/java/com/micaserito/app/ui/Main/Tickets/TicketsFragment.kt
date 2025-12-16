package com.micaserito.app.ui.Main.Tickets

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.micaserito.app.R
import com.micaserito.app.data.api.MockData

class TicketsFragment : Fragment(R.layout.fragment_tickets) {

    private lateinit var adapter: TicketsAdapter
    private lateinit var rvTickets: RecyclerView
    private lateinit var tvNoTickets: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvTickets = view.findViewById(R.id.rvTickets)
        tvNoTickets = view.findViewById(R.id.tvNoTickets)
        rvTickets.layoutManager = LinearLayoutManager(context)

        // 1. Inicializamos el Adapter con la acción de click
        adapter = TicketsAdapter { ticketSeleccionado ->

            // A. Preparamos el ID para enviarlo
            val bundle = Bundle().apply {
                putInt("ticketId", ticketSeleccionado.idTicket)
            }

            // B. Navegamos usando la acción definida en tu mobile_navigation.xml
            findNavController().navigate(R.id.action_nav_tickets_to_nav_ticket_detail, bundle)
        }

        rvTickets.adapter = adapter

        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            // Esto hace que "vuelva a la normalidad" (retroceda)
            findNavController().popBackStack()
        }
    }

    override fun onResume() {
        super.onResume()
        loadTicketsFromMockData()
    }

    private fun loadTicketsFromMockData() {
        adapter.clear()

        // 1. Obtener el contexto de forma segura
        val ctx = context ?: return

        // 2. Usar tu SessionManager pasando el contexto
        val storedId = com.micaserito.app.data.Local.SessionManager.getUserId(ctx)
        val userId = if (storedId != 0) storedId else 1

        val userRole = com.micaserito.app.data.Local.SessionManager.getUserType(ctx)

        // 3. Llamar a MockData con los datos reales
        val tickets = MockData.getMyTickets(userId = userId, userRole = userRole)

        if (tickets.isEmpty()) {
            rvTickets.visibility = View.GONE
            tvNoTickets.visibility = View.VISIBLE
        } else {
            rvTickets.visibility = View.VISIBLE
            tvNoTickets.visibility = View.GONE
            adapter.addList(tickets)
        }
    }
}