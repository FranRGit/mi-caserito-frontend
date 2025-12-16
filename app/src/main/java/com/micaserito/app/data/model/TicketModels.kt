package com.micaserito.app.data.model

import com.google.gson.annotations.SerializedName

// Estados del Ticket (Enum para evitar errores de tipeo)
enum class TicketStatus {
    PENDIENTE,      // (Local) Usuario seleccionando productos
    NEGOCIANDO,     // (Enviado) Vendedor debe poner fecha/lugar - Cliente debe aceptar
    EN_PROCESO,     // Cliente aceptó -> Se genera QR/Código
    COMPLETADO,     // Código validado -> Venta cerrada
    ANULADO,        // Rechazado por cliente o vendedor
    REPORTADO       // Cliente reportó un problema
}

data class Ticket(
    val idTicket: Int,
    val codigoTicket: String, // Ej: "TCK-82391"
    val idCliente: Int,
    val idNegocio: Int,
    val nombreNegocio: String, // Para mostrar fácil en la lista
    var items: List<TicketItem>,
    var total: Double,
    var estado: TicketStatus,

    // Datos que se llenan en etapa NEGOCIANDO
    var puntoEntrega: String? = null,
    var fechaEntrega: String? = null,

    // Datos que se generan en etapa EN_PROCESO
    var codigoVerificacion: String? = null, // El código secreto (ej: "65465")
    val qrUrl: String? = null
)

data class TicketItem(
    val idProducto: Int,
    val nombre: String,
    val precioUnitario: Double,
    var cantidad: Int
) {
    val subtotal: Double get() = precioUnitario * cantidad
}