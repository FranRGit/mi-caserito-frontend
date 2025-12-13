package com.micaserito.app.ui.Main.Post

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.micaserito.app.R
import com.micaserito.app.data.api.MockData
import com.micaserito.app.data.repository.UploadPostRepository
import com.micaserito.app.databinding.FragmentUploadPostBinding
import com.micaserito.app.ui.Viewholders.PostCardViewHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import com.micaserito.app.data.model.ItemDetails // Para la vista previa
import androidx.navigation.fragment.findNavController
class UploadPostFragment : Fragment(R.layout.fragment_upload_post) {

    private var _binding: FragmentUploadPostBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: UploadPostViewModel
    private var compressedImageFile: File? = null

    // El ViewHolder del Post para la vista previa
    private lateinit var postCardViewHolder: PostCardViewHolder

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
        _binding = FragmentUploadPostBinding.bind(view)

        // Inicializar ViewModel
        val repository = UploadPostRepository(MockData.getMockService())
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(UploadPostViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return UploadPostViewModel(repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
        viewModel = ViewModelProvider(this, factory).get(UploadPostViewModel::class.java)

        // Inicializar el PostCardViewHolder usando la vista incluida
        val postCardView = binding.includePostCard.root
        postCardViewHolder = PostCardViewHolder(postCardView)

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        // Entrada de texto
        binding.etDescripcion.doAfterTextChanged { editable ->
            viewModel.setDescription(editable.toString())
        }

        // Clic para subir imagen
        binding.flImageContainer.setOnClickListener {
            showImageSourceDialog()
        }

        // Botón Eliminar Imagen
        binding.btnRemoveImage.setOnClickListener {
            deleteCompressedImage()
            viewModel.clearImage()
        }

        // Botón Vista Previa (Alterna el modo)
        binding.btnPreview.setOnClickListener {
            if (binding.etDescripcion.text.isNullOrBlank()) {
                Toast.makeText(context, "Escribe el contenido para previsualizar.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.setPreviewMode(true)
        }

        // Botón Volver a Editar (Sale del modo Preview)
        binding.layoutPreview.setOnClickListener {
            viewModel.setPreviewMode(false)
        }

        // Botón Publicar
        binding.btnPublish.setOnClickListener {
            viewModel.submitPost(compressedImageFile)
        }
    }

    private fun observeViewModel() {
        // 1. Observar Modo Previsualización (Requisito Vital)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isPreviewMode.collect { isPreview ->
                binding.scrollForm.isVisible = !isPreview
                binding.layoutPreview.isVisible = isPreview

                if (isPreview) {
                    // Si estamos en modo Preview, renderizamos la Post Card
                    renderPostPreview()
                }
            }
        }

        // 2. Observar Imagen
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.imageUri.collect { uri ->
                val hasImage = uri != null
                binding.ivPostImage.isVisible = hasImage
                binding.btnRemoveImage.isVisible = hasImage
                binding.layoutAddImagePlaceholder.isVisible = !hasImage

                if (hasImage) {
                    binding.ivPostImage.setImageURI(Uri.parse(uri))
                }
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
    }

    /**
     * Lógica de Reutilización: Llenar el PostCardViewHolder con los datos actuales.
     */
    private fun renderPostPreview() {
        // Nota: Los datos de Negocio (nombre, profileUrl) deberían venir de la sesión del usuario.
        // Aquí usamos mock data para la simulación.

        val currentImageUri = viewModel.imageUri.value

        val postDetails = ItemDetails(
            descripcion = viewModel.description.value,
            // Simulamos datos del negocio del vendedor para la vista previa
            nombreNegocio = "Mi Caserito (Vendedor)",
            profileUrl = "https://i.pravatar.cc/150?img=10",
            imageUrl = currentImageUri
        )

        postCardViewHolder.render(postDetails)
    }

    private fun showImageSourceDialog() {
        val options = arrayOf("Galería", "Cámara")
        AlertDialog.Builder(requireContext())
            .setTitle("Seleccionar Imagen")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> openGallery()
                    1 -> Toast.makeText(context, "Implementar apertura de Cámara", Toast.LENGTH_SHORT).show()
                }
            }.show()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        imagePickerLauncher.launch(intent)
    }

    private fun compressAndSaveImage(imageUri: Uri) {
        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val inputStream = requireContext().contentResolver.openInputStream(imageUri)
                    val originalBytes = inputStream?.readBytes() ?: return@withContext

                    // SIMULACIÓN DE COMPRESIÓN
                    val compressedBytes = originalBytes

                    // Guardar en caché
                    val file = File(requireContext().cacheDir, "post_upload_${System.currentTimeMillis()}.jpg")
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

    private fun deleteCompressedImage() {
        compressedImageFile?.delete()
        compressedImageFile = null
    }
    //Resetea la UI y el estado del Post
    private fun resetForm() {
        // 1. Limpiar campos de texto
        binding.etDescripcion.text?.clear()

        // 2. Limpiar estado del ViewModel y el archivo temporal
        viewModel.clearImage() // Limpia el estado de la imagen
        deleteCompressedImage() // Borra el archivo del caché

        // 3. Asegurar que estamos en modo formulario (no preview)
        viewModel.setPreviewMode(false)
    }
    private fun showSuccessDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("¡Novedad Publicada!")
            .setMessage("Tu post se ha publicado correctamente en el feed.")
            .setPositiveButton("Volver al Home") { dialog, _ -> // Cambiamos el texto
                // Acción 1: Navegar de vuelta a la pantalla anterior
                dialog.dismiss()
                findNavController().popBackStack()
            }
            .setNegativeButton("Subir otro") { dialog, _ -> // Nuevo botón
                // Acción 2: Limpiar formulario y quedarse
                resetForm()
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        deleteCompressedImage() // Limpiar el archivo temporal al cerrar
    }
}