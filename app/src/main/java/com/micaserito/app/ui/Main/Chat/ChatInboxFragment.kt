package com.micaserito.app.ui.main.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.micaserito.app.data.network.NetworkModule
import com.micaserito.app.data.repository.ChatRepositoryImpl
import com.micaserito.app.databinding.FragmentChatInboxBinding
import kotlinx.coroutines.launch
import androidx.lifecycle.Lifecycle

class ChatInboxFragment : Fragment() {

    private var _binding: FragmentChatInboxBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ChatViewModel
    private lateinit var adapter: ChatAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentChatInboxBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val repo = ChatRepositoryImpl(NetworkModule.apiService)
        val factory = object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ChatViewModel(repo) as T
            }
        }
        viewModel = ViewModelProvider(this, factory).get(ChatViewModel::class.java)

        adapter = ChatAdapter { preview ->
            // Navegar a detalle: implementar navegación en tu app
            // Ejemplo: findNavController().navigate(R.id.action_to_chatDetail, bundleOf("chatId" to preview.idChat.toString()))
        }
        binding.rvChats.layoutManager = LinearLayoutManager(requireContext())
        binding.rvChats.adapter = adapter

        binding.chipAll.setOnClickListener {
            binding.progress.isVisible = true
            viewModel.loadChats(null)
        }
        binding.chipUnread.setOnClickListener {
            binding.progress.isVisible = true
            viewModel.loadChats("unread")
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.chatsState.collect { list ->
                        adapter.submitList(list)
                        binding.tvEmpty.isVisible = list.isEmpty()
                        binding.progress.isVisible = false
                    }
                }
            }
        }

        // carga inicial
        binding.chipAll.isChecked = true
        viewModel.loadChats(null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}