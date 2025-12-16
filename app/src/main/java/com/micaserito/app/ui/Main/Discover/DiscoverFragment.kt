package com.micaserito.app.ui.Main.Discover

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup
import com.micaserito.app.R
import com.micaserito.app.data.api.MockData

class DiscoverFragment : Fragment(R.layout.fragment_discover) {

    private lateinit var discoverAdapter: DiscoverAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter
    private var isLoading = false
    private var currentPage = 1
    private var currentFilter = "todo" // todo, business, product
    private var currentCategoryId: Int? = null // ID de la categoría seleccionada
    private var searchQuery = ""
    private val allCategories = MockData.getCategories()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- Configuración de Listas ---
        setupAdapters(view)

        // --- Lógica de UI ---
        setupUI(view)

        // --- Lógica de Búsqueda Inicial ---
        // Revisar si se pasó una query desde HomeFragment u otro lado
        arguments?.getString("search_query")?.let { query ->
            if (query.isNotBlank()) {
                searchQuery = query
                Toast.makeText(context, "Búsqueda inicial: \"$searchQuery\"", Toast.LENGTH_LONG).show()
            }
        }

        // --- Carga Inicial ---
        loadData()
    }

    private fun setupAdapters(view: View) {
        // 1. Adapter del Feed Principal
        // ACTUALIZADO: ID cambiado para coincidir con el XML (rv_home_feed)
        val rvFeed = view.findViewById<RecyclerView>(R.id.rv_home_feed)
        rvFeed.layoutManager = LinearLayoutManager(requireContext())
        discoverAdapter = DiscoverAdapter()
        rvFeed.adapter = discoverAdapter

        // 2. Adapter de Categorías
        // ID correcto según el XML (rv_categories)
        val rvCategories = view.findViewById<RecyclerView>(R.id.rv_categories)
        rvCategories.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        categoriesAdapter = CategoriesAdapter(allCategories.map { it.nombre }) { selectedCategoryName ->
            // --- Lógica de Clic en Categoría ---
            currentCategoryId = allCategories.find { it.nombre == selectedCategoryName }?.id
            // Limpiar la búsqueda al aplicar un filtro de categoría
            searchQuery = ""
            resetAndLoad()
        }
        rvCategories.adapter = categoriesAdapter
    }

    private fun setupUI(view: View) {
        // Configuración del SearchBar (IDs coinciden con el include)
        val includeSearchBarView = view.findViewById<View>(R.id.include_search_bar)
        val searchBar = includeSearchBarView?.findViewById<EditText>(R.id.et_search_input)

        if (searchBar != null) {
            searchBar.setOnEditorActionListener { v, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchQuery = v.text.toString()
                    currentCategoryId = null // Limpiar filtro de categoría al buscar
                    resetAndLoad()
                    true
                } else { false }
            }
        }

        // ACTUALIZADO: IDs del ChipGroup y los Chips individuales
        val chipGroup = view.findViewById<ChipGroup>(R.id.cg_main_filters)
        chipGroup.setOnCheckedChangeListener { _, checkedId ->
            currentFilter = when (checkedId) {
                R.id.chip_negocios -> "business" // Coincide con XML
                R.id.chip_productos -> "product" // Coincide con XML
                R.id.chip_todo -> "todo"         // Coincide con XML
                else -> "todo" // Caso por defecto si no hay selección (aunque selectionRequired lo evita)
            }
            // Limpiar búsqueda y categoría al cambiar el filtro principal
            searchQuery = ""
            currentCategoryId = null
            resetAndLoad()
        }
    }

    private fun resetAndLoad() {
        currentPage = 1
        discoverAdapter.clear()
        loadData()
    }

    private fun loadData() {
        isLoading = true
        // ACTUALIZADO: ID del ProgressBar (progress_bar_loading)
        val progressBar = view?.findViewById<View>(R.id.progress_bar_loading)
        progressBar?.visibility = View.VISIBLE

        Handler(Looper.getMainLooper()).postDelayed({
            isLoading = false
            progressBar?.visibility = View.GONE

            // --- LLAMADA A MOCKDATA CON FILTROS ---
            // Asegúrate de que tu función MockData acepte estos parámetros
            val response = MockData.getDiscoverResults(currentFilter, currentCategoryId, searchQuery)
            val items = response.data.items ?: emptyList()

            if (items.isNotEmpty()) {
                discoverAdapter.addList(items)
            } else {
                // Opcional: Mostrar vista de "No resultados" si la lista está vacía
                Toast.makeText(context, "No se encontraron resultados", Toast.LENGTH_SHORT).show()
            }
        }, 500)
    }
}