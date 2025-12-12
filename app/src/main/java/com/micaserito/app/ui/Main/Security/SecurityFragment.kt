package com.micaserito.app.ui.Main.Security

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.micaserito.app.R
import com.micaserito.app.ui.Main.Security.ReportsAdapter

class SecurityFragment : Fragment(R.layout.fragment_security) {

    private lateinit var adapter: ReportsAdapter
    private var currentPage = 1
    private var isLoading = false
    private var isSentReports = true // true = Enviados, false = Recibidos

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar RecyclerView
        val rvReports = view.findViewById<RecyclerView>(R.id.rvReports)
        val layoutManager = LinearLayoutManager(context)
        rvReports.layoutManager = layoutManager
        adapter = ReportsAdapter()
        rvReports.adapter = adapter

        // Botones de filtro
        val btnSent = view.findViewById<Button>(R.id.btnSentReports)
        val btnReceived = view.findViewById<Button>(R.id.btnReceivedReports)

        btnSent.setOnClickListener {
            switchTab(true)
        }

        btnReceived.setOnClickListener {
            switchTab(false)
        }

        // Scroll Infinito
        rvReports.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(rv, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisiblePos = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && (visibleItemCount + firstVisiblePos) >= totalItemCount && firstVisiblePos >= 0) {
                    loadMoreData()
                }
            }
        })

        // Carga inicial
        loadData()
    }

    private fun switchTab(showSent: Boolean) {
        if (isSentReports == showSent) return // Ya estamos en esa pestaña
        isSentReports = showSent
        currentPage = 1
        adapter.clear()
        loadData()
    }

    private fun loadData() {
        isLoading = true
        // Aquí llamarías a tu API:
        // Si isSentReports es true -> GET /api/v1/reports/sent
        // Si isSentReports es false -> GET /api/v1/reports/received

        // Simulación:
        android.os.Handler().postDelayed({
            isLoading = false
            Toast.makeText(context, "Cargando página $currentPage...", Toast.LENGTH_SHORT).show()
            // adapter.addList(mockData)
        }, 1000)
    }

    private fun loadMoreData() {
        currentPage++
        loadData()
    }
}
