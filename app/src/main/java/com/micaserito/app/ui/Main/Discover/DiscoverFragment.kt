package com.micaserito.app.ui.Main.Discover

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.micaserito.app.data.api.MockData
import com.micaserito.app.data.model.CategoriaNegocio

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

        // --- Carga Inicial ---
        loadData()
    }

    private fun setupAdapters(view: View) {
        // Adapter del Feed Principal
        val rvFeed = view.findViewById<RecyclerView>(R.id.rvDiscoverFeed)
        rvFeed.layoutManager = LinearLayoutManager(requireContext())
        discoverAdapter = DiscoverAdapter()
        rvFeed.adapter = discoverAdapter

        // Adapter de Categorías
        val rvCategories = view.findViewById<RecyclerView>(R.id.rvCategories)
        rvCategories.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        
        categoriesAdapter = CategoriesAdapter(allCategories.map { it.nombre }) { selectedCategoryName ->
            // --- Lógica de Clic en Categoría ---
            currentCategoryId = allCategories.find { it.nombre == selectedCategoryName }?.id
            resetAndLoad()
        }
        rvCategories.adapter = categoriesAdapter
    }

    private fun setupUI(view: View) {
        // Listeners y demás...
        val searchBar = view.findViewById<EditText>(R.id.searchBar)
        searchBar.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchQuery = v.text.toString()
                currentCategoryId = null // Limpiar filtro de categoría al buscar
                resetAndLoad()
                true
            } else { false }
        }

        view.findViewById<ImageButton>(R.id.btnTickets).setOnClickListener {
            findNavController().navigate(R.id.nav_tickets)
        }

        view.findViewById<ChipGroup>(R.id.chipGroupFilter).setOnCheckedChangeListener { _, checkedId ->
            currentFilter = when (checkedId) {
                R.id.chipNegocios -> "business"
                R.id.chipProductos -> "product"
                else -> "todo"
            }
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
        view?.findViewById<View>(R.id.progressBarDiscover)?.visibility = View.VISIBLE

        Handler(Looper.getMainLooper()).postDelayed({
            isLoading = false
            view?.findViewById<View>(R.id.progressBarDiscover)?.visibility = View.GONE

            // --- LLAMADA A MOCKDATA CON FILTROS ---
            val response = MockData.getDiscoverResults(currentFilter, currentCategoryId)
            val items = response.data.items ?: emptyList()

            if (items.isNotEmpty()) {
                discoverAdapter.addList(items)
            } else {
                Toast.makeText(context, "No se encontraron resultados", Toast.LENGTH_SHORT).show()
            }
        }, 500)
    }
}