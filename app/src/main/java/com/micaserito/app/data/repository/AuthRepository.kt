package com.micaserito.app.data.repository

import com.micaserito.app.data.api.MockData
import com.micaserito.app.data.model.BusinessInfo
import com.micaserito.app.data.model.RegisterRequest
import com.micaserito.app.data.model.User

interface AuthRepository {

    /** LOGIN */
    fun login(email: String, password: String): User?

    /** REGISTRO DE USUARIO */
    fun registerUser(request: RegisterRequest): Boolean

    /** REGISTRO DE NEGOCIO (OPCIONAL SI ES VENDEDOR) */
    fun registerBusiness(businessInfo: BusinessInfo): Boolean
}
