package com.micaserito.app.data.repository

import com.micaserito.app.data.api.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class UploadProductRepository(
    private val apiService: ApiService
) {
    /**
     * Sube un nuevo producto al servidor utilizando Multipart/Form-Data.
     * La implementación se ajusta a la firma del ApiService que usa Map<String, RequestBody>.
     */
    suspend fun uploadProduct(
        imagePart: MultipartBody.Part, // File (Binary)
        name: RequestBody,
        id_categoria_producto: RequestBody,
        precio: RequestBody,
        stock: RequestBody,
        negociable: RequestBody,
        descripcion: RequestBody?, // Opcional
        fecha_inicio: RequestBody?, // Requerido si negociable=true
        fecha_fin: RequestBody? // Requerido si negociable=true
    ): Result<Unit> {

        // Helper para crear un RequestBody vacío si el campo opcional es nulo
        val emptyBody = RequestBody.create(MultipartBody.FORM, "")

        // 1. Construir el Map<String, RequestBody> para los campos de texto
        val dataMap = mutableMapOf<String, RequestBody>()

        dataMap["name"] = name
        dataMap["id_categoria_producto"] = id_categoria_producto
        dataMap["precio"] = precio
        dataMap["stock"] = stock
        dataMap["negociable"] = negociable // <--- Soluciona el envío de negociable

        // Añadir campos opcionales/condicionales
        dataMap["descripcion"] = descripcion ?: emptyBody
        dataMap["fecha_inicio"] = fecha_inicio ?: emptyBody // <--- Soluciona el envío de fecha_inicio
        dataMap["fecha_fin"] = fecha_fin ?: emptyBody // <--- Soluciona el envío de fecha_fin

        // 2. Llamar al API con la firma correcta (image + data map)
        return try {
            val response = apiService.createProduct( // <-- CAMBIO DE MÉTODO
                image = imagePart,
                data = dataMap // <-- Se pasa el mapa
            )

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                // Manejo de errores 400, 500, etc.
                Result.failure(Exception("Error al subir el producto: ${response.code()}"))
            }
        } catch (e: Exception) {
            // Manejo de errores de red
            Result.failure(e)
        }
    }

    // Nota: Necesitas funciones utilitarias en el ViewModel para convertir String/Double a RequestBody
    // RequestBody.create(MediaType.parse("text/plain"), value)
}