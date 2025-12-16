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
import com.micaserito.app.data.api.MockData // <--- USAMOS MOCKDATA DIRECTO
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
        // ... (tus líneas de asignar texto: nombre, precio, etc. déjalas igual) ...

        // 1. Obtenemos el contexto desde la vista
        val context = itemView.context

        // 2. Obtenemos el ID del usuario real
        val storedId = com.micaserito.app.data.Local.SessionManager.getUserId(context)
        val userId = if (storedId != 0) storedId else 1 // Fallback a 1 si no hay login

        // 3. PREGUNTA: ¿Este usuario ya tiene este producto en su ticket?
        val yaEstaEnTicket = MockData.isProductAdded(userId = userId, productId = item.idProducto ?: -1)

        // 4. Actualizamos el color del botón (Gris o Verde)
        updateButtonState(yaEstaEnTicket)

        // 5. Configuramos el click pasando el userId correcto
        setupClickListeners(item, userId)
    }

    // Asegúrate que tu función setupClickListeners reciba el userId
    private fun setupClickListeners(item: ItemDetails, userId: Int) {
        // ... (otros listeners) ...

        btnAddTicket.setOnClickListener {
            // Usamos el ID real al agregar
            val mensaje = MockData.addProductToTicket(userId = userId, product = item)

            Toast.makeText(itemView.context, mensaje, Toast.LENGTH_SHORT).show()

            // Pintamos el botón de gris inmediatamente
            updateButtonState(true)
        }
    }

    // Función auxiliar para pintar el botón
    private fun updateButtonState(isAdded: Boolean) {
        if (isAdded) {
            btnAddTicket.backgroundTintList = ColorStateList.valueOf(Color.GRAY)

        } else {
            // Estado: DISPONIBLE (Verde)
            btnAddTicket.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#346536"))
            btnAddTicket.isEnabled = true
            btnAddTicket.setIconResource(R.drawable.ic_plus)
        }
    }
}