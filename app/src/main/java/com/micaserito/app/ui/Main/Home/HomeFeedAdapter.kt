package com.micaserito.app.ui.Main.Home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.micaserito.app.R
import com.micaserito.app.data.model.FeedItem
import com.micaserito.app.ui.Viewholders.PostCardViewHolder
import com.micaserito.app.ui.Viewholders.ProductCardViewHolder

// ACEPTAMOS LA ARQUITECTURA MODERNA: ListAdapter y DiffUtil
class HomeFeedAdapter : ListAdapter<FeedItem, RecyclerView.ViewHolder>(FeedItemDiffCallback()) {

    // Constantes usadas para el SpanSizeLookup en HomeFragment
    companion object {
        const val TYPE_POST = 1
        const val TYPE_PRODUCT = 2
        const val TYPE_BUSINESS = 3
        const val TYPE_HEADER = 4 // <<-- NUEVA CONSTANTE AÑADIDA
    }

    // 1. Determina el ViewType usando el campo 'type' del modelo
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).type) {
            "product" -> TYPE_PRODUCT
            "post" -> TYPE_POST
            "business" -> TYPE_BUSINESS
            "header" -> TYPE_HEADER // <<-- NUEVA LÓGICA DE TIPO DE VISTA
            else -> TYPE_POST
        }
    }

    // 2. Inflar el diseño CORRECTO y crear el ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            TYPE_PRODUCT -> {
                val view = inflater.inflate(R.layout.item_product_card, parent, false)
                ProductCardViewHolder(view)
            }
            // Posts y Business usan la tarjeta social
            TYPE_POST, TYPE_BUSINESS -> {
                val view = inflater.inflate(R.layout.item_post_card, parent, false)
                PostCardViewHolder(view)
            }
            TYPE_HEADER -> { // <<-- MANEJO DEL HEADER DE SECCIÓN
                val view = inflater.inflate(R.layout.item_feed_header, parent, false)
                // Usamos un ViewHolder anónimo simple, ya que no necesita lógica de binding compleja
                object : RecyclerView.ViewHolder(view) {}
            }
            else -> {
                val view = inflater.inflate(R.layout.item_post_card, parent, false)
                PostCardViewHolder(view)
            }
        }
    }

    // 3. Pasar los datos al ViewHolder usando el método 'render'
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        when (holder) {
            is PostCardViewHolder -> item.details.let { holder.render(it) }
            is ProductCardViewHolder -> item.details.let { holder.render(it) }
            // Los Headers no requieren lógica de binding en este ejemplo
        }
    }
}

// Clase necesaria para el manejo eficiente de la lista (DiffUtil)
class FeedItemDiffCallback : DiffUtil.ItemCallback<FeedItem>() {

    override fun areItemsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {

        // Si son de tipos diferentes (ej. producto vs header), no son el mismo ítem
        if (oldItem.type != newItem.type) return false

        // Para tipos que no son el Header, usamos el ID
        return when (oldItem.type) {
            "product" -> oldItem.details.idProducto == newItem.details.idProducto
            "post" -> oldItem.details.idPost == newItem.details.idPost
            "business" -> oldItem.details.idNegocio == newItem.details.idNegocio

            // Si el tipo es 'header', asumimos que solo habrá uno (o que se comparan por índice/título si fuera dinámico)
            "header" -> true // Tratamos los headers como idénticos si son del mismo tipo
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
        // La comparación por data class es suficiente para el contenido.
        return oldItem == newItem
    }
}