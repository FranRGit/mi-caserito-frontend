package com.micaserito.app.ui.Main.Chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.micaserito.app.R
import com.micaserito.app.data.repository.MockChatRepository
import com.micaserito.app.data.model.ChatSummary
import com.micaserito.app.ui.Main.Chat.ChatViewModel
import kotlinx.coroutines.launch
import androidx.navigation.fragment.findNavController
class ChatFragment : Fragment() {
    private val viewModel: ChatViewModel by viewModels { viewModelFactory }
    private lateinit var chatsAdapter: ChatAdapter

    private val onChatClickListener: (ChatSummary) -> Unit = { chat ->
        navigateToChatDetail(chat.idChat)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatsAdapter = ChatAdapter(onChatClickListener)

        val rvChats = view.findViewById<RecyclerView>(R.id.rvChats)
        val tvEmpty = view.findViewById<TextView>(R.id.tvEmpty)

        rvChats.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatsAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.chatsState.collect { chatsList ->
                chatsAdapter.submitList(chatsList)
                tvEmpty.visibility = if (chatsList.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        // Carga inicial
        viewModel.loadChats()
    }

    private fun navigateToChatDetail(chatId: Int) {
        val action = ChatFragmentDirections.actionNavChatToNavChatDetail(chatId)
        findNavController().navigate(action)
    }
}