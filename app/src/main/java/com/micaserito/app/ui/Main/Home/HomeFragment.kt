package com.micaserito.app.ui.Main.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.micaserito.app.R

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 1. Inflar el diseño (Aquí pones tu XML antiguo)
        // Asegúrate de cambiar R.layout.fragment_home por el nombre de tu XML
        val view: View? = inflater.inflate(R.layout.fragment_home, container, false)

        // 2. Aquí pones tu lógica (FindViewById, ClickListeners, etc.)
        // Ejemplo:
        // TextView titulo = view.findViewById(R.id.miTitulo);
        return view
    }
}