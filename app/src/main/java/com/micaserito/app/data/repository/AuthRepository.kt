package com.micaserito.app.data.repository

import com.micaserito.app.data.api.MockData
import com.micaserito.app.data.model.BusinessInfo
import com.micaserito.app.data.model.RegisterRequest
import com.micaserito.app.data.model.User
import com.micaserito.app.data.model.UserSessionData

interface AuthRepository {

    /** LOGIN */
    suspend fun login(email: String, password: String): UserSessionData?

    /** REGISTRO DE USUARIO */
    suspend fun registerUser(request: RegisterRequest): Boolean

    /** REGISTRO DE NEGOCIO (OPCIONAL SI ES VENDEDOR) */
    suspend fun registerBusiness(businessInfo: BusinessInfo): Boolean
}
