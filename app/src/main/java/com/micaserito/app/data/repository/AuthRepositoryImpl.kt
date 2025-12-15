package com.micaserito.app.data.repository

import com.micaserito.app.data.api.MockData
import com.micaserito.app.data.model.BusinessInfo
import com.micaserito.app.data.model.RegisterRequest
import com.micaserito.app.data.model.User

class AuthRepositoryImpl : AuthRepository {

    /** LOGIN DE SIMULACIÓN */
    override fun login(email: String, password: String): User? {
        return MockData.loginFake(email, password)
    }

    /** REGISTRO DE USUARIO SIMULADO */
    override fun registerUser(request: RegisterRequest): Boolean {
        return MockData.registrarUsuario(request)
    }

    /** REGISTRO DE NEGOCIO SIMULADO */
    override fun registerBusiness(businessInfo: BusinessInfo): Boolean {
        return MockData.registrarNegocio(businessInfo)
    }
}
