package com.micaserito.app.ui.Main.Discover

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.micaserito.app.R
import com.micaserito.app.data.model.FeedItem
import com.micaserito.app.ui.Viewholders.BusinessCardViewHolder
import com.micaserito.app.ui.Viewholders.PostCardViewHolder
import com.micaserito.app.ui.Viewholders.ProductCardViewHolder

class DiscoverAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<FeedItem>()

    companion object {
        const val TYPE_PRODUCT = 1
        const val TYPE_POST = 2
        const val TYPE_BUSINESS = 3
    }

    fun addList(newItems: List<FeedItem>) {
        val startPosition = items.size
        items.addAll(newItems)
        notifyItemRangeInserted(startPosition, newItems.size)
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position].type) {
            "product" -> TYPE_PRODUCT
            "post" -> TYPE_POST
            "business" -> TYPE_BUSINESS
            else -> throw IllegalArgumentException("Tipo de item desconocido: ${items[position].type}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_PRODUCT -> {
                val view = inflater.inflate(R.layout.item_product_card, parent, false)
                ProductCardViewHolder(view)
            }
            TYPE_POST -> {
                val view = inflater.inflate(R.layout.item_post_card, parent, false)
                PostCardViewHolder(view)
            }
            TYPE_BUSINESS -> {
                val view = inflater.inflate(R.layout.item_feed_header, parent, false)
                BusinessCardViewHolder(view)
            }
            else -> throw IllegalArgumentException("Vista desconocida")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (holder) {
            is ProductCardViewHolder -> holder.render(item.details)
            is PostCardViewHolder -> holder.render(item.details)
            is BusinessCardViewHolder -> holder.bind(item.details)
        }
    }

    override fun getItemCount(): Int = items.size
}