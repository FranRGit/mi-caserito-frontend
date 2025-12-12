package com.micaserito.app.ui.Main.Home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.micaserito.app.data.model.FeedItem
import com.micaserito.app.data.repository.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    // El Repositorio debe ser inyectado, no creado aquí
    private val repository: HomeRepository
) : ViewModel() {

    // Lista de items del feed que la UI observará (LiveData o StateFlow)
    private val _feedData = MutableStateFlow<List<FeedItem>>(emptyList())
    val feedData: StateFlow<List<FeedItem>> = _feedData

    // Indicador de si estamos cargando actualmente (para evitar peticiones duplicadas)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var currentPage = 1
    private var isLastPage = false

    init {
        loadNextPage() // Carga inicial al crear el ViewModel
    }

    /**
     * Dispara la carga de la siguiente página del feed.
     */
    fun loadNextPage() {
        // Evitar cargar si ya estamos cargando o si ya no hay más páginas
        if (_isLoading.value || isLastPage) return

        viewModelScope.launch {
            _isLoading.value = true // Inicia el estado de carga

            val result = repository.fetchHomeFeedPage(currentPage)

            result.onSuccess { response ->
                // 1. Aplanar las secciones y obtener la lista de items
                val newItems = repository.extractFeedItems(response)

                // 2. Concatenar los items nuevos a la lista actual
                _feedData.value = _feedData.value + newItems

                // 3. Actualizar el estado de paginación
                val meta = response.pagination
                isLastPage = !meta.hasMore // La API indica si hay más
                if (!isLastPage) {
                    currentPage++
                }

                _isLoading.value = false
            }.onFailure {
                // Manejar error (ej. mostrar un Toast en el Fragment)
                _isLoading.value = false
                // Aquí podrías agregar lógica para reintentar o notificar a la UI
            }
        }
    }

    /**
     * Reinicia la paginación para un Pull-to-Refresh.
     */
    fun refreshFeed() {
        if (_isLoading.value) return
        currentPage = 1
        isLastPage = false
        _feedData.value = emptyList() // Limpiar la UI antes de recargar
        loadNextPage()
    }
}