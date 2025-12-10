package com.micaserito.app.data.model

import com.google.gson.annotations.SerializedName

// --- TICKETS (Mis Compras) ---
data class TicketSummary(
    @SerializedName("id_ticket") val idTicket: Int,
    @SerializedName("codigo_ticket") val codigoTicket: String,
    @SerializedName("titulo") val titulo: String, // Nombre del Negocio
    @SerializedName("total") val total: Double,
    @SerializedName("estado") val estado: String, // "pendiente", "confirmado"...
    @SerializedName("fecha") val fecha: String
)

data class ReportSummary(
    @SerializedName("id_reporte") val idReporte: Int,
    @SerializedName("tipo_reporte") val tipoReporte: String,
    @SerializedName("titulo") val titulo: String, // Motivo
    @SerializedName("usuario_relacionado") val usuarioRelacionado: String, // Contra quien/Quien reportó
    @SerializedName("estado") val estado: String,
    @SerializedName("fecha") val fecha: String
)