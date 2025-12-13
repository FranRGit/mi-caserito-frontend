package com.micaserito.app.data.repository

import com.micaserito.app.data.api.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class UploadPostRepository(
    private val apiService: ApiService
) {
    /**
     * Sube un nuevo post al servidor utilizando Multipart/Form-Data.
     * @param imagePart El archivo de imagen listo como parte MultipartBody (opcional).
     * @param descriptionPart El contenido de texto del post.
     */
    suspend fun uploadPost(
        imagePart: MultipartBody.Part?, // Permite ser nulo
        descriptionPart: RequestBody
    ): Result<Unit> {

        // La MockApiService solo tiene la firma que recibe image y descripcion:
        // override suspend fun createPost(image: MultipartBody.Part, descripcion: RequestBody): Response<FeedItem>
        // Si la imagen es nula, enviaremos un placeholder o una parte vacía.

        return try {
            val response = apiService.createPost(
                // Si la imagen no está presente, enviamos una parte dummy (esto depende de cómo la API lo maneje)
                image = imagePart ?: createEmptyImagePart(),
                descripcion = descriptionPart
            )

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al subir el post: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun createEmptyImagePart(): MultipartBody.Part {
        // Crea una parte Multipart vacía para cuando el post no tiene imagen
        return MultipartBody.Part.createFormData("image", "")
    }
}