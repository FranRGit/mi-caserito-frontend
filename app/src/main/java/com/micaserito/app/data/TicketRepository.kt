package com.micaserito.app.data

import com.micaserito.app.data.model.TicketSummary

/**
 * Un objeto Singleton que actúa como una base de datos en memoria para los tickets.
 * Esto nos permite simular la creación y persistencia de tickets entre diferentes pantallas.
 */
object TicketRepository {

    val generatedTickets = mutableListOf<TicketSummary>()

    fun addTicket(ticket: TicketSummary) {
        generatedTickets.add(0, ticket) // Añade el ticket al principio de la lista
    }
}
