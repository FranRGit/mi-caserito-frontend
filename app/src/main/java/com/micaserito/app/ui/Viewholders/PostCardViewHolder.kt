package com.micaserito.app.ui.Viewholders

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Color
import android.net.Uri
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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

    // Elementos de Interacción (Añadidos de feature/alexander_cambios-2 - ASUMIENDO IDs)
    private val btnLike: ImageButton = itemView.findViewById(R.id.btn_like) // ID Asumido
    private val btnComment: ImageButton = itemView.findViewById(R.id.btn_comment) // ID Asumido
    private val btnView: ImageButton = itemView.findViewById(R.id.btn_view) // ID Asumido

    // Estado del like
    private var isLiked: Boolean = false

    init {
        setupClickListeners()
    }

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

        // 4. Resetear estado del Like (Opcional, depende de si el itemDetails tiene un estado 'liked')
        isLiked = false
        btnLike.setColorFilter(Color.parseColor("#616161")) // Color inicial gris
    }

    // --- Lógica Privada (La magia que tú programaste) ---
    private fun setupClickListeners() {

        // --- LÓGICA DE LIKE (Animación + Cambio de Color) ---
        btnLike.setOnClickListener {
            isLiked = !isLiked // Cambiar estado (Toggle)

            if (isLiked) {
                // 1. Cambiar a Rojo
                btnLike.setColorFilter(Color.RED)

                // 2. Animación de "Latido" (Scale Up/Down) con listener seguro
                btnLike.animate()
                    .scaleX(1.2f)
                    .scaleY(1.2f)
                    .setDuration(180)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            btnLike.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(120)
                                .setListener(null)
                        }
                    })
            } else {
                // Volver a Gris y asegurar escala normal
                btnLike.setColorFilter(Color.parseColor("#616161"))
                btnLike.animate().scaleX(1f).scaleY(1f).setDuration(120).setListener(null)
            }
        }

        btnComment.setOnClickListener {
            Toast.makeText(itemView.context, "Abriendo comentarios...", Toast.LENGTH_SHORT).show()
        }

        btnView.setOnClickListener {
            Toast.makeText(itemView.context, "Abriendo perfil...", Toast.LENGTH_SHORT).show()
        }
    }
}