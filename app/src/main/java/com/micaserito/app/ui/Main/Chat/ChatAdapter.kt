package com.micaserito.app.ui.main.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.micaserito.app.data.model.ChatPreview
import com.micaserito.app.databinding.ItemChatPreviewBinding

class ChatAdapter(
    private val onClick: (ChatPreview) -> Unit = {}
) : ListAdapter<ChatPreview, ChatAdapter.ChatVH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatVH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemChatPreviewBinding.inflate(inflater, parent, false)
        return ChatVH(binding)
    }

    override fun onBindViewHolder(holder: ChatVH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ChatVH(private val b: ItemChatPreviewBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: ChatPreview) {
            b.tvName.text = item.nombre_usuario
            b.tvLastMessage.text = item.ultimo_mensaje
            b.tvDate.text = item.fecha

            // Placeholder para imagen; opcion: Glide/Picasso si lo integras
            // Glide.with(b.ivProfile).load(item.foto_perfil).placeholder(R.drawable.ic_profile).into(b.ivProfile)

            if (item.no_leidos > 0) {
                b.tvUnreadBadge.visibility = View.VISIBLE
                b.tvUnreadBadge.text = item.no_leidos.toString()
            } else {
                b.tvUnreadBadge.visibility = View.GONE
            }

            b.root.setOnClickListener { onClick(item) }
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<ChatPreview>() {
            override fun areItemsTheSame(oldItem: ChatPreview, newItem: ChatPreview): Boolean =
                oldItem.id_chat == newItem.id_chat

            override fun areContentsTheSame(oldItem: ChatPreview, newItem: ChatPreview): Boolean =
                oldItem == newItem
        }
    }
}