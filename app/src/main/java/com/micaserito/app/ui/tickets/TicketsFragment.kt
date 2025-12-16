package com.micaserito.app.ui.tickets

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
import com.micaserito.app.ui.Main.Tickets.TicketsAdapter

class TicketsFragment : Fragment(R.layout.fragment_tickets) {

    private lateinit var adapter: TicketsAdapter
    private lateinit var rvTickets: RecyclerView
    private lateinit var tvNoTickets: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Inicializar las vistas
        rvTickets = view.findViewById(R.id.rvTickets)
        tvNoTickets = view.findViewById(R.id.tvNoTickets)
        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)

        // 2. Configurar el RecyclerView
        adapter = TicketsAdapter()
        rvTickets.layoutManager = LinearLayoutManager(context)
        rvTickets.adapter = adapter

        // 3. Listeners
        btnBack.setOnClickListener { findNavController().popBackStack() }

        // 4. Cargar los datos
        loadTickets()
    }

    private fun loadTickets() {
        val tickets = MockData.getMyTickets()

        if (tickets.isEmpty()) {
            rvTickets.visibility = View.GONE
            tvNoTickets.visibility = View.VISIBLE
        } else {
            rvTickets.visibility = View.VISIBLE
            tvNoTickets.visibility = View.GONE
            adapter.clear()
            adapter.addList(tickets)
        }
    }
}