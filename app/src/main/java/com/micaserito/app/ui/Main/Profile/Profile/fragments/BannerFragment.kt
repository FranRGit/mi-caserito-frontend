package com.micaserito.app.ui.Main.Profile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.micaserito.app.R

class BannerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_banner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnBecomeSeller = view.findViewById<MaterialButton>(R.id.btnBecomeSeller)

        btnBecomeSeller.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Redirigiendo a registro de vendedor...",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
