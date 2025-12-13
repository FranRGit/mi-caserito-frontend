package com.micaserito.app.ui.Main.Product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.micaserito.app.data.model.CategoriaNegocio
import com.micaserito.app.data.repository.UploadProductRepository
import com.micaserito.app.data.api.MockData // Para simular la carga de categorías
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class UploadProductViewModel(
    private val repository: UploadProductRepository
) : ViewModel() {

    // --- Estado de la UI ---
    // Maneja la visibilidad del campo de fechas (Requerimiento "Negociable")
    private val _isNegotiable = MutableStateFlow(false)
    val isNegotiable: StateFlow<Boolean> = _isNegotiable

    // Ruta local de la imagen seleccionada
    private val _imageUri = MutableStateFlow<String?>(null)
    val imageUri: StateFlow<String?> = _imageUri

    // Estado de carga para el ProgressBar
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Eventos para notificar a la UI (ej. éxito, error)
    private val _uploadStatus = MutableStateFlow<Result<Unit>?>(null)
    val uploadStatus: StateFlow<Result<Unit>?> = _uploadStatus

    // Mock de Categorías (se cargaría desde otro Repositorio en una app real)
    private val _categories = MutableStateFlow<List<CategoriaNegocio>>(emptyList())
    val categories: StateFlow<List<CategoriaNegocio>> = _categories

    init {
        loadCategories()
    }

    private fun loadCategories() {
        // En una app real, llamarías a CategoryRepository.fetchCategories()
        _categories.value = MockData.getCategories()
    }

    // --- Funciones Públicas ---

    fun setNegotiable(isNegotiable: Boolean) {
        _isNegotiable.value = isNegotiable
    }

    fun setImageUri(uri: String) {
        _imageUri.value = uri
    }
    fun clearImage() {
        _imageUri.value = null
    }
    /**
     * Valida y prepara los datos para el envío Multipart/Form-Data.
     * @param form: Data class simple con todos los campos del formulario.
     * @param imageFile: Archivo de imagen ya comprimido y guardado localmente.
     */
    fun submitProduct(form: ProductForm, imageFile: File?) {
        if (_isLoading.value) return

        if (!validateForm(form, imageFile)) {
            // La validación falló (error manejado dentro de validateForm)
            return
        }

        _isLoading.value = true
        _uploadStatus.value = null // Limpiar el estado anterior

        // 1. Convertir el archivo a RequestBody y Part
        val imagePart = createImageMultipart(imageFile!!)

        // 2. Convertir campos de texto a RequestBody
        val namePart = createTextRequestBody(form.name)
        val categoryIdPart = createTextRequestBody(form.categoryId.toString())
        val pricePart = createTextRequestBody(form.price.toString())
        val stockPart = createTextRequestBody(form.stock.toString())
        val negotiablePart = createTextRequestBody(form.negotiable.toString()) // true/false

        // 3. Partes opcionales
        val descriptionPart = form.description?.let { createTextRequestBody(it) }
        val dateStartPart = form.dateStart?.let { createTextRequestBody(it) }
        val dateEndPart = form.dateEnd?.let { createTextRequestBody(it) }

        viewModelScope.launch {
            val result = repository.uploadProduct(
                imagePart,
                namePart,
                categoryIdPart,
                pricePart,
                stockPart,
                negotiablePart,
                descriptionPart,
                dateStartPart,
                dateEndPart
            )

            _uploadStatus.value = result // Notifica al Fragmento el resultado
            _isLoading.value = false
        }
    }

    private fun validateForm(form: ProductForm, imageFile: File?): Boolean {
        // Validación básica (debería ser más robusta, con mensajes de error)
        if (form.name.isBlank() || form.price <= 0 || form.stock <= 0 || form.categoryId == -1) {
            _uploadStatus.value = Result.failure(Exception("Completa todos los campos requeridos."))
            return false
        }

        if (imageFile == null || !imageFile.exists()) {
            _uploadStatus.value = Result.failure(Exception("Debes seleccionar una imagen para el producto."))
            return false
        }

        // Validación de fechas (Requerimiento)
        if (form.negotiable) {
            if (form.dateStart.isNullOrBlank() || form.dateEnd.isNullOrBlank()) {
                _uploadStatus.value = Result.failure(Exception("Las fechas de inicio y fin son obligatorias para productos negociables."))
                return false
            }
            // Falta: Validación de que Fecha Fin no sea menor que Fecha Inicio
        }
        return true
    }


    // --- Utilidades para Multipart ---

    private fun createTextRequestBody(value: String): RequestBody {
        // Usamos text/plain para los campos de texto
        return RequestBody.create("text/plain".toMediaTypeOrNull(), value)
    }

    private fun createImageMultipart(file: File): MultipartBody.Part {
        // Crear RequestBody para el archivo (Binary)
        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)

        // Crear MultipartBody.Part usando el nombre del campo de la API ("image")
        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }
}

// Data class simple para facilitar el paso de datos del formulario al ViewModel
data class ProductForm(
    val name: String,
    val description: String?,
    val categoryId: Int,
    val price: Double,
    val stock: Int,
    val negotiable: Boolean,
    val dateStart: String?,
    val dateEnd: String?
)