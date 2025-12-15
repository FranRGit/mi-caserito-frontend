package com.micaserito.app.ui.Auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.micaserito.app.databinding.FragmentForgotPasswordBinding

class ForgotPasswordFragment : Fragment() {

    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Botón atrás
        binding.btnBackForgot.setOnClickListener {
            findNavController().popBackStack()
        }

        // Botón para obtener OTP
        binding.btnGetOtp.setOnClickListener {
            val email = binding.etForgotEmail.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(requireContext(), "Ingrese su correo electrónico", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Aquí puedes llamar a tu API real o Mock
            Toast.makeText(requireContext(), "OTP enviado a $email", Toast.LENGTH_LONG).show()

            // Opcional: volver al Login
            // findNavController().navigate(R.id.loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
