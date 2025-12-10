package com.micaserito.app.data.model

import com.google.gson.annotations.SerializedName

// Item para la lista de "Bandeja de Entrada"
data class ChatSummary(
    @SerializedName("id_chat") val idChat: Int,
    @SerializedName("nombre") val nombreParticipante: String,
    @SerializedName("profile_url") val profileUrl: String?,
    @SerializedName("ultimo_mensaje") val ultimoMensaje: String,
    @SerializedName("mensajes_no_leidos") val mensajesNoLeidos: Int,
    @SerializedName("fecha_envio") val fechaUltimoMensaje: String
)

// Respuesta al entrar a un chat
data class ChatHistoryResponse(
    @SerializedName("data") val data: List<ChatMessage>,
    @SerializedName("pagination") val pagination: PaginationMeta
)

// Un mensaje individual (burbuja)
data class ChatMessage(
    @SerializedName("id_mensaje") val idMensaje: Int,
    @SerializedName("id_usuario") val idUsuario: Int, // ID del que envió (para saber si ponerlo a la derecha/izquierda)
    @SerializedName("contenido") val contenido: String,
    @SerializedName("fecha_envio") val fechaEnvio: String,
    @SerializedName("leido") val leido: Boolean
)

// Para enviar un mensaje nuevo
data class SendMessageRequest(
    @SerializedName("contenido") val contenido: String
)