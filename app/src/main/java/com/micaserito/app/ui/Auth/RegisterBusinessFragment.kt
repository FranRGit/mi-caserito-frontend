package com.micaserito.app.ui.Auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.micaserito.app.R
import com.micaserito.app.databinding.FragmentRegisterBusinessBinding
import com.micaserito.app.ui.Map.MapPickerFragment

class RegisterBusinessFragment : Fragment() {

    private var _binding: FragmentRegisterBusinessBinding? = null
    private val binding get() = _binding!!

    private val args by lazy {
        RegisterBusinessFragmentArgs.fromBundle(requireArguments())
    }

    // Estado de ubicación del negocio
    private var businessLocationSelected = false
    private var businessLocationText: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBusinessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Mostrar / ocultar RUC según tipo de vendedor
        binding.layoutRuc.visibility =
            if (args.requireRuc) View.VISIBLE else View.GONE

        configurarSpinners()

        // 🔙 Volver
        binding.btnBackBusiness.setOnClickListener {
            findNavController().popBackStack()
        }

        // 📍 Seleccionar ubicación del negocio (MAPA)
        binding.btnSelectBusinessLocation.setOnClickListener {

            val dialog = MapPickerFragment { location ->

                //Se ejecuta SOLO cuando el usuario confirma ubicación
                businessLocationSelected = true
                businessLocationText = location

                binding.tvBusinessLocationSelected.text = location
                binding.tvBusinessLocationSelected.setTextColor(
                    resources.getColor(R.color.black, null)
                )

                Toast.makeText(
                    requireContext(),
                    "Ubicación del negocio registrada",
                    Toast.LENGTH_SHORT
                ).show()
            }

            dialog.show(parentFragmentManager, "BusinessMapPicker")
        }

        // Siguiente
        binding.btnBusinessNext.setOnClickListener {

            val nombre = binding.etBusinessName.text.toString().trim()
            val ruc = binding.etRuc.text.toString().trim()
            val categoria = binding.spBusinessCategory.selectedItem.toString()

            // Validaciones
            if (nombre.isEmpty()) {
                binding.etBusinessName.error = "Campo obligatorio"
                return@setOnClickListener
            }

            if (categoria == getString(R.string.seleccione)) {
                Toast.makeText(
                    requireContext(),
                    "Seleccione una categoría",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (!businessLocationSelected) {
                Toast.makeText(
                    requireContext(),
                    "Debe seleccionar la ubicación del negocio",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (args.requireRuc && ruc.isEmpty()) {
                binding.etRuc.error = "RUC obligatorio"
                return@setOnClickListener
            }

            // Aquí luego podrás guardar:
            // businessLocationText (dirección real / coordenadas)

            findNavController().navigate(
                RegisterBusinessFragmentDirections.actionBusinessToConfirm()
            )
        }
    }

    private fun configurarSpinners() {
        val categorias = listOf(
            getString(R.string.seleccione),
            "Bodega",
            "Verdulería",
            "Farmacia",
            "Restaurante",
            "Ferretería"
        )

        binding.spBusinessCategory.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            categorias
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
