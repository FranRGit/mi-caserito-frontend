package com.micaserito.app.ui.Main.Chat

import androidx.lifecycle.ViewModelProvider
import com.micaserito.app.data.repository.MockChatRepository
import com.micaserito.app.data.model.ChatRepository
import com.micaserito.app.ui.Main.Chat.ChatViewModel

private val MOCK_REPO_INSTANCE = MockChatRepository()
private val CHAT_REPOSITORY: ChatRepository = MOCK_REPO_INSTANCE
private val CURRENT_USER_ID = MOCK_REPO_INSTANCE.getFakeUserId()

// Esta Factory es ahora accesible a ambos fragmentos.
val viewModelFactory = object : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatViewModel(CHAT_REPOSITORY, CURRENT_USER_ID) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}