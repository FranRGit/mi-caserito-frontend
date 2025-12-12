package com.micaserito.app.ui.Main.Tickets

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.micaserito.app.R
import com.micaserito.app.data.model.TicketSummary

class TicketsAdapter : RecyclerView.Adapter<TicketsAdapter.TicketViewHolder>() {

    private val tickets = mutableListOf<TicketSummary>()

    fun addList(newTickets: List<TicketSummary>) {
        val start = tickets.size
        tickets.addAll(newTickets)
        notifyItemRangeInserted(start, newTickets.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        // Asegúrate de crear un layout simple llamado item_ticket.xml
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_card, parent, false)
        // NOTA: Estoy usando item_product_card temporalmente, lo ideal es crear item_ticket.xml
        return TicketViewHolder(view)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        holder.bind(tickets[position])
    }

    override fun getItemCount(): Int = tickets.size

    class TicketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Ajusta estos IDs a tu layout real
        private val txtBusinessName: TextView = itemView.findViewById(R.id.tv_product_name) // Reutilizando ID
        private val txtTotal: TextView = itemView.findViewById(R.id.tv_product_price) // Reutilizando ID

        fun bind(ticket: TicketSummary) {
            // Requisito UI: El título es el nombre del negocio
            txtBusinessName.text = ticket.titulo
            txtTotal.text = "Total: S/ ${ticket.total}"
        }
    }
}
