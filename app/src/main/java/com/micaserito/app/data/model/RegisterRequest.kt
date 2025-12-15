package com.micaserito.app.data.model

data class RegisterRequest(
    val tipoUsuario: String,      // "cliente" o "vendedor"
    val email: String,
    val password: String,
    val nombre: String,
    val apellido: String,
    val dniImage: String?,        // simulamos ruta de imagen
    val profileImage: String?,    // opcional

    // Datos comunes
    val direccion: String,
    val latitud: Double,
    val longitud: Double,

    // Cliente
    val genero: String?,          // "M", "F", "Otro"
    val fechaNacimiento: String?, // YYYY-MM-DD

    // Vendedor (estos van en BusinessInfo, pero podrían llegar aquí)
    val telefono: String? = null
)