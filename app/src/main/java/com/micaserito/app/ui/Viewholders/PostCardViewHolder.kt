package com.micaserito.app.ui.Viewholders

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.micaserito.app.R
import com.micaserito.app.data.model.ItemDetails
import androidx.recyclerview.widget.RecyclerView
/**
 * ViewHolder reutilizable para el layout item_post_card.xml
 * Se usa para mostrar posts en el RecyclerView (Home/Discover) y en la Vista Previa (UploadPostFragment).
 */
class PostCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    // Mapeo a los IDs EXACTOS de item_post_card.xml
    private val ivProfile: ImageView = itemView.findViewById(R.id.iv_profile_pic)
    private val tvBusinessName: TextView = itemView.findViewById(R.id.tv_username) // Usando tv_username para el nombre del negocio
    private val tvSubtitle: TextView = itemView.findViewById(R.id.tv_subtitle) // Campo extra para subtítulo o fecha
    private val ivContent: ImageView = itemView.findViewById(R.id.iv_post_image) // La imagen grande del post
    private val tvDescription: TextView = itemView.findViewById(R.id.tv_post_content) // El texto de la publicación

    fun render(item: ItemDetails) {
        // 1. Datos del Header (Simulación del Vendedor)
        tvBusinessName.text = item.nombreNegocio ?: "Vendedor Caserito" // Usar nombre del negocio
        tvSubtitle.text = item.fechaCreacion ?: "Vista Previa" // Usar la fecha o un placeholder

        // La imagen de perfil (ivProfile) usaría Glide/Coil, por ahora usaremos el placeholder.

        // 2. Contenido del Post
        tvDescription.text = item.descripcion

        // 3. Imagen del Contenido
        val imageUrl = item.imageUrl
        if (!imageUrl.isNullOrBlank()) {
            ivContent.isVisible = true
            // En la Vista Previa (UploadPostFragment), imageUrl es una Uri local (String)
            ivContent.setImageURI(Uri.parse(imageUrl))

            // Nota: El fondo gris en item_post_card.xml para la imagen
            // se puede ocultar para limpiar la vista.
        } else {
            ivContent.isVisible = false
        }
    }
}