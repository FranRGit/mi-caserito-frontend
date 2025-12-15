package com.micaserito.app.ui.Map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.button.MaterialButton
import com.micaserito.app.R

class MapPickerFragment(
    private val onLocationConfirmed: (String) -> Unit
) : DialogFragment() {

    private var locationSelected = false
    private var selectedLocationText: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_map_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val webView = view.findViewById<WebView>(R.id.webViewMap)
        val btnConfirm = view.findViewById<MaterialButton>(R.id.btnConfirmLocation)

        val settings = webView.settings
        settings.javaScriptEnabled = true
        settings.allowFileAccess = true
        settings.allowContentAccess = true
        settings.domStorageEnabled = true
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

        webView.webViewClient = WebViewClient()
        webView.loadUrl("file:///android_asset/leaflet/map.html")

        // Simulación: el usuario tocó el mapa
        webView.setOnTouchListener { _, _ ->
            locationSelected = true
            selectedLocationText = "Ubicación seleccionada"
            false
        }

        // Confirmar ubicación
        btnConfirm.setOnClickListener {

            if (!locationSelected) {
                Toast.makeText(
                    requireContext(),
                    "Debe seleccionar una ubicación en el mapa",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            onLocationConfirmed(selectedLocationText!!)
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}
