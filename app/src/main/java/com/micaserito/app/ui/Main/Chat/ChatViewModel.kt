package com.micaserito.app.ui.Main.Chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.micaserito.app.data.model.ChatMessage
import com.micaserito.app.data.model.ChatSummary
import com.micaserito.app.data.model.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ChatViewModel(
    private val repository: ChatRepository,
    val miIdActual: Int
) : ViewModel() {

    private val _chatsState = MutableStateFlow<List<ChatSummary>>(emptyList())
    val chatsState: StateFlow<List<ChatSummary>> = _chatsState

    private val _messagesState = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messagesState: StateFlow<List<ChatMessage>> = _messagesState

    private var currentChatId: String? = null
    private var currentPage = 1
    private var isLoadingPage = false

    fun loadChats(filter: String? = null) {
        viewModelScope.launch {
            repository.getChats(filter).collect { list ->
                _chatsState.value = list
            }
        }
    }

    fun loadMessages(chatId: String, page: Int = 1) {
        if (isLoadingPage) return
        isLoadingPage = true
        currentChatId = chatId
        viewModelScope.launch {
            repository.getMessages(chatId, page).collect { nuevos ->
                val current = _messagesState.value
                _messagesState.value = if (page > 1) {
                    (nuevos + current).distinctBy { it.idMensaje }
                } else {
                    nuevos
                }
                currentPage = page
                isLoadingPage = false
            }
        }
    }

    fun sendMessage(content: String, onResult: (Boolean) -> Unit = {}) {
        val chatId = currentChatId ?: return onResult(false)
        viewModelScope.launch {
            try {
                // Mensaje optimista para actualización rápida de la UI
                val optimistic = ChatMessage(
                    idMensaje = (-System.currentTimeMillis()).toInt(),
                    idUsuario = miIdActual,
                    contenido = content,
                    fechaEnvio = "Ahora",
                    leido = false
                )
                _messagesState.value = _messagesState.value + optimistic

                repository.sendMessage(chatId, content).collect { returned ->
                    // Reemplazar el mensaje optimista con el mensaje confirmado del repo
                    _messagesState.value = _messagesState.value
                        .filter { it.idMensaje != optimistic.idMensaje } + returned
                    onResult(true)
                }
            } catch (e: Exception) {
                // Eliminar el mensaje optimista si falla el envío
                _messagesState.value = _messagesState.value.filter { it.idMensaje != (-System.currentTimeMillis()).toInt() }
                onResult(false)
            }
        }
    }

    fun getCurrentPage(): Int = currentPage
}