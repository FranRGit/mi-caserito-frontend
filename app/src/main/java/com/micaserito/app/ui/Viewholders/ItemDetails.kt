package com.micaserito.app.ui.Viewholders

data class ItemDetails(
    val idNegocio: Int? = null,
    val idProducto: Int? = null,
    val idPost: Int? = null,

    val nombreNegocio: String? = null,
    val nombreProducto: String? = null,
    val descripcion: String? = null,
    val precioBase: Double? = null,

    val imageUrl: String? = null,        // 🟢 producto / post
    val profileUrl: String? = null,      // 🟢 imagen de perfil
    val fechaCreacion: String? = null,

    val categoria: String? = null
)


