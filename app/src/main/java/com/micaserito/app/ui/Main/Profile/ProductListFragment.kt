package com.micaserito.app.ui.Main.Profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.micaserito.app.R

class ProductListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Vincula con el XML que creaste en el paso 3.4
        return inflater.inflate(R.layout.fragment_product_list, container, false)
    }
}