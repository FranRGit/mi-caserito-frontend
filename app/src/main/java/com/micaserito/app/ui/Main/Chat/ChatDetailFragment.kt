package com.micaserito.app.ui.main.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.micaserito.app.data.api.MockData
import com.micaserito.app.data.repository.ChatRepositoryImpl
import com.micaserito.app.databinding.FragmentChatDetailBinding
import kotlinx.coroutines.launch
import androidx.lifecycle.Lifecycle

class ChatDetailFragment : Fragment() {

    private var _binding: FragmentChatDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ChatViewModel
    private lateinit var adapter: MessageAdapter

    private val miIdActual = MockData.getFakeSession().idUsuario
    private val chatIdArg: String?
        get() = arguments?.getString("chatId") ?: "1"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentChatDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val repo = ChatRepositoryImpl()
        val factory = object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ChatViewModel(repo, miIdActual) as T
            }
        }
        viewModel = ViewModelProvider(this, factory).get(ChatViewModel::class.java)

        val lm = LinearLayoutManager(requireContext())
        lm.stackFromEnd = true
        binding.rvMessages.layoutManager = lm

        adapter = MessageAdapter(miIdActual)
        binding.rvMessages.adapter = adapter

        binding.rvMessages.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(rv, dx, dy)
                val firstPos = lm.findFirstCompletelyVisibleItemPosition()
                if (firstPos <= 2) {
                    val nextPage = viewModel.getCurrentPage() + 1
                    viewModel.loadMessages(chatIdArg ?: "1", nextPage)
                }
            }
        })

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.messagesState.collect { list ->
                        val wasAtBottom = isAtBottom()
                        adapter.submitList(list) {
                            if (wasAtBottom) binding.rvMessages.scrollToPosition(adapter.itemCount - 1)
                        }
                    }
                }
            }
        }

        viewModel.loadMessages(chatIdArg ?: "1", 1)

        binding.btnSend.setOnClickListener {
            val text = binding.etMessage.text.toString().trim()
            if (text.isNotEmpty()) {
                binding.etMessage.setText("")
                viewModel.sendMessage(text) { /* optional feedback */ }
            }
        }
    }

    private fun isAtBottom(): Boolean {
        val lm = binding.rvMessages.layoutManager as? LinearLayoutManager ?: return true
        val last = lm.findLastCompletelyVisibleItemPosition()
        return last >= adapter.itemCount - 2
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}