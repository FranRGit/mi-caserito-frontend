package com.micaserito.app.data.model

import com.google.gson.annotations.SerializedName

// --- REQUESTS  ---

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

// --- RESPONSES ---

data class LoginResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: UserSessionData
)

data class UserSessionData(
    @SerializedName("id_usuario") val idUsuario: Int,
    @SerializedName("tipo_usuario") val tipoUsuario: String, // "cliente" o "vendedor"
    @SerializedName("token") val token: String
)

data class RegisterResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String?
)