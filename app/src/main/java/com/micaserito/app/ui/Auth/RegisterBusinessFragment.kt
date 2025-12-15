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

class RegisterBusinessFragment : Fragment() {

    private var _binding: FragmentRegisterBusinessBinding? = null
    private val binding get() = _binding!!

    private val args by lazy {
        RegisterBusinessFragmentArgs.fromBundle(requireArguments())
    }

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

        // 🔁 Mostrar / ocultar RUC
        binding.layoutRuc.visibility =
            if (args.requireRuc) View.VISIBLE else View.GONE

        configurarSpinners()

        // 🔙 Flecha verde (volver)
        binding.btnBackBusiness.setOnClickListener {
            findNavController().popBackStack()
        }

        // ➡️ Siguiente
        binding.btnBusinessNext.setOnClickListener {

            val nombre = binding.etBusinessName.text.toString().trim()
            val ruc = binding.etRuc.text.toString().trim()

            val categoria = binding.spBusinessCategory.selectedItem.toString()
            val direccion = binding.spBusinessAddress.selectedItem.toString()

            // VALIDACIONES
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

            if (direccion == getString(R.string.seleccione)) {
                Toast.makeText(
                    requireContext(),
                    "Seleccione una dirección",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (args.requireRuc && ruc.isEmpty()) {
                binding.etRuc.error = "RUC obligatorio"
                return@setOnClickListener
            }

            // TODO: Guardar datos (Mock / ViewModel)

            val action =
                RegisterBusinessFragmentDirections.actionBusinessToConfirm()
            findNavController().navigate(action)
        }
    }

    private fun configurarSpinners() {

        // Categorías
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

        // Direcciones (mock)
        val direcciones = listOf(
            getString(R.string.seleccione),
            "Lima",
            "Callao",
            "Arequipa",
            "Cusco",
            "Trujillo"
        )

        binding.spBusinessAddress.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            direcciones
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
