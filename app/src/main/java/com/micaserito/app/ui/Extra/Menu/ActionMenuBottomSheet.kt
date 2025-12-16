package com.micaserito.app.ui.Extra.Menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.micaserito.app.R
import com.micaserito.app.databinding.FragmentActionMenuBinding
import com.micaserito.app.ui.Main.MainActivity

class ActionMenuBottomSheet : BottomSheetDialogFragment() {

    private var _binding: FragmentActionMenuBinding? = null
    // El binding ya está disponible porque el XML fragment_action_menu.xml ya existe
    private val binding get() = _binding!!

    private var isSeller = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isSeller = it.getBoolean(ARG_IS_SELLER, false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActionMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // LÓGICA CORREGIDA: Ambos roles acceden al menú, pero se configuran diferente.
        if (isSeller) {
            setupSellerActions()
        } else {
            setupCustomerActions() // El cliente accede a sus acciones compartidas
        }
    }

    /**
     * Configura las acciones para el Vendedor (Publicar Producto, Post, Tickets, Seguridad)
     */
    private fun setupSellerActions() {
        // Mostrar opciones de Publicación
        binding.btnUploadProduct.visibility = View.VISIBLE
        binding.btnUploadPost.visibility = View.VISIBLE

        // Clic en Publicar Producto
        binding.btnUploadProduct.setOnClickListener {
            (activity as? MainActivity)?.navigateTo(R.id.nav_upload_product)
            dismiss()
        }

        // Clic en Publicar Post/Novedad
        binding.btnUploadPost.setOnClickListener {
            (activity as? MainActivity)?.navigateTo(R.id.nav_upload_post)
            dismiss()
        }

        // Configuración de acciones compartidas (Tickets y Seguridad)
        setupSharedActions()
    }

    /**
     * Configura las acciones para el Cliente (Tickets, Seguridad)
     */
    private fun setupCustomerActions() {
        // Ocultar opciones de Publicación, exclusivas del vendedor
        binding.btnUploadProduct.visibility = View.GONE
        binding.btnUploadPost.visibility = View.GONE

        // Configuración de acciones compartidas (Tickets y Seguridad)
        setupSharedActions()
    }

    /**
     * Configura los listeners para las acciones disponibles para AMBOS roles (Tickets y Seguridad).
     */
    private fun setupSharedActions() {
        // Clic en Ir a Centro de Seguridad
        binding.btnGoToSecurity.setOnClickListener {
            (activity as? MainActivity)?.navigateTo(R.id.nav_security)
            dismiss()
        }

        // Clic en Mis Tickets
        binding.btnMyTickets.setOnClickListener {
            (activity as? MainActivity)?.navigateTo(R.id.nav_tickets)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "ActionMenuBottomSheet"
        const val ARG_IS_SELLER = "is_seller"

        fun newInstance(isSeller: Boolean) =
            ActionMenuBottomSheet().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_IS_SELLER, isSeller)
                }
            }
    }
}