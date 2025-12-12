package com.micaserito.app.data.repository

import com.micaserito.app.data.api.ApiService
import com.micaserito.app.data.model.FeedItem
import com.micaserito.app.data.model.HomeFeedResponse
import com.micaserito.app.data.model.ItemDetails // Necesario para crear el Header

/**
 * Repositorio para la pantalla Home.
 * Se encarga de manejar la lógica de paginación y la fuente de datos.
 */
class HomeRepository(
    private val apiService: ApiService
) {
    private val PAGE_LIMIT = 10 // Limite de items por página, según el requerimiento.

    /**
     * Llama al endpoint /home/feed y gestiona la respuesta.
     */
    suspend fun fetchHomeFeedPage(page: Int): Result<HomeFeedResponse> {
        return try {
            val response = apiService.getHomeFeed(page = page, limit = PAGE_LIMIT)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                // Error de servidor (ej. 404, 500)
                Result.failure(Exception("Error en la respuesta: ${response.code()}"))
            }
        } catch (e: Exception) {
            // Error de red (Timeouts, no connection)
            Result.failure(e)
        }
    }

    /**
     * Función helper para aplanar la respuesta de la API (que usa secciones)
     * en una lista simple de FeedItem, INYECTANDO EL HEADER DE NEGOCIOS.
     */
    fun extractFeedItems(response: HomeFeedResponse): List<FeedItem> {
        val flatList = mutableListOf<FeedItem>()

        // 1. Iteramos sobre las secciones que vienen de la API
        response.data.sections?.forEach { section ->
            if (section.items.isNotEmpty()) {
                // Verificamos el tipo de sección (ej. featured_products o latest_posts)

                // Si la sección es de Posts o Contenido de Usuarios, inyectamos el Header
                if (section.type == "latest_posts") {
                    // CREACIÓN DEL ITEM HEADER (type="header")
                    // Se necesita un ItemDetails para cumplir con el modelo, aunque esté vacío.
                    val headerItem = FeedItem(type = "header", details = ItemDetails())
                    flatList.add(headerItem)
                }

                // 2. Añadimos todos los ítems de la sección a la lista plana
                flatList.addAll(section.items)
            }
        }

        // Si la respuesta usa la lista plana (para Discover), la agregamos también (aunque no es el caso de Home)
        response.data.items?.let {
            flatList.addAll(it)
        }

        // Si la lista está vacía, esto indica que no se recibió data, pero ya no fallará por NullPointerException.
        return flatList
    }
}