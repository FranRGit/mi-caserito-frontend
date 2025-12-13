package com.micaserito.app.ui.Main.Tickets

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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

    fun clear() {
        tickets.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ticket, parent, false)
        return TicketViewHolder(view)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        holder.bind(tickets[position])
    }

    override fun getItemCount(): Int = tickets.size

    class TicketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCode: TextView = itemView.findViewById(R.id.tvTicketCode)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTicketTitle)
        private val tvTotal: TextView = itemView.findViewById(R.id.tvTicketTotal)
        private val tvStatus: TextView = itemView.findViewById(R.id.tvTicketStatus)
        private val tvDate: TextView = itemView.findViewById(R.id.tvTicketDate)

        fun bind(ticket: TicketSummary) {
            tvCode.text = ticket.codigoTicket
            tvTitle.text = ticket.titulo
            tvTotal.text = "Total: S/ ${String.format("%.2f", ticket.total)}"
            tvDate.text = ticket.fecha
            tvStatus.text = ticket.estado

            // --- Lógica para Colores de Estado ---
            val statusColor = when (ticket.estado.lowercase()) {
                "negociando" -> "#8E8E93" // Gris
                "en proceso" -> "#FF9500" // Naranja
                "completado" -> "#34C759" // Verde
                "anulado" -> "#FF3B30"    // Rojo
                "reportado" -> "#FF3B30"    // Rojo
                else -> "#8E8E93"
            }

            val color = Color.parseColor(statusColor)
            tvStatus.setTextColor(color)

            val drawable = tvStatus.background as GradientDrawable
            drawable.setStroke(3, color) // 3 es el ancho del borde en píxeles
        }
    }
}