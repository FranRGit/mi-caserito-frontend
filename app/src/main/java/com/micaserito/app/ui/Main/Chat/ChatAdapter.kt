package com.micaserito.app.ui.main.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.micaserito.app.data.model.ChatSummary
import com.micaserito.app.databinding.ItemChatPreviewBinding

class ChatAdapter(
    private val onClick: (ChatSummary) -> Unit = {}
) : ListAdapter<ChatSummary, ChatAdapter.ChatVH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatVH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemChatPreviewBinding.inflate(inflater, parent, false)
        return ChatVH(binding)
    }

    override fun onBindViewHolder(holder: ChatVH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ChatVH(private val b: ItemChatPreviewBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: ChatSummary) {
            b.tvName.text = item.nombreParticipante
            b.tvLastMessage.text = item.ultimoMensaje
            b.tvDate.text = item.fechaUltimoMensaje

            // Placeholder para imagen; opcion: Glide/Picasso si lo integras
            // Glide.with(b.ivProfile).load(item.profileUrl).placeholder(R.drawable.ic_profile).into(b.ivProfile)

            if (item.mensajesNoLeidos > 0) {
                b.tvUnreadBadge.visibility = View.VISIBLE
                b.tvUnreadBadge.text = item.mensajesNoLeidos.toString()
            } else {
                b.tvUnreadBadge.visibility = View.GONE
            }

            b.root.setOnClickListener { onClick(item) }
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<ChatSummary>() {
            override fun areItemsTheSame(oldItem: ChatSummary, newItem: ChatSummary): Boolean =
                oldItem.idChat == newItem.idChat

            override fun areContentsTheSame(oldItem: ChatSummary, newItem: ChatSummary): Boolean =
                oldItem == newItem
        }
    }
}