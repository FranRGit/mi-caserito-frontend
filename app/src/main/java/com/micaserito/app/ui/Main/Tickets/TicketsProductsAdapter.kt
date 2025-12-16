package com.micaserito.app.ui.Main.Tickets

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.micaserito.app.R
import com.micaserito.app.data.model.TicketItem

class TicketProductsAdapter(
    private val items: MutableList<TicketItem>,
    private val isEditable: Boolean, // Si es false, ocultamos los botones +/-
    private val onQuantityChanged: (TicketItem, Int) -> Unit // Callback para avisar al Fragment
) : RecyclerView.Adapter<TicketProductsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Necesitas crear un layout item_ticket_product_detail.xml (ver abajo)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ticket_product_detail, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvProductName)
        val tvPrice: TextView = itemView.findViewById(R.id.tvProductPrice)
        val tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)
        val btnMinus: ImageButton = itemView.findViewById(R.id.btnMinus)
        val btnPlus: ImageButton = itemView.findViewById(R.id.btnPlus)

        fun bind(item: TicketItem) {
            tvName.text = item.nombre // Asegúrate que tu TicketItem tenga 'nombre'
            tvPrice.text = "S/. ${String.format("%.2f", item.precioUnitario)}"
            tvQuantity.text = item.cantidad.toString()

            if (isEditable) {
                btnMinus.visibility = View.VISIBLE
                btnPlus.visibility = View.VISIBLE

                btnMinus.setOnClickListener { onQuantityChanged(item, -1) }
                btnPlus.setOnClickListener { onQuantityChanged(item, 1) }
            } else {
                btnMinus.visibility = View.INVISIBLE
                btnPlus.visibility = View.INVISIBLE
            }
        }
    }

    fun updateData() {
        notifyDataSetChanged()
    }
}