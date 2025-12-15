package com.micaserito.app.data.repository

import android.util.Log
import com.micaserito.app.data.model.BusinessInfo
import com.micaserito.app.data.model.LoginRequest
import com.micaserito.app.data.model.RegisterRequest
import com.micaserito.app.data.model.UserSessionData
import com.micaserito.app.data.network.NetworkModule

class AuthRepositoryImpl : AuthRepository {

    // 1. Obtenemos la API real
    private val api = NetworkModule.apiService

    // CORRECCIÓN: El tipo de retorno debe ser UserSessionData?
    override suspend fun login(email: String, password: String): UserSessionData? {
        return try {
            // Creamos el objeto request
            val request = LoginRequest(email, password)

            // Llamamos al Backend real
            val response = api.login(request)

            if (response.isSuccessful && response.body() != null) {
                response.body()?.data
            } else {
                Log.e("AuthRepo", "Error login: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("AuthRepo", "Fallo de conexión: ${e.message}")
            null
        }
    }

    override suspend fun registerUser(request: RegisterRequest): Boolean {
        return try {
            // Aquí iría la lógica real cuando tengas el endpoint listo
            true
        } catch (e: Exception) {
            Log.e("AuthRepo", "Error registro: ${e.message}")
            false
        }
    }

    override suspend fun registerBusiness(businessInfo: BusinessInfo): Boolean {
        return true
    }
}