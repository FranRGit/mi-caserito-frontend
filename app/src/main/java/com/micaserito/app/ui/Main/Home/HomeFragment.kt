package com.micaserito.app.ui.Main.Home

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo // NUEVO: Para manejar la tecla Enter
import android.view.inputmethod.InputMethodManager // NUEVO: Para controlar el teclado
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.micaserito.app.R
import com.micaserito.app.databinding.FragmentHomeBinding
import com.micaserito.app.data.api.MockData
import com.micaserito.app.ui.Main.MainActivity
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModel
import com.micaserito.app.data.repository.HomeRepository
// Nuevo import para la navegación
import androidx.navigation.fragment.findNavController

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
        val repository = HomeRepository(MockData.getMockService())

        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return HomeViewModel(repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }

        viewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)

        setupRecyclerView()
        setupSearchBarListener()
        observeViewModel()
    }

    private fun setupSearchBarListener() {
        val searchInput = binding.includeSearchBar.etSearchInput
        val searchCard = binding.includeSearchBar.cardSearchBar

        // SOLUCIÓN AL BUG VISUAL: Asegura que el campo esté vacío y el hint correcto.
        searchInput.setText("")
        // Aseguramos que el hint sea correcto (aunque ya está en el XML, esto es preventivo)
        searchInput.hint = "Buscar producto o negocio"

        // 1. Manejar el clic en la CardView (Redirecciona solo si hacen clic en la barra vacía)
        // El propósito de este listener es actuar como un atajo a Discover si la barra está vacía
        // o si el usuario quiere dejar de escribir.
        searchCard.setOnClickListener {
            // Restauramos el comportamiento: cualquier toque a la CardView redirige a Discover.
            handleNavigationToDiscover()
        }


        // 3. Manejar la acción 'Enter' (Buscar) - Lógica de redirección principal
        searchInput.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = v.text.toString()

                if (query.isNotBlank()) {
                    handleNavigationToDiscover(query)
                } else {
                    handleNavigationToDiscover()
                }

                // **SOLUCIÓN 2: OCULTAR TECLADO**
                // Forzamos el ocultamiento del teclado inmediatamente después de buscar.
                hideKeyboard(v)
                v.clearFocus()
                v.setText("")
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    // Función de utilidad para mostrar el teclado
    private fun showKeyboard(view: View) {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    // Función de utilidad para ocultar el teclado
    private fun hideKeyboard(view: View) {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }


    // MODIFICADO Y CORREGIDO: Usa findNavController() para pasar la query como argumento
    private fun handleNavigationToDiscover(query: String? = null) {

        // 1. Obtener el NavController
        val navController = findNavController()

        // 2. Crear Bundle si hay query
        val bundle = if (!query.isNullOrBlank()) {
            Bundle().apply {
                // "search_query" debe coincidir con el nombre esperado en DiscoverFragment
                putString("search_query", query)
            }
        } else {
            null
        }

        // 3. Navegar a Discover y pasar el Bundle
        if (navController.currentDestination?.id != R.id.nav_discover) {
            navController.navigate(R.id.nav_discover, bundle)
        } else if (bundle != null) {
            // Si ya estamos en Discover, a veces es necesario resetear los argumentos
            // o navegar de nuevo. En este caso, solo navegamos si no estamos en el destino,
            // pero si el usuario presiona Enter, la navegación con el Bundle fuerza la actualización.
            navController.navigate(R.id.nav_discover, bundle)
        }
    }

    private fun setupRecyclerView() {
        //... [código de setupRecyclerView sin cambios]
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