package com.micaserito.app.ui.Main.Chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.micaserito.app.R
import com.micaserito.app.data.model.ChatSummary

class ChatAdapter(
    private val onChatClicked: (ChatSummary) -> Unit
) : ListAdapter<ChatSummary, ChatAdapter.ChatViewHolder>(ChatDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_preview, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = getItem(position)
        holder.bind(chat)
        holder.itemView.setOnClickListener {
            onChatClicked(chat)
        }
    }

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // IDs asumidos de item_chat_preview.xml
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val tvLastMessage: TextView = itemView.findViewById(R.id.tvLastMessage)
        private val tvUnreadBadge: TextView = itemView.findViewById(R.id.tvUnreadBadge)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)

        fun bind(chat: ChatSummary) {
            tvName.text = chat.nombreParticipante
            tvLastMessage.text = chat.ultimoMensaje
            tvDate.text = chat.fechaUltimoMensaje

            if (chat.mensajesNoLeidos > 0) {
                tvUnreadBadge.text = chat.mensajesNoLeidos.toString()
                tvUnreadBadge.visibility = View.VISIBLE
            } else {
                tvUnreadBadge.visibility = View.GONE
            }
        }
    }
}

class ChatDiffCallback : DiffUtil.ItemCallback<ChatSummary>() {

    override fun areItemsTheSame(oldItem: ChatSummary, newItem: ChatSummary): Boolean {
        return oldItem.idChat == newItem.idChat
    }

    override fun areContentsTheSame(oldItem: ChatSummary, newItem: ChatSummary): Boolean {
        return oldItem == newItem
    }
}