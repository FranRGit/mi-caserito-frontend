package com.micaserito.app.data.repository

import com.micaserito.app.data.model.ChatMessage
import com.micaserito.app.data.model.ChatSummary
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getChats(filter: String?): Flow<List<ChatSummary>>
    fun getMessages(chatId: String, page: Int): Flow<List<ChatMessage>>
    fun sendMessage(chatId: String, content: String): Flow<ChatMessage>
}