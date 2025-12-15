package com.micaserito.app.ui.main.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.micaserito.app.data.api.MockData
import com.micaserito.app.data.model.ChatMessage
import com.micaserito.app.data.model.ChatSummary
import com.micaserito.app.data.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ChatViewModel(
    private val repository: ChatRepository,
    private val miIdActual: Int = MockData.getFakeSession().idUsuario
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
                val optimistic = ChatMessage(
                    idMensaje = (-System.currentTimeMillis()).toInt(),
                    idUsuario = miIdActual,
                    contenido = content,
                    fechaEnvio = "Ahora",
                    leido = false
                )
                _messagesState.value = _messagesState.value + optimistic

                repository.sendMessage(chatId, content).collect { returned ->
                    _messagesState.value = (_messagesState.value + returned).distinctBy { it.idMensaje }
                    onResult(true)
                }
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }

    fun getCurrentPage(): Int = currentPage
}