package com.micaserito.app.ui.Main.Discover

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup
import com.micaserito.app.R
import com.micaserito.app.data.model.FeedItem
import com.micaserito.app.data.model.ItemDetails
import android.view.inputmethod.InputMethodManager
import android.content.Context


class DiscoverFragment : Fragment(R.layout.fragment_discover) {

    private lateinit var adapter: DiscoverAdapter
    private var isLoading = false
    private var currentPage = 1
    private var currentFilter = "todo" // "todo", "negocios", "productos"
    private var searchQuery = "" // Para la búsqueda

    companion object {
        // Definición de la clave de argumento para la búsqueda
        const val ARG_SEARCH_QUERY = "search_query" // Puedes usar cualquier string, pero "search_query" es claro
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Configurar RecyclerView
        val rvFeed = view.findViewById<RecyclerView>(R.id.rvDiscoverFeed)
        val layoutManager = LinearLayoutManager(requireContext())
        rvFeed.layoutManager = layoutManager
        adapter = DiscoverAdapter()
        rvFeed.adapter = adapter

        // 2. Configurar Barra de Búsqueda
        val searchBar = view.findViewById<EditText>(R.id.searchBar)

        // **************** CAMBIOS CLAVE AÑADIDOS ****************

        // A. Poner foco y abrir teclado al entrar al Fragmento
        searchBar.requestFocus()
        showKeyboard(searchBar)

        // B. Intentar recibir la búsqueda si se navegó desde Home
        arguments?.getString(ARG_SEARCH_QUERY)?.let { query ->
            if (query.isNotEmpty()) {
                searchBar.setText(query)
                searchQuery = query
                // Limpiar argumentos para que no se repita la búsqueda
                arguments?.remove(ARG_SEARCH_QUERY)
            }
        }

        // *******************************************************

        searchBar.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchQuery = v.text.toString()
                resetAndLoad()
                true
            } else {
                false
            }
        }

        // 3. Botón de Mis Tickets
        val btnTickets = view.findViewById<ImageButton>(R.id.btnTickets)
        btnTickets.setOnClickListener {
            findNavController().navigate(R.id.nav_tickets)
        }

        // 4. Configurar Chips (Filtros)
        val chipGroup = view.findViewById<ChipGroup>(R.id.chipGroupFilter)
        chipGroup.setOnCheckedChangeListener { _, checkedId ->
            currentFilter = when (checkedId) {
                R.id.chipNegocios -> "negocios"
                R.id.chipProductos -> "productos"
                else -> "todo"
            }
            resetAndLoad()
        }

        // 5. Infinite Scroll
        rvFeed.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    loadMoreData()
                }
            }
        })

        // Carga Inicial
        loadData()
    }
    // Función de utilidad para mostrar el teclado (añadida)
    private fun showKeyboard(view: View) {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
    private fun resetAndLoad() {
        currentPage = 1
        adapter.clear()
        loadData()
    }

    private fun loadData() {
        isLoading = true
        view?.findViewById<View>(R.id.progressBarDiscover)?.visibility = View.VISIBLE

        // SIMULACIÓN DE API CON DATOS DE EJEMPLO
        android.os.Handler().postDelayed({
            isLoading = false
            view?.findViewById<View>(R.id.progressBarDiscover)?.visibility = View.GONE

            val mockData = createMockData(currentPage, currentFilter, searchQuery)
            if (mockData.isNotEmpty()) {
                adapter.addList(mockData)
            } else {
                Toast.makeText(context, "No hay más resultados", Toast.LENGTH_SHORT).show()
            }
        }, 1200)
    }

    private fun loadMoreData() {
        currentPage++
        loadData()
    }

    // --- FUNCIÓN DE SIMULACIÓN (BORRAR AL CONECTAR API REAL) ---
    private fun createMockData(page: Int, filter: String, query: String): List<FeedItem> {
        if (page > 3) return emptyList() // Simular que no hay más páginas

        val items = mutableListOf<FeedItem>()

        // Si hay una búsqueda, mostrar solo un item de ejemplo
        if (query.isNotEmpty()) {
            items.add(FeedItem("product", ItemDetails(nombreProducto = "Resultado de '$query'", precioBase = 99.9, nombreNegocio = "Búsqueda Rápida")))
            return items
        }

        // Lógica de Filtros
        if (filter == "todo" || filter == "productos") {
            items.add(FeedItem("product", ItemDetails(nombreProducto = "Pollo a la brasa (Pág $page)", precioBase = 25.5, nombreNegocio = "Pardos Chicken")))
            items.add(FeedItem("product", ItemDetails(nombreProducto = "Lomo Saltado (Pág $page)", precioBase = 35.0, nombreNegocio = "Tanta")))
        }

        if (filter == "todo" || filter == "negocios") {
            items.add(FeedItem("post", ItemDetails(descripcion = "¡Nuevo local en Miraflores! (Pág $page)", fechaCreacion = "Hace 2 horas", nombreNegocio = "Starbucks")))
        }

        return items.shuffled() // Mezclar para que se vea más real
    }
}
