package com.micaserito.app.ui.Auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.micaserito.app.databinding.FragmentRegisterConfirmBinding
import com.micaserito.app.ui.Main.MainActivity

class RegisterConfirmFragment : Fragment() {

    private var _binding: FragmentRegisterConfirmBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterConfirmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Flecha verde (retrocede correctamente)
        binding.btnBackConfirm.setOnClickListener {
            findNavController().popBackStack()
        }

        // Finalizar registro
        binding.btnFinish.setOnClickListener {

            val email = binding.etConfirmEmail.text.toString().trim()
            val password = binding.etConfirmPassword.text.toString().trim()
            val repeat = binding.etRepeatPassword.text.toString().trim()

            // VALIDACIONES
            if (email.isEmpty()) {
                binding.etConfirmEmail.error = "Ingrese su correo"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.etConfirmPassword.error = "Ingrese una contraseña"
                return@setOnClickListener
            }

            if (password.length < 6) {
                binding.etConfirmPassword.error = "Mínimo 6 caracteres"
                return@setOnClickListener
            }

            if (password != repeat) {
                binding.etRepeatPassword.error = "Las contraseñas no coinciden"
                return@setOnClickListener
            }

            // SIMULACIÓN DE REGISTRO EXITOSO
            // Aquí luego irá backend o MockData.registrarUsuario()

            Toast.makeText(
                requireContext(),
                "Registro completado correctamente",
                Toast.LENGTH_SHORT
            ).show()

            //IR DIRECTO A HOME (flujo correcto)
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)

            // Cerramos AuthActivity para que no regrese con BACK
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
