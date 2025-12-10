package com.micaserito.app.data.model

import com.google.gson.annotations.SerializedName

// Respuesta principal del Feed y Buscador
data class HomeFeedResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: FeedData, // Puede ser lista o secciones
    @SerializedName("pagination") val pagination: PaginationMeta
)

// Wrapper para cuando la respuesta tiene secciones (Home)
data class FeedData(
    @SerializedName("sections") val sections: List<FeedSection>? = null,
    @SerializedName("items") val items: List<FeedItem>? = null // Para Discover (lista plana)
)

data class FeedSection(
    @SerializedName("type") val type: String, // "featured_products", "latest_posts"
    @SerializedName("title") val title: String,
    @SerializedName("items") val items: List<FeedItem>
)

data class PaginationMeta(
    @SerializedName("current_page") val currentPage: Int,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("has_more") val hasMore: Boolean
)

// EL ITEM MAESTRO (Puede ser cualquier cosa)
data class FeedItem(
    @SerializedName("type") val type: String, // "product", "post", "business"
    @SerializedName("details") val details: ItemDetails
)

// Esta clase contiene TODOS los campos posibles de los 3 tipos.
// Al ser nullables (?), solo se llenarán los que correspondan al tipo.
data class ItemDetails(
    // -- Campos de PRODUCTO --
    @SerializedName("id_producto") val idProducto: Int? = null,
    @SerializedName("nombre") val nombreProducto: String? = null,
    @SerializedName("precio_base") val precioBase: Double? = null,
    @SerializedName("image_url") val imageUrl: String? = null, // Sirve para Producto y Post

    // -- Campos de POST --
    @SerializedName("id_post") val idPost: Int? = null,
    @SerializedName("descripcion") val descripcion: String? = null,
    @SerializedName("fecha_creacion") val fechaCreacion: String? = null,

    // -- Campos de NEGOCIO (Discover) --
    @SerializedName("id_negocio") val idNegocio: Int? = null,
    @SerializedName("nombre_negocio") val nombreNegocio: String? = null,
    @SerializedName("calificacion_promedio") val calificacionPromedio: Double? = null,
    @SerializedName("direccion") val direccion: String? = null,

    // -- Comunes (Join) --
    @SerializedName("profile_url") val profileUrl: String? = null, // Foto del vendedor
    @SerializedName("ciudad") val ciudad: String? = null
)

// Para los círculos de categorías en Discover
data class CategoriaNegocio(
    @SerializedName("id_categoria_negocio") val id: Int,
    @SerializedName("nombre") val nombre: String
)