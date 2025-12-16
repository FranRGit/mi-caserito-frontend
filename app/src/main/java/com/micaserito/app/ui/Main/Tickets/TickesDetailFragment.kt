package com.micaserito.app.ui.Main.Tickets

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.micaserito.app.R
import com.micaserito.app.data.Local.SessionManager
import com.micaserito.app.data.api.MockData
import com.micaserito.app.data.model.TicketStatus
import java.util.Calendar

class TicketDetailFragment : Fragment(R.layout.fragment_ticket_detail) {

    private var ticketId: Int = -1
    private lateinit var adapter: TicketProductsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { ticketId = it.getInt("ticketId", -1) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. CARGAR DATOS
        val ticket = MockData.getTicketById(ticketId)
        if (ticket == null) {
            findNavController().popBackStack()
            return
        }

        val userRole = SessionManager.getUserType(requireContext())
        val isVendor = userRole == "vendedor"

        // 2. REFERENCIAS UI
        val tvCode = view.findViewById<TextView>(R.id.tvTicketCodeDetail)
        val tvTitleScreen = view.findViewById<TextView>(R.id.tvTitleScreen)
        val btnBack = view.findViewById<View>(R.id.btnBackDetail)
        val btnPrimary = view.findViewById<MaterialButton>(R.id.btnPrimary)
        val btnSecondary = view.findViewById<MaterialButton>(R.id.btnSecondary)

        // Layouts
        val layoutList = view.findViewById<View>(R.id.layoutProductList)
        val layoutForm = view.findViewById<View>(R.id.layoutNegotiationForm)
        val layoutDelivery = view.findViewById<View>(R.id.layoutDeliveryInfo) // Nuevo

        // Elementos de la pantalla de Entrega
        val tvStatusBadge = view.findViewById<TextView>(R.id.tvStatusBadge)
        val tvDeliveryPointInfo = view.findViewById<TextView>(R.id.tvDeliveryPointInfo)
        val tvDeliveryDateInfo = view.findViewById<TextView>(R.id.tvDeliveryDateInfo)
        val tvVerificationCode = view.findViewById<TextView>(R.id.tvVerificationCode)
        val tvTotalInfo = view.findViewById<TextView>(R.id.tvTotalInfo)
        val btnViewDetailsInfo = view.findViewById<View>(R.id.btnViewDetailsInfo)

        // Elementos Lista y Formulario
        val tvTotalList = view.findViewById<TextView>(R.id.tvTotalDetail)
        val rvProducts = view.findViewById<RecyclerView>(R.id.rvProductsDetail)
        val etLocation = view.findViewById<EditText>(R.id.etDeliveryPoint)
        val tvDate = view.findViewById<TextView>(R.id.tvDeliveryDate)
        val tvTotalNeg = view.findViewById<TextView>(R.id.tvTotalNegotiation)
        val btnViewDetails = view.findViewById<View>(R.id.btnViewDetails)

        tvCode.text = "#${ticket.codigoTicket}"
        btnBack.setOnClickListener { findNavController().popBackStack() }

        // 3. LÓGICA DE ESTADOS
        when (ticket.estado) {
            TicketStatus.PENDIENTE -> {
                showLayout(layoutList, layoutForm, layoutDelivery)
                val isEditable = !isVendor
                setupAdapter(rvProducts, ticket, isEditable, tvTotalList)

                if (isVendor) {
                    btnPrimary.visibility = View.GONE
                    btnSecondary.visibility = View.GONE
                } else {
                    btnPrimary.text = "Solicitar"
                    btnSecondary.text = "Eliminar"
                    btnPrimary.setOnClickListener {
                        if (MockData.submitTicket(ticketId)) {
                            Toast.makeText(context, "Solicitado", Toast.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                        }
                    }
                    btnSecondary.setOnClickListener {
                        MockData.deleteTicket(ticketId)
                        findNavController().popBackStack()
                    }
                }
            }

            TicketStatus.NEGOCIANDO -> {
                if (isVendor) {
                    showLayout(layoutForm, layoutList, layoutDelivery)
                    tvTotalNeg.text = "S/. ${String.format("%.2f", ticket.total)}"
                    btnPrimary.text = "Aprobar"
                    btnSecondary.text = "Rechazar"

                    tvDate.setOnClickListener { showDatePicker(tvDate) }

                    // Ver detalles desde formulario
                    btnViewDetails.setOnClickListener {
                        layoutForm.visibility = View.GONE
                        layoutList.visibility = View.VISIBLE
                        setupAdapter(rvProducts, ticket, false, tvTotalList)
                        btnPrimary.visibility = View.GONE
                        btnSecondary.text = "Volver"
                        btnSecondary.setOnClickListener {
                            findNavController().popBackStack() // Recargar pantalla simple
                            findNavController().navigate(R.id.nav_ticket_detail, Bundle().apply { putInt("ticketId", ticketId) })
                        }
                    }

                    btnPrimary.setOnClickListener {
                        val lugar = etLocation.text.toString()
                        val fecha = tvDate.text.toString()
                        if (lugar.isNotEmpty() && fecha.isNotEmpty()) {
                            MockData.approveTicket(ticketId, lugar, fecha)
                            findNavController().popBackStack()
                        }
                    }
                    btnSecondary.setOnClickListener {
                        MockData.rejectTicket(ticketId)
                        findNavController().popBackStack()
                    }

                } else {
                    // Cliente esperando
                    showLayout(layoutList, layoutForm, layoutDelivery)
                    setupAdapter(rvProducts, ticket, false, tvTotalList)
                    btnPrimary.visibility = View.GONE
                    btnSecondary.text = "Cancelar Pedido"
                    btnSecondary.setOnClickListener {
                        MockData.rejectTicket(ticketId)
                        findNavController().popBackStack()
                    }
                }
            }

            TicketStatus.EN_PROCESO -> {
                showLayout(layoutDelivery, layoutList, layoutForm)
                fillDeliveryInfo(ticket, tvDeliveryPointInfo, tvDeliveryDateInfo, tvTotalInfo, tvVerificationCode)

                // Configurar Badge Amarillo
                setupStatusBadge(tvStatusBadge, TicketStatus.EN_PROCESO)

                btnPrimary.text = "Continuar"
                btnSecondary.text = "Reportar"

                // Lógica CONTINUAR (Ingresar Código)
                btnPrimary.setOnClickListener {
                    showVerificationDialog(ticket)
                }

                btnSecondary.setOnClickListener {
                    MockData.rejectTicket(ticketId)
                    findNavController().popBackStack()
                }

                // Ver detalles desde Info
                btnViewDetailsInfo.setOnClickListener {
                    layoutDelivery.visibility = View.GONE
                    layoutList.visibility = View.VISIBLE
                    setupAdapter(rvProducts, ticket, false, tvTotalList)
                    btnPrimary.visibility = View.GONE
                    btnSecondary.text = "Volver"
                    btnSecondary.setOnClickListener {
                        // Recargar para simplificar
                        findNavController().popBackStack()
                        findNavController().navigate(R.id.nav_ticket_detail, Bundle().apply { putInt("ticketId", ticketId) })
                    }
                }
            }

            TicketStatus.COMPLETADO -> {
                showLayout(layoutDelivery, layoutList, layoutForm)
                fillDeliveryInfo(ticket, tvDeliveryPointInfo, tvDeliveryDateInfo, tvTotalInfo, tvVerificationCode)

                // Cambio visual para compra exitosa
                tvTitleScreen.text = "¡Compra Exitosa!"

                // Badge Verde
                setupStatusBadge(tvStatusBadge, TicketStatus.COMPLETADO)

                // Ocultar botones o dejar uno de cerrar
                btnPrimary.visibility = View.GONE
                btnSecondary.text = "Cerrar"
                btnSecondary.setOnClickListener { findNavController().popBackStack() }

                btnViewDetailsInfo.setOnClickListener { /* Lógica similar a arriba si deseas ver items */ }
            }

            else -> { /* Anulado, etc */ }
        }
    }

    // --- UTILS ---

    private fun showLayout(show: View, hide1: View, hide2: View) {
        show.visibility = View.VISIBLE
        hide1.visibility = View.GONE
        hide2.visibility = View.GONE
    }

    private fun fillDeliveryInfo(ticket: com.micaserito.app.data.model.Ticket, tvPoint: TextView, tvDate: TextView, tvTotal: TextView, tvCode: TextView) {
        tvPoint.text = ticket.puntoEntrega ?: "Por definir"
        tvDate.text = ticket.fechaEntrega ?: "Por definir"
        tvTotal.text = "S/. ${String.format("%.2f", ticket.total)}"
        tvCode.text = "Código:\n${ticket.codigoVerificacion ?: "12345"}"
    }

    private fun setupStatusBadge(textView: TextView, status: TicketStatus) {
        textView.text = status.name.lowercase().replaceFirstChar { it.uppercase() }.replace("_", " ")

        // COLORES SEGÚN TU MAPA
        val colorHex = when (status) {
            TicketStatus.PENDIENTE, TicketStatus.NEGOCIANDO -> "#8E8E93" // Gris
            TicketStatus.EN_PROCESO -> "#FFCC00"  // Amarillo
            TicketStatus.COMPLETADO -> "#34C759"  // Verde
            TicketStatus.ANULADO, TicketStatus.REPORTADO -> "#FF3B30" // Rojo
        }

        val color = Color.parseColor(colorHex)
        textView.setTextColor(color)

        // Cambiar color del borde dinámicamente
        val background = textView.background as GradientDrawable
        background.setStroke(3, color) // 3px de ancho
    }

    private fun showVerificationDialog(ticket: com.micaserito.app.data.model.Ticket) {
        val input = EditText(requireContext())
        input.hint = "Ingresa el código (ej: 12345)"
        input.setPadding(50, 30, 50, 30)

        AlertDialog.Builder(requireContext())
            .setTitle("Confirmar Entrega")
            .setMessage("Ingresa el código que tiene la otra persona para finalizar.")
            .setView(input)
            .setPositiveButton("Confirmar") { _, _ ->
                val code = input.text.toString()
                if (MockData.completeTicket(ticketId, code)) {
                    Toast.makeText(context, "¡Transacción Completada!", Toast.LENGTH_LONG).show()
                    // Recargar pantalla
                    findNavController().popBackStack()
                    findNavController().navigate(R.id.nav_ticket_detail, Bundle().apply { putInt("ticketId", ticketId) })
                } else {
                    Toast.makeText(context, "Código incorrecto", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun setupAdapter(rv: RecyclerView, ticket: com.micaserito.app.data.model.Ticket, isEditable: Boolean, tvTotal: TextView) {
        tvTotal.text = "S/. ${String.format("%.2f", ticket.total)}"
        adapter = TicketProductsAdapter(ticket.items.toMutableList(), isEditable) { item, delta ->
            val newTotal = MockData.updateTicketItemQuantity(ticketId, item.idProducto, delta)
            tvTotal.text = "S/. ${String.format("%.2f", newTotal)}"
            adapter.notifyDataSetChanged()
        }
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = adapter
    }

    private fun showDatePicker(textView: TextView) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(requireContext(), { _, year, month, day ->
            textView.text = "$day/${month + 1}/$year 18:00"
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }
}