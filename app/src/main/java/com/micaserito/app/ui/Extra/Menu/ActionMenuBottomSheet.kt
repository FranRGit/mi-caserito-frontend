package com.micaserito.app.ui.Extra.Menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.micaserito.app.R
import com.micaserito.app.databinding.FragmentActionMenuBinding // Usamos el Binding del XML
import com.micaserito.app.ui.Main.MainActivity // Importamos la Activity

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
        // Opcional: Estilo de BottomSheet para evitar esquinas redondeadas si es necesario
        // setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
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

        // La lógica para mostrar el menú de acciones de Vendedor
        if (isSeller) {
            setupSellerActions()
        } else {
            // Si es Comprador, simplemente se cierra el Bottom Sheet.
            dismiss()
            (activity as? MainActivity)?.navigateTo(R.id.nav_profile)
        }
    }

    private fun setupSellerActions() {
        // Clic en Publicar Producto
        binding.btnUploadProduct.setOnClickListener {
            (activity as? MainActivity)?.navigateTo(R.id.nav_upload_product)
            dismiss() // Cierra el menú flotante después de navegar
        }

        // Clic en Publicar Post/Novedad
        binding.btnUploadPost.setOnClickListener {
            (activity as? MainActivity)?.navigateTo(R.id.nav_upload_post)
            dismiss() // Cierra el menú flotante después de navegar
        }
    }

    // ... (Métodos onDestroyView y companion object siguen igual) ...

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