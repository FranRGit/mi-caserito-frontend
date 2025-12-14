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

class DiscoverFragment : Fragment(R.layout.fragment_discover) {

    private lateinit var adapter: DiscoverAdapter
    private var isLoading = false
    private var currentPage = 1
    private var currentFilter = "todo" // "todo", "negocios", "productos"
    private var searchQuery = "" // Para la búsqueda

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Configurar RecyclerView
        val rvFeed = view.findViewById<RecyclerView>(R.id.rvDiscoverFeed)
        val layoutManager = LinearLayoutManager(requireContext())
        rvFeed.layoutManager = layoutManager
        adapter = DiscoverAdapter()
        rvFeed.adapter = adapter

        // **********************************************
        // INICIO DE CAMBIOS PARA BUSCADOR RECICLADO
        // **********************************************

        // 2. RECEPCIÓN DE LA QUERY DE BÚSQUEDA DEL HOME/NAVEGACIÓN
        arguments?.getString("search_query")?.let { query ->
            // Si hay una query válida (no en blanco), la aplicamos.
            if (query.isNotBlank()) {
                searchQuery = query
                // Muestra un Toast para confirmar que la búsqueda se aplicó
                Toast.makeText(context, "Búsqueda inicial: \"$searchQuery\"", Toast.LENGTH_LONG).show()
            }
        }

        // 2. Configurar Barra de Búsqueda
        // SE ELIMINA LA LÓGICA DEL EDITTEXT DUPLICADO (R.id.searchBar)
        /*
        val searchBar = view.findViewById<EditText>(R.id.searchBar)
        searchBar.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchQuery = v.text.toString()
                resetAndLoad()
                true
            } else {
                false
            }
        }
        */

        // 3. Botón de Mis Tickets
        // Se mantiene la inicialización de btnTickets
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
            // AL CAMBIAR EL FILTRO, DEBEMOS RESETEAR LA BÚSQUEDA INICIAL
            searchQuery = ""
            resetAndLoad()
        }

        // **********************************************
        // FIN DE CAMBIOS PARA BUSCADOR RECICLADO
        // **********************************************

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

            // loadData ahora utiliza el searchQuery actualizado.
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