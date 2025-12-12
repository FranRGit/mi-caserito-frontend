// ui/Main/Home/CategoryAdapter.kt (MODIFICAR)

package com.micaserito.app.ui.Main.Home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.micaserito.app.R
import com.micaserito.app.data.model.CategoriaNegocio // Importación del modelo confirmado

// Definimos la interfaz del Listener
typealias OnCategoryClickListener = (category: CategoriaNegocio) -> Unit

// ViewHolder para la tarjeta de categoría
class CategoryViewHolder(itemView: View, private val onClick: OnCategoryClickListener) : RecyclerView.ViewHolder(itemView) {
    private val ivIcon: ImageView = itemView.findViewById(R.id.iv_category_icon)
    private val tvName: TextView = itemView.findViewById(R.id.tv_category_name)

    fun bind(category: CategoriaNegocio) {
        tvName.text = category.nombre

        // --- LÓGICA DE ICONOS (Temporal) ---
        when (category.id) {
            1 -> ivIcon.setImageResource(R.drawable.ic_shopping)
            2 -> ivIcon.setImageResource(R.drawable.ic_user)
            3 -> ivIcon.setImageResource(R.drawable.ic_view)
            else -> ivIcon.setImageResource(R.drawable.ic_more)
        }

        // ** AÑADIMOS EL CLICK LISTENER AL ÍTEM **
        // El clic en el ítem completo (LinearLayout) dispara el callback
        itemView.setOnClickListener {
            onClick(category)
        }
    }
}

// Adapter para la lista horizontal
class CategoryAdapter(
    private val categories: List<CategoriaNegocio>,
    private val categoryClickListener: OnCategoryClickListener // <-- RECIBIMOS EL LISTENER
) : RecyclerView.Adapter<CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_chip, parent, false)
        // PASAMOS EL LISTENER AL VIEWHOLDER
        return CategoryViewHolder(view, categoryClickListener)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size
}