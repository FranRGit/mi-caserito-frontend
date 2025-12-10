package com.micaserito.app.ui.Viewholders

import android.animation.AnimatorListenerAdapter
import android.animation.Animator
import android.graphics.Color
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.micaserito.app.R
import com.micaserito.app.data.model.ItemDetails


class PostCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    // 1. Referencias a las vistas (usando findViewById para máxima compatibilidad)
    private val tvUsername: TextView = itemView.findViewById(R.id.tv_username)
    private val tvSubtitle: TextView = itemView.findViewById(R.id.tv_subtitle)
    private val tvContent: TextView = itemView.findViewById(R.id.tv_post_content)
    private val ivProfile: ImageView = itemView.findViewById(R.id.iv_profile_pic)
    private val ivPostImage: ImageView = itemView.findViewById(R.id.iv_post_image)

    // Botones de acción
    private val btnLike: ImageButton = itemView.findViewById(R.id.btn_like)
    private val btnComment: ImageButton = itemView.findViewById(R.id.btn_comment)
    private val btnView: ImageButton = itemView.findViewById(R.id.btn_view)

    // Estado local para la animación del Like
    private var isLiked = false

    // 2. Función Principal: RENDERIZAR
    // Tus compañeros solo llamarán a holder.render(datos)
    fun render(item: ItemDetails) {

        // --- A. Setear Datos ---
        tvUsername.text = item.nombreNegocio ?: "Usuario Desconocido"
        tvSubtitle.text = item.fechaCreacion ?: "Hace un momento"
        tvContent.text = item.descripcion ?: ""

        // TODO: Aquí deberían usar Glide o Picasso para cargar las fotos reales
        // Glide.with(itemView.context).load(item.imageUrl).into(ivPostImage)

        // --- B. Activar la Lógica de Botones ---
        setupClickListeners()
    }

    // 3. Lógica Privada (La magia que tú programaste)
    private fun setupClickListeners() {

        // --- LÓGICA DE LIKE (Animación + Cambio de Color) ---
        btnLike.setOnClickListener {
            isLiked = !isLiked // Cambiar estado (Toggle)

            if (isLiked) {
                // 1. Cambiar a Rojo
                btnLike.setColorFilter(Color.RED)

                // 2. Animación de "Latido" (Scale Up/Down)
                btnLike.animate()
                    .scaleX(1.2f)
                    .scaleY(1.2f)
                    .setDuration(100)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            super.onAnimationEnd(animation)
                            btnLike.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100)
                        }
                    })
            } else {
                // Volver a Gris
                btnLike.setColorFilter(Color.parseColor("#616161"))
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