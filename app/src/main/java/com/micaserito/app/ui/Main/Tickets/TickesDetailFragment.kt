package com.micaserito.app.ui.Main.Tickets

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.micaserito.app.R
import com.micaserito.app.data.api.MockData
import com.micaserito.app.data.model.TicketStatus

class TicketDetailFragment : Fragment(R.layout.fragment_ticket_detail) { // Crea este XML basado en tus imágenes

    private var ticketId: Int = -1
    private lateinit var tvTotal: TextView
    private lateinit var adapter: TicketProductsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Recuperar ID pasado por argumentos (SafeArgs o Bundle)
        arguments?.let {
            ticketId = it.getInt("ticketId", -1)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Retroceder
        val btnBack = view.findViewById<View>(R.id.btnBackDetail)
        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // Cargar Ticket
        val ticket = MockData.getTicketById(ticketId)
        if (ticket == null) {
            Toast.makeText(context, "Ticket no encontrado", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        // 1. UI References
        val tvCode = view.findViewById<TextView>(R.id.tvTicketCodeDetail)
        tvTotal = view.findViewById(R.id.tvTotalDetail)
        val rvProducts = view.findViewById<RecyclerView>(R.id.rvProductsDetail)
        val btnPrimary = view.findViewById<MaterialButton>(R.id.btnPrimary) // "Solicitar" o "Aprobar"
        val btnSecondary = view.findViewById<MaterialButton>(R.id.btnSecondary) // "Eliminar" o "Rechazar"

        // 2. Render Basic Info
        tvCode.text = "#${ticket.codigoTicket}"
        tvTotal.text = "S/. ${String.format("%.2f", ticket.total)}"

        // 3. Determinar si es editable (Solo si es PENDIENTE)
        val isEditable = ticket.estado == TicketStatus.PENDIENTE

        // 4. Configurar Adapter
        adapter = TicketProductsAdapter(ticket.items.toMutableList(), isEditable) { item, delta ->
            // Callback: Al cambiar cantidad
            val newTotal = MockData.updateTicketItemQuantity(ticketId, item.idProducto, delta)
            tvTotal.text = "S/. ${String.format("%.2f", newTotal)}"
            adapter.notifyDataSetChanged() // Refrescar UI (números)
        }
        rvProducts.layoutManager = LinearLayoutManager(context)
        rvProducts.adapter = adapter

        // 5. Configurar Botones Según Estado (LÓGICA DINÁMICA)
        if (ticket.estado == TicketStatus.PENDIENTE) {
            // --- MODO BORRADOR ---
            btnPrimary.text = "Solicitar"
            btnSecondary.text = "Eliminar"

            btnPrimary.setOnClickListener {
                if (MockData.submitTicket(ticketId)) {
                    Toast.makeText(context, "¡Solicitud enviada al vendedor!", Toast.LENGTH_LONG).show()
                    findNavController().popBackStack() // Volver a lista (ahora saldrá gris/negociando)
                }
            }

            btnSecondary.setOnClickListener {
                MockData.deleteTicket(ticketId)
                findNavController().popBackStack()
            }

        } else if (ticket.estado == TicketStatus.NEGOCIANDO) {
            btnPrimary.visibility = View.GONE // O "Contactar"
            btnSecondary.text = "Cancelar Pedido"

        }

        // ... Lógica para otros estados (EN_PROCESO, etc) ...
    }
}