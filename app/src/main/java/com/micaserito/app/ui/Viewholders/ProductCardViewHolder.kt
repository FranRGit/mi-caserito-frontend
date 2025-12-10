package com.micaserito.app.ui.Viewholders

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.micaserito.app.R
import com.micaserito.app.data.model.ItemDetails

class ProductCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    // 1. Referencias a las Vistas
    private val tvSellerName: TextView = itemView.findViewById(R.id.tv_seller_name)
    private val cvProfileContainer: CardView = itemView.findViewById(R.id.cv_profile_container)
    private val btnOptions: ImageView = itemView.findViewById(R.id.btn_options)

    private val ivProductImage: ImageView = itemView.findViewById(R.id.iv_product_image)
    private val tvProductName: TextView = itemView.findViewById(R.id.tv_product_name)
    private val tvProductPrice: TextView = itemView.findViewById(R.id.tv_product_price)
    private val tvProductDesc: TextView = itemView.findViewById(R.id.tv_product_desc)

    private val btnAddTicket: MaterialButton = itemView.findViewById(R.id.btn_add_ticket)

    // Variable para controlar si el producto ya fue agregado
    private var isAdded = false

    // 2. Método Render (El que llaman tus compañeros)
    fun render(item: ItemDetails) {

        // --- A. Poner los datos ---
        tvSellerName.text = item.nombreNegocio ?: "Negocio Desconocido"
        tvProductName.text = item.nombreProducto ?: "Producto"

        // Formato de precio: S/. 5.50
        tvProductPrice.text = "S/. ${String.format("%.2f", item.precioBase ?: 0.0)}"

        // Descripción o Unidad (ej: "Por kilo")
        tvProductDesc.text = item.descripcion ?: ""

        // TODO: Cargar imagen con Glide/Picasso
        // Glide.with(itemView.context).load(item.imageUrl).into(ivProductImage)

        // --- B. Configurar Clics y Lógica ---
        setupClickListeners()
    }

    private fun setupClickListeners() {

        // 1. Lógica: Ver Más (3 puntos)
        btnOptions.setOnClickListener {
            Toast.makeText(itemView.context, "Ver más opciones", Toast.LENGTH_SHORT).show()
        }

        // 2. Lógica: Ir al Perfil (Clic en la foto O en el nombre)
        val profileClickListener = View.OnClickListener {
            Toast.makeText(itemView.context, "Dirigiendo al perfil...", Toast.LENGTH_SHORT).show()
        }
        cvProfileContainer.setOnClickListener(profileClickListener)
        tvSellerName.setOnClickListener(profileClickListener)

        // 3. Lógica: Agregar Ticket (Botón +)
        btnAddTicket.setOnClickListener {
            if (!isAdded) {
                // CAMBIAR A GRIS (Simula desactivado/agregado)
                btnAddTicket.backgroundTintList = ColorStateList.valueOf(Color.GRAY)
                // Opcional: Cambiar icono a check
                // btnAddTicket.setIconResource(R.drawable.ic_check)

                Toast.makeText(itemView.context, "Creando ticket...", Toast.LENGTH_SHORT).show()
                isAdded = true
            } else {
                btnAddTicket.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#2E7D32")) // Tu verde original
                isAdded = false
            }
        }
    }
}