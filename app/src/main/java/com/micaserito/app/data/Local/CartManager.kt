package com.micaserito.app.data.Local

import com.micaserito.app.data.model.ItemDetails
import com.micaserito.app.data.model.TicketItem

object CartManager {

    // MAPA DE BORRADORES (IdNegocio -> Lista de Items)
    private val drafts = mutableMapOf<Int, MutableList<TicketItem>>()

    // MAPA DE NOMBRES (IdNegocio -> Nombre del Negocio)
    private val businessNames = mutableMapOf<Int, String>()

    // Variable para saber cuál es el negocio "activo" (el último al que agregaste algo)
    private var lastActiveBusinessId: Int? = null

    /**
     * Propiedad pública para obtener los items del negocio actual
     * Esto soluciona: CartManager.items
     */
    val items: List<TicketItem>
        get() = drafts[lastActiveBusinessId] ?: emptyList()

    fun addItem(product: ItemDetails, quantity: Int = 1) {
        val businessId = product.idNegocio ?: return
        val productId = product.idProducto ?: return

        // Guardamos este negocio como el "activo"
        lastActiveBusinessId = businessId
        // Guardamos el nombre del negocio para usarlo luego
        businessNames[businessId] = product.nombreNegocio ?: "Negocio Desconocido"

        if (!drafts.containsKey(businessId)) {
            drafts[businessId] = mutableListOf()
        }

        val currentList = drafts[businessId]!!
        val existingItem = currentList.find { it.idProducto == productId }

        if (existingItem != null) {
            existingItem.cantidad += quantity
        } else {
            currentList.add(
                TicketItem(
                    idProducto = productId,
                    nombre = product.nombreProducto ?: "Producto",
                    precioUnitario = product.precioBase ?: 0.0,
                    cantidad = quantity
                )
            )
        }
    }

    // --- FUNCIONES QUE TE FALTABAN (Solución al error Unresolved reference) ---

    fun getCurrentBusinessId(): Int? {
        return lastActiveBusinessId
    }

    fun getCurrentBusinessName(): String? {
        return businessNames[lastActiveBusinessId]
    }

    fun getTotal(): Double {
        return items.sumOf { it.subtotal }
    }

    fun isProductAdded(businessId: Int?, productId: Int?): Boolean {
        if (businessId == null || productId == null) return false
        val list = drafts[businessId] ?: return false
        return list.any { it.idProducto == productId }
    }

    // Limpia solo el carrito activo actual
    fun clearCart() {
        lastActiveBusinessId?.let { id ->
            drafts.remove(id)
            businessNames.remove(id)
        }
        // Si borramos el activo, buscamos si hay otro, si no, null
        lastActiveBusinessId = drafts.keys.firstOrNull()
    }
}