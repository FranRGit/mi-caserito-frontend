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
        adapter = TicketsAdapter()
        rvTickets.adapter = adapter

        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener { findNavController().popBackStack() }
    }

    override fun onResume() {
        super.onResume()
        loadTicketsFromMockData()
    }

    private fun loadTicketsFromMockData() {
        adapter.clear()
        // --- LLAMADA A MOCKDATA ---
        val tickets = MockData.getMyTickets()
        
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
