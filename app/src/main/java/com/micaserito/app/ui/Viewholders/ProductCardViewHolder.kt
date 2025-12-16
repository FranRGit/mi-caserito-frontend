package com.micaserito.app.ui.Viewholders

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.micaserito.app.R
import com.micaserito.app.data.api.MockData
import com.micaserito.app.data.Local.SessionManager // Import necesario
import com.micaserito.app.data.model.ItemDetails

class ProductCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    // Referencias a las vistas
    private val tvSellerName: TextView = itemView.findViewById(R.id.tv_seller_name)
    private val cvProfileContainer: CardView = itemView.findViewById(R.id.cv_profile_container)
    private val btnOptions: ImageView = itemView.findViewById(R.id.btn_options)
    private val ivProductImage: ImageView = itemView.findViewById(R.id.iv_product_image)
    private val tvProductName: TextView = itemView.findViewById(R.id.tv_product_name)
    private val tvProductPrice: TextView = itemView.findViewById(R.id.tv_product_price)
    private val tvProductDesc: TextView = itemView.findViewById(R.id.tv_product_desc)
    private val btnAddTicket: MaterialButton = itemView.findViewById(R.id.btn_add_ticket)

    fun render(item: ItemDetails) {

        // 1. OBTENCIÓN DE ID DE USUARIO DE LA SESIÓN (Usando el SessionManager)
        val context = itemView.context
        val storedId = SessionManager.getUserId(context)
        // Usamos 1 como fallback si el ID no se pudo obtener (asumiendo que ID 1 es el cliente)
        val userId = if (storedId != 0) storedId else 1

        // 2. RESTAURAR RENDERIZADO COMPLETO (SOLUCIÓN a "no puedo ver nombre de productos")
        tvSellerName.text = item.nombreNegocio ?: "Negocio Desconocido"
        tvProductName.text = item.nombreProducto ?: "Producto Desconocido" // <-- Aquí se corrige el nombre
        tvProductPrice.text = "S/. ${String.format("%.2f", item.precioBase ?: 0.0)}"
        tvProductDesc.text = item.descripcion ?: ""

        // Lógica de carga de imagen (Recuperada de la versión original)
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

        // 3. LÓGICA DE TICKETS: Verificar estado inicial y actualizar botón
        val yaEstaEnTicket = MockData.isProductAdded(userId = userId, productId = item.idProducto ?: -1)
        updateButtonState(yaEstaEnTicket)

        // 4. Configurar listeners
        setupClickListeners(item, userId)
    }

    private fun setupClickListeners(item: ItemDetails, userId: Int) {

        // Listeners de navegación (Opciones y Perfil)
        btnOptions.setOnClickListener {
            Toast.makeText(itemView.context, "Ver más opciones", Toast.LENGTH_SHORT).show()
        }
        val profileClickListener = View.OnClickListener {
            Toast.makeText(itemView.context, "Dirigiendo al perfil: ${item.nombreNegocio}...", Toast.LENGTH_SHORT).show()
        }
        cvProfileContainer.setOnClickListener(profileClickListener)
        tvSellerName.setOnClickListener(profileClickListener)

        // Listener del botón de Ticket (Lógica de la derecha)
        btnAddTicket.setOnClickListener {

            if (btnAddTicket.backgroundTintList?.defaultColor == Color.GRAY) {
                Toast.makeText(itemView.context, "Este producto ya tiene un ticket asociado.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Llama a MockData para agregar el producto.
            // (Se asume que MockData.addProductToTicket devuelve un mensaje y actualiza el estado interno)
            val mensaje = MockData.addProductToTicket(userId = userId, product = item)

            Toast.makeText(itemView.context, mensaje, Toast.LENGTH_SHORT).show()

            // Pintamos el botón de gris (asociado/agregado) inmediatamente después del éxito
            updateButtonState(true)
        }
    }

    // Función auxiliar para pintar el botón
    private fun updateButtonState(isAdded: Boolean) {
        if (isAdded) {
            // Estado: AGREGADO (Gris)
            btnAddTicket.backgroundTintList = ColorStateList.valueOf(Color.GRAY)
            btnAddTicket.setIconResource(R.drawable.ic_plus) // Cambiar a un checkmark
        } else {
            // Estado: DISPONIBLE (Verde)
            btnAddTicket.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#346536"))
            btnAddTicket.isEnabled = true
            btnAddTicket.setIconResource(R.drawable.ic_plus)
        }
    }
}