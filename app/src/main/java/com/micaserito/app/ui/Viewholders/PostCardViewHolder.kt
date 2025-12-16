package com.micaserito.app.ui.Viewholders

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Color
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.micaserito.app.R
import com.micaserito.app.data.model.ItemDetails

/**
 * ViewHolder reutilizable para el layout item_post_card.xml
 * Se usa para mostrar posts en el RecyclerView (Home/Discover)
 * y en la Vista Previa (UploadPostFragment).
 */
class PostCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val ivProfile: ImageView = itemView.findViewById(R.id.iv_profile_pic)
    private val tvBusinessName: TextView = itemView.findViewById(R.id.tv_username)
    private val tvSubtitle: TextView = itemView.findViewById(R.id.tv_subtitle)
    private val ivContent: ImageView = itemView.findViewById(R.id.iv_post_image)
    private val tvDescription: TextView = itemView.findViewById(R.id.tv_post_content)

    private val btnLike: ImageButton = itemView.findViewById(R.id.btn_like)
    private val btnComment: ImageButton = itemView.findViewById(R.id.btn_comment)
    private val btnView: ImageButton = itemView.findViewById(R.id.btn_view)

    private var isLiked = false

    init {
        setupClickListeners()
    }

    fun render(item: ItemDetails) {
        // --- Header ---
        tvBusinessName.text = item.nombreNegocio ?: "Vendedor Caserito"
        tvSubtitle.text = item.fechaCreacion ?: "Vista Previa"

// --- Imagen de Perfil ---
        Glide.with(itemView)
            .load(item.profileUrl)
            .placeholder(android.R.color.darker_gray)
            .circleCrop()
            .into(ivProfile)

        // --- Contenido ---
        tvDescription.text = item.descripcion

        // --- Imagen del Post ---
        val imageUrl = item.imageUrl
        if (!imageUrl.isNullOrBlank()) {
            ivContent.isVisible = true

            Glide.with(itemView)
                .load(imageUrl)
                .placeholder(android.R.color.darker_gray)
                .centerCrop()
                .into(ivContent)
        } else {
            ivContent.isVisible = false
            Glide.with(itemView).clear(ivContent)
        }

        // --- Reset estado reciclado ---
        isLiked = false
        btnLike.setColorFilter(Color.parseColor("#616161"))
        btnLike.scaleX = 1f
        btnLike.scaleY = 1f
    }

    private fun setupClickListeners() {
        btnLike.setOnClickListener {
            isLiked = !isLiked

            if (isLiked) {
                btnLike.setColorFilter(Color.RED)
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
