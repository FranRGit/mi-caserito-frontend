package com.micaserito.app.data.model

data class User(
    val id: Int,
    val email: String,
    val tipoUsuario: String,  // cliente / vendedor
    val token: String
)