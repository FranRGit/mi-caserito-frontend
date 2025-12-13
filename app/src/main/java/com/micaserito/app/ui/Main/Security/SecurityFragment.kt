package com.micaserito.app.ui.Main.Security

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.micaserito.app.R
import com.micaserito.app.data.api.MockData

class SecurityFragment : Fragment(R.layout.fragment_security) {

    private lateinit var adapter: ReportsAdapter
    private lateinit var btnIncidencias: MaterialButton
    private lateinit var btnSanciones: MaterialButton
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

        btnIncidencias = view.findViewById(R.id.btnIncidencias)
        btnSanciones = view.findViewById(R.id.btnSanciones)

        btnIncidencias.setOnClickListener { switchTab(true) }
        btnSanciones.setOnClickListener { switchTab(false) }

        rvReports.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(rv, dx, dy)
                if (!isLoading && layoutManager.findLastCompletelyVisibleItemPosition() == adapter.itemCount - 1) {
                    // Lógica de paginación futura
                }
            }
        })

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
        val greenColor = Color.parseColor("#34C759")
        val whiteColor = Color.WHITE

        if (isIncidenciasSelected) {
            btnIncidencias.backgroundTintList = ColorStateList.valueOf(greenColor)
            btnIncidencias.setTextColor(whiteColor)
            btnSanciones.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
            btnSanciones.setTextColor(greenColor)
            btnSanciones.strokeColor = ColorStateList.valueOf(greenColor)
        } else {
            btnIncidencias.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
            btnIncidencias.setTextColor(greenColor)
            btnIncidencias.strokeColor = ColorStateList.valueOf(greenColor)
            btnSanciones.backgroundTintList = ColorStateList.valueOf(greenColor)
            btnSanciones.setTextColor(whiteColor)
        }
    }

    private fun loadData() {
        isLoading = true
        adapter.clear()

        android.os.Handler().postDelayed({
            val reportType = if (isIncidenciasSelected) "sent" else "received"
            // --- LLAMADA A MOCKDATA ---
            val reports = MockData.getReports(reportType)
            adapter.addList(reports)
            isLoading = false
        }, 500)
    }
}