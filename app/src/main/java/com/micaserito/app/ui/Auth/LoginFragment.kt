package com.micaserito.app.ui.Auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.micaserito.app.data.Local.SessionManager
import com.micaserito.app.data.repository.AuthRepositoryImpl
import com.micaserito.app.databinding.FragmentLoginBinding
import com.micaserito.app.ui.Main.MainActivity

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    // Repositorio (usa MockData internamente)
    private val authRepository = AuthRepositoryImpl()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // LOGIN
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Complete todos los campos",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val user = authRepository.login(email, password)

            if (user != null) {

                // GUARDAR TOKEN Y TIPO LOCALMENTE
                SessionManager.saveSession(
                    requireContext(),
                    user.token,
                    user.tipoUsuario
                )

                // IR AL HOME
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()

            } else {
                Toast.makeText(
                    requireContext(),
                    "Credenciales incorrectas",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // REGISTRO
        binding.btnGoRegister.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginToRegister()
            findNavController().navigate(action)
        }

        // OLVIDÉ CONTRASEÑA
        binding.tvForgotPassword.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginToForgot()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
