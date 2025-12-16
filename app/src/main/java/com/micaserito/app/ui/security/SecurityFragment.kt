package com.micaserito.app.ui.security

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.micaserito.app.R
import com.micaserito.app.data.api.MockData
import com.micaserito.app.ui.Main.Security.ReportsAdapter

class SecurityFragment : Fragment(R.layout.fragment_security) {

    private lateinit var adapter: ReportsAdapter
    private lateinit var btnIncidencias: TextView
    private lateinit var btnSanciones: TextView
    private lateinit var rvReports: RecyclerView

    private var isIncidenciasSelected = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Inicializar todas las vistas
        rvReports = view.findViewById(R.id.rvReports)
        btnIncidencias = view.findViewById(R.id.btnIncidencias)
        btnSanciones = view.findViewById(R.id.btnSanciones)
        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)

        // 2. Configurar el RecyclerView
        adapter = ReportsAdapter()
        rvReports.layoutManager = LinearLayoutManager(context)
        rvReports.adapter = adapter

        // 3. Configurar los listeners
        btnBack.setOnClickListener { findNavController().popBackStack() }
        btnIncidencias.setOnClickListener { switchTab(true) }
        btnSanciones.setOnClickListener { switchTab(false) }

        // 4. Cargar el estado inicial
        updateTabStyles()
        loadData()
    }

    private fun switchTab(selectIncidencias: Boolean) {
        if (isIncidenciasSelected == selectIncidencias) return // No hacer nada si ya está seleccionada
        
        isIncidenciasSelected = selectIncidencias
        updateTabStyles()
        loadData()
    }

    private fun updateTabStyles() {
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
        val reportType = if (isIncidenciasSelected) "sent" else "received"
        val reports = MockData.getReports(reportType)
        adapter.clear()
        adapter.addList(reports)
    }
}