package com.micaserito.app.ui.Auth

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.micaserito.app.R
import com.micaserito.app.databinding.FragmentRegisterBinding
import java.io.File

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    // URIs de imágenes (frontend)
    private var dniImageUri: Uri? = null
    private var profileImageUri: Uri? = null
    private var isDni = true

    /* =======================
       GALERÍA
     ======================= */
    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                if (isDni) {
                    dniImageUri = it
                    binding.btnUploadDni.tag = "OK"
                    Toast.makeText(requireContext(), "DNI cargado desde galería", Toast.LENGTH_SHORT).show()
                } else {
                    profileImageUri = it
                    binding.btnUploadPhoto.tag = "OK"
                    Toast.makeText(requireContext(), "Foto cargada desde galería", Toast.LENGTH_SHORT).show()
                }
            }
        }

    /* =======================
       CÁMARA
     ======================= */
    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                if (isDni) {
                    binding.btnUploadDni.tag = "OK"
                    Toast.makeText(requireContext(), "DNI capturado con cámara", Toast.LENGTH_SHORT).show()
                } else {
                    binding.btnUploadPhoto.tag = "OK"
                    Toast.makeText(requireContext(), "Foto capturada con cámara", Toast.LENGTH_SHORT).show()
                }
            }
        }

    /* =======================
       PERMISO CÁMARA
     ======================= */
    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                abrirCamara()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permiso de cámara denegado",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cargarSpinners()

        // 📷 SUBIR DNI
        binding.btnUploadDni.setOnClickListener {
            isDni = true
            mostrarOpcionesImagen()
        }

        // 📷 SUBIR FOTO PERFIL
        binding.btnUploadPhoto.setOnClickListener {
            isDni = false
            mostrarOpcionesImagen()
        }

        // ➡️ SIGUIENTE
        binding.btnNext.setOnClickListener {
            if (!validarFormulario()) return@setOnClickListener

            val tipo = binding.spUserType.selectedItem.toString().lowercase()

            when (tipo) {
                "comprador" -> {
                    findNavController().navigate(
                        RegisterFragmentDirections.actionRegisterToConfirm()
                    )
                }
                "vendedor básico" -> {
                    findNavController().navigate(
                        RegisterFragmentDirections.actionRegisterToBusiness(false)
                    )
                }
                "vendedor verificado" -> {
                    findNavController().navigate(
                        RegisterFragmentDirections.actionRegisterToBusiness(true)
                    )
                }
            }
        }

        // 🔙 VOLVER
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    /* =======================
       DIÁLOGO OPCIONES
     ======================= */
    private fun mostrarOpcionesImagen() {
        val opciones = arrayOf("Tomar foto", "Elegir de galería")

        AlertDialog.Builder(requireContext())
            .setTitle("Seleccionar imagen")
            .setItems(opciones) { _, which ->
                when (which) {
                    0 -> verificarPermisoCamara()
                    1 -> abrirGaleria()
                }
            }
            .show()
    }

    private fun abrirGaleria() {
        galleryLauncher.launch("image/*")
    }

    private fun verificarPermisoCamara() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            abrirCamara()
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun abrirCamara() {
        val uri = crearUriImagen()
        if (isDni) {
            dniImageUri = uri
        } else {
            profileImageUri = uri
        }
        cameraLauncher.launch(uri)
    }

    private fun crearUriImagen(): Uri {
        val dir = File(requireContext().cacheDir, "images")
        if (!dir.exists()) dir.mkdirs()

        val file = File(dir, "img_${System.currentTimeMillis()}.jpg")

        return FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            file
        )
    }

    /* =======================
       SPINNERS
     ======================= */
    private fun cargarSpinners() {
        fun adapter(arrayId: Int) =
            ArrayAdapter.createFromResource(
                requireContext(),
                arrayId,
                android.R.layout.simple_spinner_dropdown_item
            )

        binding.spUserType.adapter = adapter(R.array.user_types)
        binding.spGender.adapter = adapter(R.array.genders)
        binding.spLocation.adapter = adapter(R.array.locations)
    }

    /* =======================
       VALIDACIONES
     ======================= */
    private fun validarFormulario(): Boolean {
        if (binding.etName.text.isNullOrBlank()) {
            binding.etName.error = "Campo obligatorio"
            return false
        }

        if (binding.etLastName.text.isNullOrBlank()) {
            binding.etLastName.error = "Campo obligatorio"
            return false
        }

        if (dniImageUri == null) {
            Toast.makeText(requireContext(), "Debe cargar DNI", Toast.LENGTH_SHORT).show()
            return false
        }

        if (profileImageUri == null) {
            Toast.makeText(requireContext(), "Debe cargar foto de perfil", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
