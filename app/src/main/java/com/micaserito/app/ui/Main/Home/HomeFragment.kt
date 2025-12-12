package com.micaserito.app.ui.Main.Home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.micaserito.app.R
import com.micaserito.app.databinding.FragmentHomeBinding
import com.micaserito.app.data.api.MockData // Importación necesaria para el Repositorio
import com.micaserito.app.ui.Main.MainActivity // Necesario para la navegación
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModel // Necesario para la Factory anónima
import com.micaserito.app.data.repository.HomeRepository

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel
    private lateinit var feedAdapter: HomeFeedAdapter
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        // ** CÓDIGO SOLUCIONADO: Inicialización Manual del ViewModel **
        // Esto resuelve el error de feed vacío por falta de inicialización.

        // Asumiendo que MockData.getMockService() existe y devuelve tu ApiService
        val repository = HomeRepository(MockData.getMockService())

        // Crear una Factory anónima (en línea) para inyectar el Repositorio
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return HomeViewModel(repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }

        // Inicializar la variable 'viewModel'
        viewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)

        // El resto de la configuración
        setupRecyclerView()
        setupSearchBarListener()
        observeViewModel()
    }

    private fun setupSearchBarListener() {
        // Manejar el clic en la CardView que contiene el EditText
        binding.includeSearchBar.cardSearchBar.setOnClickListener {
            handleNavigationToDiscover()
        }

        // Manejar el focus para asegurar que el teclado no se quede abierto en Home
        binding.includeSearchBar.etSearchInput.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                handleNavigationToDiscover()
                // Quitar el foco inmediatamente para que el teclado se cierre
                v.clearFocus()
            }
        }
    }

    private fun handleNavigationToDiscover() {
        // Nos comunicamos con la Activity para que ella gestione el cambio de pestaña.
        val mainActivity = activity as? MainActivity

        if (mainActivity != null) {
            // Llama al método expuesto en MainActivity
            mainActivity.navigateTo(R.id.nav_discover)
        } else {
            Toast.makeText(context, "Error: No se puede acceder a la navegación principal", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() {
        feedAdapter = HomeFeedAdapter()

        // --- 1. CONFIGURACIÓN DEL FEED PRINCIPAL (Cuadrícula Flexible) ---
        binding.rvHomeFeed.apply {
            val spanCount = 2
            val gridLayoutManager = GridLayoutManager(context, spanCount)

            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    // La lógica del Header ya está incluida aquí
                    return when (feedAdapter.getItemViewType(position)) {
                        HomeFeedAdapter.TYPE_PRODUCT -> 1 // 1/2 Ancho
                        HomeFeedAdapter.TYPE_POST, HomeFeedAdapter.TYPE_BUSINESS, HomeFeedAdapter.TYPE_HEADER -> spanCount // Ancho Completo
                        else -> spanCount
                    }
                }
            }

            layoutManager = gridLayoutManager
            adapter = feedAdapter

            // Lógica de Paginación (OnScrollListener)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val currentLayoutManager = recyclerView.layoutManager as GridLayoutManager
                    val totalItemCount = currentLayoutManager.itemCount
                    val lastVisibleItemPosition = currentLayoutManager.findLastVisibleItemPosition()

                    val threshold = 5
                    val shouldLoadMore = lastVisibleItemPosition >= totalItemCount - threshold

                    if (shouldLoadMore && ::viewModel.isInitialized && !viewModel.isLoading.value) {
                        viewModel.loadNextPage()
                    }
                }
            })
        }

        // --- 2. CONFIGURACIÓN DEL RECYCLERVIEW DE CATEGORÍAS (Horizontal) ---
        binding.rvCategories.apply {
            // Carga de datos mock para Categories
            val mockCategories = MockData.getCategories() // Asume esta función existe en MockData

            // Definición del Listener de Clicks
            val categoryClickListener: OnCategoryClickListener = { category ->
                // Lógica que se ejecuta al hacer clic:
                Toast.makeText(context, "Filtro: ${category.nombre} (ID: ${category.id}). Redirigiendo a Discover.", Toast.LENGTH_SHORT).show()

                // Redirigir a Discover
                handleNavigationToDiscover()
            }

            // Inicializamos el Adapter pasándole la lista de datos Y el Listener de Clicks
            categoryAdapter = CategoryAdapter(mockCategories, categoryClickListener)

            // Usamos requireContext() para asegurar la inicialización del contexto
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = categoryAdapter
        }
    }

    private fun observeViewModel() {
        if (!::viewModel.isInitialized) return

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.feedData.collect { items ->
                feedAdapter.submitList(items)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.progressBarLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}