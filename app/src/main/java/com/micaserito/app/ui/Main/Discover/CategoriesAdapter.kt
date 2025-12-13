package com.micaserito.app.ui.Main.Discover

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.micaserito.app.R

class CategoriesAdapter(
    private val categories: List<String>,
    private val onCategoryClick: (String) -> Unit // Callback para notificar el clic
) : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category_circle, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val categoryName = categories[position]
        holder.bind(categoryName)
        holder.itemView.setOnClickListener {
            onCategoryClick(categoryName)
        }
    }

    override fun getItemCount(): Int = categories.size

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCategoryName: TextView = itemView.findViewById(R.id.tvCategoryName)
        private val ivCategoryIcon: ImageView = itemView.findViewById(R.id.ivCategoryIcon)

        fun bind(categoryName: String) {
            tvCategoryName.text = categoryName
            
            // Lógica para asignar un ícono diferente a cada categoría
            val iconRes = when (categoryName.lowercase()) {
                "alimentos" -> android.R.drawable.ic_menu_myplaces
                "ropa" -> android.R.drawable.ic_menu_myplaces
                "campo" -> android.R.drawable.ic_menu_myplaces
                "belleza" -> android.R.drawable.ic_menu_myplaces
                "hogar" -> android.R.drawable.ic_menu_myplaces
                "tecnología" -> android.R.drawable.ic_menu_myplaces
                else -> android.R.drawable.ic_menu_gallery
            }
            ivCategoryIcon.setImageResource(iconRes)
        }
    }
}