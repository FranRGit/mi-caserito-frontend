package com.micaserito.app.data.model

import com.google.gson.annotations.SerializedName

data class ItemDetails(
    // -- Campos de PRODUCTO --
    @SerializedName("id_producto") val idProducto: Int? = null,
    @SerializedName("nombre") val nombreProducto: String? = null,
    @SerializedName("precio_base") val precioBase: Double? = null,
    @SerializedName("image_url") val imageUrl: String? = null,

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
    @SerializedName("profile_url") val profileUrl: String? = null,
    @SerializedName("ciudad") val ciudad: String? = null,
    @SerializedName("categoria") val categoria: String? = null
)

data class CategoriaNegocio(
    @SerializedName("id_categoria_negocio") val id: Int,
    @SerializedName("nombre") val nombre: String
)
