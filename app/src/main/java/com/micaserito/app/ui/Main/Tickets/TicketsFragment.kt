package com.micaserito.app.ui.Main.Tickets

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.micaserito.app.R
import com.micaserito.app.data.model.TicketSummary

class TicketsFragment : Fragment(R.layout.fragment_tickets) {

    private lateinit var adapter: TicketsAdapter
    private var currentPage = 1
    private var isLoading = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvTickets = view.findViewById<RecyclerView>(R.id.rvTickets)
        val layoutManager = LinearLayoutManager(context)
        rvTickets.layoutManager = layoutManager
        adapter = TicketsAdapter()
        rvTickets.adapter = adapter

        // Scroll Infinito
        rvTickets.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(rv, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisiblePos = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && (visibleItemCount + firstVisiblePos) >= totalItemCount && firstVisiblePos >= 0) {
                    currentPage++
                    loadTickets()
                }
            }
        })

        loadTickets()
    }

    private fun loadTickets() {
        isLoading = true
        view?.findViewById<View>(R.id.progressBarTickets)?.visibility = View.VISIBLE

        // SIMULACIÓN DE API (Borrar al conectar)
        android.os.Handler().postDelayed({
            isLoading = false
            view?.findViewById<View>(R.id.progressBarTickets)?.visibility = View.GONE

            // Datos de ejemplo para probar que la lista funciona
            val mockTickets = listOf(
                TicketSummary(1, "T-001", "Pardos Chicken", 55.0, "Entregado", "2024-05-20"),
                TicketSummary(2, "T-002", "Tanta", 89.5, "En camino", "2024-05-21"),
                TicketSummary(3, "T-003", "Starbucks", 25.0, "Pendiente", "2024-05-21")
            )
            adapter.addList(mockTickets)

        }, 1500)
    }
}