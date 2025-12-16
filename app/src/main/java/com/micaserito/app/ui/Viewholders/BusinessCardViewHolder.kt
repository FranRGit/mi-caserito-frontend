package com.micaserito.app.ui.Viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.micaserito.app.R
import com.micaserito.app.data.model.ItemDetails

class BusinessCardViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val businessName: TextView = view.findViewById(R.id.tv_business_name)
    private val businessAddress: TextView = view.findViewById(R.id.tv_business_address)
    private val businessRating: TextView = view.findViewById(R.id.tv_business_rating)
    private val businessCategory: TextView = view.findViewById(R.id.tv_business_category)
    private val businessProfileImage: ImageView = view.findViewById(R.id.iv_business_profile)

    fun bind(item: ItemDetails) {
        businessName.text = item.nombreNegocio
        businessAddress.text = item.direccion
        businessRating.text = item.calificacionPromedio.toString()
        businessCategory.text = item.categoria

        Glide.with(itemView.context)
            .load(item.profileUrl)
            .placeholder(R.drawable.ic_user)
            .circleCrop()
            .into(businessProfileImage)
    }
}