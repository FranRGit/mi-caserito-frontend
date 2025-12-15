package com.micaserito.app.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.flow.Flow

//=============================================================================
// 1. MODELOS DE DATOS (ChatSummary, ChatMessage, etc.)
//=============================================================================

data class ChatSummary(
    @SerializedName("id_chat") val idChat: Int,
    @SerializedName("nombre") val nombreParticipante: String,
    @SerializedName("profile_url") val profileUrl: String?,
    @SerializedName("ultimo_mensaje") val ultimoMensaje: String,
    @SerializedName("mensajes_no_leidos") val mensajesNoLeidos: Int,
    @SerializedName("fecha_envio") val fechaUltimoMensaje: String
)

data class ChatMessage(
    @SerializedName("id_mensaje") val idMensaje: Int,
    @SerializedName("id_usuario") val idUsuario: Int,
    @SerializedName("contenido") val contenido: String,
    @SerializedName("fecha_envio") val fechaEnvio: String,
    @SerializedName("leido") val leido: Boolean
)

data class SendMessageRequest(
    @SerializedName("contenido") val contenido: String
)

data class ChatHistoryResponse(
    @SerializedName("data") val data: List<ChatMessage>,
    @SerializedName("pagination") val pagination: Any
)


//=============================================================================
// 2. CONTRATO DE REPOSITORIO (ChatRepository Interface)
//=============================================================================

interface ChatRepository {
    fun getChats(filter: String?): Flow<List<ChatSummary>>
    fun getMessages(chatId: String, page: Int): Flow<List<ChatMessage>>
    fun sendMessage(chatId: String, content: String): Flow<ChatMessage>
}