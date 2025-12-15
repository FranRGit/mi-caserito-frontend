package com.micaserito.app.ui.main.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.micaserito.app.data.api.MockData
import com.micaserito.app.data.model.ChatPreview
import com.micaserito.app.data.model.Message
import com.micaserito.app.data.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel(
    private val repository: ChatRepository,
    private val miIdActual: Int = MockData.getFakeSession().idUsuario
) : ViewModel() {

    private val _chatsState = MutableStateFlow<List<ChatPreview>>(emptyList())
    val chatsState: StateFlow<List<ChatPreview>> = _chatsState

    private val _messagesState = MutableStateFlow<List<Message>>(emptyList())
    val messagesState: StateFlow<List<Message>> = _messagesState

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

    /**
     * page 1 -> últimos mensajes (reemplaza).
     * page >1 -> mensajes más antiguos (se prepende al inicio).
     */
    fun loadMessages(chatId: String, page: Int = 1) {
        if (isLoadingPage) return
        isLoadingPage = true
        currentChatId = chatId
        viewModelScope.launch {
            repository.getMessages(chatId, page).collect { nuevos ->
                val current = _messagesState.value
                _messagesState.value = if (page > 1) {
                    // prepend antiguos, evitar duplicados
                    (nuevos + current).distinctBy { it.id_mensaje }
                } else {
                    nuevos
                }
                currentPage = page
                isLoadingPage = false
            }
        }
    }

    /**
     * Envío optimista: añade mensaje localmente y luego recoge la respuesta del repo.
     */
    fun sendMessage(content: String, onResult: (Boolean) -> Unit = {}) {
        val chatId = currentChatId ?: return onResult(false)
        viewModelScope.launch {
            try {
                val optimistic = Message(
                    id_mensaje = (-System.currentTimeMillis()).toInt(),
                    id_chat = chatId.toIntOrNull() ?: 0,
                    id_emisor = miIdActual,
                    contenido = content,
                    fecha_envio = "Ahora"
                )
                _messagesState.value = _messagesState.value + optimistic

                repository.sendMessage(chatId, content).collect { returned ->
                    // Añadir respuesta real evitando duplicados por id
                    _messagesState.value = (_messagesState.value + returned).distinctBy { it.id_mensaje }
                    onResult(true)
                }
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }

    fun getCurrentPage(): Int = currentPage
}