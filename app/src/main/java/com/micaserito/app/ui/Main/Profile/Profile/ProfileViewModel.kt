package com.micaserito.app.ui.Main.Profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.micaserito.app.data.model.ItemDetails
import com.micaserito.app.data.model.NegocioProfile
import com.micaserito.app.data.network.NetworkModule
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _businessInfo = MutableLiveData<NegocioProfile>()
    val businessInfo: LiveData<NegocioProfile> = _businessInfo

    private val _products = MutableLiveData<List<ItemDetails>>()
    val products: LiveData<List<ItemDetails>> = _products

    private val _posts = MutableLiveData<List<ItemDetails>>()
    val posts: LiveData<List<ItemDetails>> = _posts

    fun loadBusinessInfo(idNegocio: Int) {
        viewModelScope.launch {
            try {
                val response = NetworkModule.apiService.getBusinessInfo(idNegocio)
                if (response.isSuccessful && response.body() != null) {
                    _businessInfo.value = response.body()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadProducts(idNegocio: Int) {
        viewModelScope.launch {
            try {
                val response = NetworkModule.apiService.getBusinessProducts(idNegocio, 1)
                if (response.isSuccessful && response.body() != null) {
                    _products.value = response.body()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadPosts(idNegocio: Int) {
        viewModelScope.launch {
            try {
                val response = NetworkModule.apiService.getBusinessPosts(idNegocio, 1)
                if (response.isSuccessful && response.body() != null) {
                    _posts.value = response.body()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}