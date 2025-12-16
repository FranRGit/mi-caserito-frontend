package com.micaserito.app.ui.Viewholders
import com.bumptech.glide.load.engine.DiskCacheStrategy
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.micaserito.app.R
import com.micaserito.app.data.api.MockData
import com.micaserito.app.data.model.ItemDetails
import com.micaserito.app.data.model.TicketSummary
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class ProductCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val tvSellerName: TextView = itemView.findViewById(R.id.tv_seller_name)
    private val cvProfileContainer: CardView = itemView.findViewById(R.id.cv_profile_container)
    private val btnOptions: ImageView = itemView.findViewById(R.id.btn_options)
    private val ivProductImage: ImageView = itemView.findViewById(R.id.iv_product_image)
    private val tvProductName: TextView = itemView.findViewById(R.id.tv_product_name)
    private val tvProductPrice: TextView = itemView.findViewById(R.id.tv_product_price)
    private val tvProductDesc: TextView = itemView.findViewById(R.id.tv_product_desc)
    private val btnAddTicket: MaterialButton = itemView.findViewById(R.id.btn_add_ticket)

    private var isAdded = false

    fun render(item: ItemDetails) {

        // --- Reset estado reciclado ---
        isAdded = false
        btnAddTicket.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor("#2E7D32"))

        // --- Datos ---
        tvSellerName.text = item.nombreNegocio ?: "Negocio Desconocido"
        tvProductName.text = item.nombreProducto ?: "Producto"
        tvProductPrice.text = "S/. ${String.format("%.2f", item.precioBase ?: 0.0)}"
        tvProductDesc.text = item.descripcion ?: ""

        // --- Imagen del Producto ---
        val imageUrl = item.imageUrl
        if (!imageUrl.isNullOrBlank()) {
            ivProductImage.visibility = View.VISIBLE

            Glide.with(itemView)
                .load(imageUrl)
                .placeholder(android.R.color.darker_gray)
                .centerCrop()
                .into(ivProductImage)
        } else {
            ivProductImage.visibility = View.INVISIBLE
            Glide.with(itemView).clear(ivProductImage)
        }

        setupClickListeners(item)
    }

    private fun setupClickListeners(item: ItemDetails) {

        btnOptions.setOnClickListener {
            Toast.makeText(itemView.context, "Ver más opciones", Toast.LENGTH_SHORT).show()
        }

        val profileClickListener = View.OnClickListener {
            Toast.makeText(itemView.context, "Dirigiendo al perfil...", Toast.LENGTH_SHORT).show()
        }
        cvProfileContainer.setOnClickListener(profileClickListener)
        tvSellerName.setOnClickListener(profileClickListener)

        btnAddTicket.setOnClickListener {
            if (!isAdded) {
                val newTicket = TicketSummary(
                    idTicket = Random.nextInt(1000, 9999),
                    codigoTicket = "T-${Random.nextInt(100, 999)}",
                    titulo = item.nombreProducto ?: "Producto sin nombre",
                    total = item.precioBase ?: 0.0,
                    estado = "Negociando",
                    fecha = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
                )

                MockData.addMyTicket(newTicket)

                btnAddTicket.backgroundTintList =
                    ColorStateList.valueOf(Color.GRAY)

                Toast.makeText(
                    itemView.context,
                    "Ticket generado: ${newTicket.codigoTicket}",
                    Toast.LENGTH_SHORT
                ).show()

                isAdded = true
            } else {
                Toast.makeText(
                    itemView.context,
                    "Este producto ya tiene un ticket",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
