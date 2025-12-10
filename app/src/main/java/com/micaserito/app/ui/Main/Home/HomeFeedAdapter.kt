package com.micaserito.app.ui.Home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.micaserito.app.R
import com.micaserito.app.data.model.FeedItem
import com.micaserito.app.ui.Viewholders.PostCardViewHolder
// Importa aquí tu ProductViewHolder si ya lo tienes, si no, usa uno genérico por ahora

class HomeFeedAdapter(private val listaItems: List<FeedItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //EJEMPLO BORRA TODO
    // Constantes para saber qué tipo de vista es
    companion object {
        const val TYPE_POST = 1
        const val TYPE_PRODUCT = 2
    }

    // 1. Determina si el item es un Post o un Producto
    override fun getItemViewType(position: Int): Int {
        return if (listaItems[position].type == "post") TYPE_POST else TYPE_PRODUCT
    }

    // 2. Infla el diseño correcto según el tipo
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return if (viewType == TYPE_POST) {
            // AQUI USAS TU POSTCARDVIEWHOLDER
            val view = inflater.inflate(R.layout.item_post_card, parent, false)
            PostCardViewHolder(view)
        } else {
            // Aquí iría el de productos (item_product_card)
            // Por ahora ponemos el de post para que no te marque error, pero deberías crear ProductViewHolder
            val view = inflater.inflate(R.layout.item_product_card, parent, false)
            PostCardViewHolder(view) // OJO: Cambiar esto por ProductViewHolder cuando lo tengas
        }
    }

    // 3. Pasa los datos a la tarjeta (Render)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = listaItems[position]

        if (holder is PostCardViewHolder) {
            // ¡AQUÍ SE CONSUME TU LÓGICA!
            holder.render(item.details)
        }
        // else if (holder is ProductViewHolder) { ... }
    }

    override fun getItemCount(): Int = listaItems.size
}