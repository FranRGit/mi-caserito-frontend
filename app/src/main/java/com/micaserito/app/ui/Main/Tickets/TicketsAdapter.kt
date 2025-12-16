package com.micaserito.app.ui.Main.Tickets

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.micaserito.app.R
import com.micaserito.app.data.model.TicketStatus // <--- IMPORTANTE: Importar el Enum
import com.micaserito.app.data.model.TicketSummary

// 1. Agregamos 'onTicketClick' al constructor para manejar la navegación después
class TicketsAdapter(
    private val onTicketClick: (TicketSummary) -> Unit
) : RecyclerView.Adapter<TicketsAdapter.TicketViewHolder>() {

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
        val item = tickets[position]
        holder.bind(item)

        // 2. Activamos el Click en toda la tarjeta
        holder.itemView.setOnClickListener {
            onTicketClick(item)
        }
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

            // UI: Quitamos guiones bajos si vienen del Enum (Ej: EN_PROCESO -> En proceso)
            tvStatus.text = ticket.estado.replace("_", " ")

            // 3. LÓGICA DE COLORES BASADA EN EL ENUM
            // Convertimos el texto a Mayúsculas para que coincida con TicketStatus.CONSTANTE.name
            val estadoUpper = ticket.estado.uppercase()

            val statusColor = when (estadoUpper) {
                TicketStatus.PENDIENTE.name -> "#8E8E93"    // Gris (Enviado)
                TicketStatus.NEGOCIANDO.name -> "#8E8E93"   // Gris (Enviado)
                TicketStatus.EN_PROCESO.name -> "#FFCC00"   // Amarillo (Seleccionando)
                TicketStatus.COMPLETADO.name -> "#34C759"   // Verde (Listo)
                TicketStatus.ANULADO.name -> "#FF3B30"      // Rojo
                TicketStatus.REPORTADO.name -> "#FF3B30"    // Rojo
                else -> "#8E8E93" // Default Gris
            }

            // Aplicar colores
            val color = Color.parseColor(statusColor)
            tvStatus.setTextColor(color)

            // Cambiar color del borde (stroke)
            val drawable = tvStatus.background as GradientDrawable
            drawable.setStroke(3, color)
        }
    }
}