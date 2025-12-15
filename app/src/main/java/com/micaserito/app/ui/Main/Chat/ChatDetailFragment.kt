package com.micaserito.app.ui.Main.Chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.micaserito.app.R
import kotlinx.coroutines.launch

class ChatDetailFragment : Fragment() {

    private val viewModel: ChatViewModel by viewModels { viewModelFactory }
    private lateinit var messageAdapter: MessageAdapter
    private var chatId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chatId = arguments?.getInt("chatId") ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (chatId == 0) return

        messageAdapter = MessageAdapter(viewModel.miIdActual)

        val rvMessages = view.findViewById<RecyclerView>(R.id.rvMessages)
        val etMessage = view.findViewById<EditText>(R.id.etMessage)
        val btnSend = view.findViewById<ImageButton>(R.id.btnSend)

        rvMessages.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
            adapter = messageAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.messagesState.collect { messagesList ->
                messageAdapter.submitList(messagesList)
                if (!messagesList.isEmpty()) {
                    rvMessages.scrollToPosition(0)
                }
            }
        }

        btnSend.setOnClickListener {
            val content = etMessage.text.toString().trim()
            if (content.isNotEmpty()) {
                viewModel.sendMessage(content) { success ->
                    if (success) {
                        etMessage.setText("")
                    }
                }
            }
        }

        viewModel.loadMessages(chatId.toString(), 1)
    }
}