package com.micaserito.app.data.model

data class BusinessInfo(
    val nombreNegocio: String,
    val categoriaId: Int,
    val descripcion: String?,
    val ruc: String? = null,
    val direccion: String? = null
)