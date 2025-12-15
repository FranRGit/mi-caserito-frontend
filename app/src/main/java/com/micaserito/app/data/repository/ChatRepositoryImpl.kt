package com.micaserito.app.data.repository

import com.micaserito.app.data.api.ApiService
import com.micaserito.app.data.model.ChatMessage
import com.micaserito.app.data.model.ChatSummary
import com.micaserito.app.data.model.SendMessageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ChatRepositoryImpl(private val apiService: ApiService) : ChatRepository {

    override fun getChats(filter: String?): Flow<List<ChatSummary>> = flow {
        val response = apiService.getChats(filter ?: "all")
        if (response.isSuccessful) {
            emit(response.body() ?: emptyList())
        } else {
            // Manejar el error, por ahora emitimos lista vacía
            emit(emptyList())
        }
    }.flowOn(Dispatchers.IO)

    override fun getMessages(chatId: String, page: Int): Flow<List<ChatMessage>> = flow {
        val response = apiService.getChatMessages(chatId.toInt(), page)
        if (response.isSuccessful) {
            emit(response.body()?.data ?: emptyList())
        } else {
            emit(emptyList())
        }
    }.flowOn(Dispatchers.IO)

    override fun sendMessage(chatId: String, content: String): Flow<ChatMessage> = flow {
        val request = SendMessageRequest(content)
        val response = apiService.sendMessage(chatId.toInt(), request)
        if (response.isSuccessful && response.body() != null) {
            emit(response.body()!!)
        } else {
            // Manejar error, podrías lanzar una excepción
            throw Exception("Error al enviar mensaje")
        }
    }.flowOn(Dispatchers.IO)
}