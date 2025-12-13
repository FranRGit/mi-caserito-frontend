package com.micaserito.app.ui.Main.Post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.micaserito.app.data.repository.UploadPostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class UploadPostViewModel(
    private val repository: UploadPostRepository
) : ViewModel() {

    // --- Estado de la UI ---
    // Vista: true=Preview, false=Formulario
    private val _isPreviewMode = MutableStateFlow(false)
    val isPreviewMode: StateFlow<Boolean> = _isPreviewMode

    // Ruta local de la imagen seleccionada (Uri para mostrar, File para enviar)
    private val _imageUri = MutableStateFlow<String?>(null)
    val imageUri: StateFlow<String?> = _imageUri

    // Contenido del post
    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description

    // Estado de carga para el ProgressBar
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _uploadStatus = MutableStateFlow<Result<Unit>?>(null)
    val uploadStatus: StateFlow<Result<Unit>?> = _uploadStatus

    // --- Funciones Públicas ---

    fun setPreviewMode(isPreview: Boolean) {
        _isPreviewMode.value = isPreview
    }

    fun setImageUri(uri: String) {
        _imageUri.value = uri
    }

    fun setDescription(text: String) {
        _description.value = text
    }

    fun clearImage() {
        _imageUri.value = null
        // El Fragmento se encarga de eliminar el archivo temporal
    }

    /**
     * Valida y envía el post.
     */
    fun submitPost(imageFile: File?) {
        if (_isLoading.value) return

        if (_description.value.isBlank()) {
            _uploadStatus.value = Result.failure(Exception("El contenido del post es obligatorio."))
            return
        }

        _isLoading.value = true
        _uploadStatus.value = null

        // 1. Crear las partes RequestBody
        val descriptionPart = createTextRequestBody(_description.value)
        val imagePart = imageFile?.let { createImageMultipart(it) } // Nullable si no hay imagen

        viewModelScope.launch {
            val result = repository.uploadPost(
                imagePart,
                descriptionPart
            )

            _uploadStatus.value = result
            _isLoading.value = false
        }
    }

    // --- Utilidades para Multipart ---

    private fun createTextRequestBody(value: String): RequestBody {
        return RequestBody.create("text/plain".toMediaTypeOrNull(), value)
    }

    private fun createImageMultipart(file: File): MultipartBody.Part {
        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
        // El campo en la API Mock (o el Contrato) es "image"
        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }
}