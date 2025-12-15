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
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    // Instancia del repositorio
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

        // 🔐 LOGIN
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Usar lifecycleScope.launch para abrir un hilo secundario
            viewLifecycleOwner.lifecycleScope.launch {

                // (Opcional) Bloquear botón o mostrar ProgressBar
                binding.btnLogin.isEnabled = false
                binding.btnLogin.text = "Cargando..."

                // Llamada a la API (Espera aquí sin congelar la pantalla)
                val user = authRepository.login(email, password)

                // Reactivar botón
                binding.btnLogin.isEnabled = true
                binding.btnLogin.text = "Ingresar"

                if (user != null) {
                    // GUARDAR TOKEN, TIPO Y EL ID DEL USUARIO
                    SessionManager.saveSession(
                        requireContext(),
                        user.token,
                        user.tipoUsuario,
                        user.idUsuario // <-- ¡AGREGAR EL ID DEL USUARIO!
                    )

                    // IR AL HOME
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish() // Matar el Login para no volver atrás

                } else {
                    Toast.makeText(requireContext(), "Credenciales incorrectas o error de red", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // REGISTRO
        binding.btnGoRegister.setOnClickListener {
            // Asegúrate que esta acción exista en tu nav_graph
            val action = LoginFragmentDirections.actionLoginToRegister()
            findNavController().navigate(action)
        }

        // OLVIDÉ CONTRASEÑA
        binding.tvForgotPassword.setOnClickListener {
            // Asegúrate que esta acción exista en tu nav_graph
            val action = LoginFragmentDirections.actionLoginToForgot()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}