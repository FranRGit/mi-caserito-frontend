package com.micaserito.app.ui.Main.Product

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.micaserito.app.R
import com.micaserito.app.data.api.MockData
import com.micaserito.app.data.repository.UploadProductRepository
import com.micaserito.app.databinding.FragmentUploadProductBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar

class UploadProductFragment : Fragment(R.layout.fragment_upload_product) {

    private var _binding: FragmentUploadProductBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: UploadProductViewModel // Asumo que esta clase es accesible
    private var compressedImageFile: File? = null

    // ActivityResultLauncher para seleccionar imagen (Cámara o Galería)
    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri = result.data?.data
            imageUri?.let { uri ->
                compressAndSaveImage(uri)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUploadProductBinding.bind(view)

        // ** Inicialización del ViewModel (similar a HomeFragment) **
        val repository = UploadProductRepository(MockData.getMockService()) // Usamos MockService
        val factory = object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                // Aquí deberías cambiar a UploadProductViewModel si el paquete es diferente
                if (modelClass.isAssignableFrom(UploadProductViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return UploadProductViewModel(repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
        viewModel = ViewModelProvider(this, factory).get(UploadProductViewModel::class.java)

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        // 1. Lógica del Checkbox "Producto Negociable"
        binding.cbNegotiable.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setNegotiable(isChecked)
        }

        // 2. Click para seleccionar imagen
        binding.flImageContainer.setOnClickListener {
            showImageSourceDialog()
        }

        // 3. Click para seleccionar Fecha Inicio
        binding.etDateStart.setOnClickListener {
            showDatePicker(binding.etDateStart)
        }

        // 4. Click para seleccionar Fecha Fin
        binding.etDateEnd.setOnClickListener {
            showDatePicker(binding.etDateEnd)
        }

        // 5. Click para Publicar
        binding.btnPublish.setOnClickListener {
            collectAndSubmitData()
        }
    }

    private fun observeViewModel() {
        // 1. Observar visibilidad de fechas
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isNegotiable.collect { isNegotiable ->
                binding.layoutDates.isVisible = isNegotiable
                // Lógica de animación ELIMINADA. Solo queda el cambio de visibilidad.
            }
        }

        // 2. Observar Categorías (para el Dropdown)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.categories.collect { categories ->
                val categoryNames = categories.map { it.nombre }
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, categoryNames)
                binding.actvCategory.setAdapter(adapter)
            }
        }

        // 3. Observar Estado de Carga
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.progressBarUpload.isVisible = isLoading
                binding.btnPublish.isEnabled = !isLoading
            }
        }

        // 4. Observar Resultado de Subida
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uploadStatus.collect { result ->
                result?.onSuccess {
                    showSuccessDialog()
                }?.onFailure { exception ->
                    Toast.makeText(context, "Error al publicar: ${exception.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        // 5. Observar imagen seleccionada
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.imageUri.collect { uri ->
                if (uri != null) {
                    binding.ivProductImage.setImageURI(Uri.parse(uri))
                    binding.ivProductImage.visibility = View.VISIBLE
                    binding.layoutAddImagePlaceholder.visibility = View.GONE
                } else {
                    binding.ivProductImage.visibility = View.GONE
                    binding.layoutAddImagePlaceholder.visibility = View.VISIBLE
                }
            }
        }
    }

    // --- Lógica de la Imagen (Requerimiento de Comprimir) ---

    private fun showImageSourceDialog() {
        val options = arrayOf("Galería", "Cámara")
        AlertDialog.Builder(requireContext())
            .setTitle("Seleccionar Imagen")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> openGallery()
                    1 -> Toast.makeText(context, "Implementar apertura de Cámara", Toast.LENGTH_SHORT).show() // TODO: Implementar cámara
                }
            }.show()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        imagePickerLauncher.launch(intent)
    }

    /**
     * Comprime la imagen seleccionada y la guarda en caché temporal como File.
     * Esta es la clave para cumplir el requisito de compresión.
     */
    private fun compressAndSaveImage(imageUri: Uri) {
        // En una app real, usarías una librería dedicada (ej. Luban, Compressor)
        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val inputStream = requireContext().contentResolver.openInputStream(imageUri)
                    val originalBytes = inputStream?.readBytes() ?: return@withContext

                    // ** SIMULACIÓN DE COMPRESIÓN (Simple y rápido, debe ser mejorado) **
                    val compressedBytes = originalBytes // Idealmente, aquí se reduciría el tamaño/calidad

                    // Guardar en caché
                    val file = File(requireContext().cacheDir, "product_upload_${System.currentTimeMillis()}.jpg")
                    FileOutputStream(file).use { outputStream ->
                        outputStream.write(compressedBytes)
                    }

                    compressedImageFile = file
                    viewModel.setImageUri(imageUri.toString())

                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Error al procesar la imagen: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    // --- Lógica del Calendario ---

    private fun showDatePicker(targetEditText: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val date = "$selectedDay/${selectedMonth + 1}/$selectedYear" // Formato simple
            when (targetEditText.id) {
                R.id.et_date_start -> binding.etDateStart.setText(date)
                R.id.et_date_end -> binding.etDateEnd.setText(date)
            }
        }, year, month, day)

        // No permitir fechas pasadas
        dpd.datePicker.minDate = System.currentTimeMillis() - 1000
        dpd.show()
    }

    // --- Lógica de Envío ---

    private fun collectAndSubmitData() {
        val selectedCategory = binding.actvCategory.text.toString()
        val category = viewModel.categories.value.find { it.nombre == selectedCategory }

        // Asegúrate de tener la clase ProductForm accesible
        val form = ProductForm(
            name = binding.etName.text.toString(),
            description = binding.etDescripcion.text.toString().ifEmpty { null },
            categoryId = category?.id ?: -1, // -1 si no se selecciona nada
            price = binding.etPrice.text.toString().toDoubleOrNull() ?: 0.0,
            stock = binding.etStock.text.toString().toIntOrNull() ?: 0,
            negotiable = binding.cbNegotiable.isChecked, // <--- ¡CORREGIDO! Usamos 'negotiable'
            dateStart = if (binding.cbNegotiable.isChecked) binding.etDateStart.text.toString().ifEmpty { null } else null,
            dateEnd = if (binding.cbNegotiable.isChecked) binding.etDateEnd.text.toString().ifEmpty { null } else null
        )

        viewModel.submitProduct(form, compressedImageFile)
    }

    // --- Feedback de Usuario (Requerimiento) ---

    private fun showSuccessDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("¡Éxito!")
            .setMessage("El producto se ha publicado correctamente en tu catálogo.")
            .setPositiveButton("Volver al Catálogo") { dialog, _ ->
                // TODO: Navegar a la pantalla del Catálogo del Negocio
                dialog.dismiss()
            }
            .setNegativeButton("Subir otro") { dialog, _ ->
                // Opcional: Limpiar formulario
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        // Limpiar el archivo temporal si existe
        compressedImageFile?.delete()
    }
}