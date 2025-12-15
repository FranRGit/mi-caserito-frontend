package com.micaserito.app.ui.main.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.micaserito.app.data.model.ChatMessage
import com.micaserito.app.databinding.ItemMessageMeBinding
import com.micaserito.app.databinding.ItemMessageOtherBinding

class MessageAdapter(
    private val miIdActual: Int
) : ListAdapter<ChatMessage, RecyclerView.ViewHolder>(DIFF) {

    companion object {
        const val VIEW_TYPE_ME = 1
        const val VIEW_TYPE_OTHER = 2

        private val DIFF = object : DiffUtil.ItemCallback<ChatMessage>() {
            override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean =
                oldItem.idMensaje == newItem.idMensaje

            override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean =
                oldItem == newItem
        }
    }

    override fun getItemViewType(position: Int): Int {
        val msg = getItem(position)
        return if (msg.idUsuario == miIdActual) VIEW_TYPE_ME else VIEW_TYPE_OTHER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_ME) {
            val b = ItemMessageMeBinding.inflate(inflater, parent, false)
            MeVH(b)
        } else {
            val b = ItemMessageOtherBinding.inflate(inflater, parent, false)
            OtherVH(b)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = getItem(position)
        when (holder) {
            is MeVH -> holder.bind(msg)
            is OtherVH -> holder.bind(msg)
        }
    }

    class MeVH(private val b: ItemMessageMeBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(m: ChatMessage) {
            b.tvMessage.text = m.contenido
            b.tvTime.text = m.fechaEnvio
        }
    }

    class OtherVH(private val b: ItemMessageOtherBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(m: ChatMessage) {
            b.tvMessage.text = m.contenido
            b.tvTime.text = m.fechaEnvio
            // Avatar: usar Glide si quieres cargar imagen en b.ivAvatar
        }
    }
}