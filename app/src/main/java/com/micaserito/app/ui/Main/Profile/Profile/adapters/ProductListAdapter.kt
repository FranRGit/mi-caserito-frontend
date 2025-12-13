package com.micaserito.app.ui.Main.Profile.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.micaserito.app.R
import com.micaserito.app.data.model.ItemDetails
import com.micaserito.app.ui.Viewholders.ProductCardViewHolder

class ProductListAdapter(
    private var products: List<ItemDetails>
) : RecyclerView.Adapter<ProductCardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductCardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_card, parent, false)
        return ProductCardViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductCardViewHolder, position: Int) {
        holder.render(products[position])
    }

    override fun getItemCount(): Int = products.size

    fun updateData(newProducts: List<ItemDetails>) {
        products = newProducts
        notifyDataSetChanged()
    }
}