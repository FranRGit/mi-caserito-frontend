package com.micaserito.app.data.network

import com.micaserito.app.data.api.ApiService
import com.micaserito.app.data.api.MockApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {

    private const val IS_MOCK_MODE = false    // 🔴 CAMBIAR A 'FALSE' CUANDO EL BACKEND ESTÉ LISTO


    private const val BASE_URL = "http://10.0.2.2:3000/api/v1/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Esta es la variable que todos los Repositorios van a llamar
    val apiService: ApiService by lazy {
        if (IS_MOCK_MODE) {
            MockApiService() // Devuelve la data falsa
        } else {
            retrofit.create(ApiService::class.java) // Conecta a internet
        }
    }
}