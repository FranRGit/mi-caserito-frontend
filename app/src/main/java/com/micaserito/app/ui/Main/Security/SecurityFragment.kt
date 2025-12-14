package com.micaserito.app.ui.Main.Security

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.micaserito.app.R
import com.micaserito.app.data.api.MockData

class SecurityFragment : Fragment(R.layout.fragment_security) {

    private lateinit var adapter: ReportsAdapter
    private lateinit var btnIncidencias: TextView // <-- Corregido: de MaterialButton a TextView
    private lateinit var btnSanciones: TextView   // <-- Corregido: de MaterialButton a TextView
    private lateinit var layoutManager: LinearLayoutManager

    private var isLoading = false
    private var isIncidenciasSelected = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvReports = view.findViewById<RecyclerView>(R.id.rvReports)
        layoutManager = LinearLayoutManager(context)
        rvReports.layoutManager = layoutManager
        adapter = ReportsAdapter()
        rvReports.adapter = adapter

        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener { findNavController().popBackStack() }

        // Se buscan como TextView, no como MaterialButton
        btnIncidencias = view.findViewById(R.id.btnIncidencias)
        btnSanciones = view.findViewById(R.id.btnSanciones)

        btnIncidencias.setOnClickListener { switchTab(true) }
        btnSanciones.setOnClickListener { switchTab(false) }

        updateTabStyles()
        loadData()
    }

    private fun switchTab(selectIncidencias: Boolean) {
        if (isIncidenciasSelected == selectIncidencias) return
        isIncidenciasSelected = selectIncidencias
        updateTabStyles()
        loadData()
    }

    private fun updateTabStyles() {
        // Lógica adaptada para TextViews con fondos drawable
        if (isIncidenciasSelected) {
            btnIncidencias.setBackgroundResource(R.drawable.bg_tab_selected)
            btnIncidencias.setTextColor(Color.WHITE)

            btnSanciones.setBackgroundResource(R.drawable.bg_tab_unselected)
            btnSanciones.setTextColor(Color.BLACK)
        } else {
            btnIncidencias.setBackgroundResource(R.drawable.bg_tab_unselected)
            btnIncidencias.setTextColor(Color.BLACK)

            btnSanciones.setBackgroundResource(R.drawable.bg_tab_selected)
            btnSanciones.setTextColor(Color.WHITE)
        }
    }

    private fun loadData() {
        isLoading = true
        adapter.clear()

        val reportType = if (isIncidenciasSelected) "sent" else "received"
        val reports = MockData.getReports(reportType)
        adapter.addList(reports)
        isLoading = false
    }
}