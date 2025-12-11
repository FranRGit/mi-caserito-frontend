package com.micaserito.app.ui.Home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.micaserito.app.R
import com.micaserito.app.data.model.FeedItem
import com.micaserito.app.ui.Viewholders.PostCardViewHolder
import com.micaserito.app.ui.Viewholders.ProductCardViewHolder

class HomeFeedAdapter(private val listaItems: List<FeedItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val TYPE_POST = 1
        const val TYPE_PRODUCT = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (listaItems[position].type == "post") TYPE_POST else TYPE_PRODUCT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return if (viewType == TYPE_POST) {
            val view = inflater.inflate(R.layout.item_post_card, parent, false)
            PostCardViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.item_product_card, parent, false)
            ProductCardViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = listaItems[position]
        if (holder is PostCardViewHolder) {
            holder.render(item.details)
        }
    }

    override fun getItemCount(): Int = listaItems.size
}