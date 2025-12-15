package com.micaserito.app.ui.Main.Profile.Profile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.micaserito.app.R
import com.micaserito.app.ui.Main.Profile.ProfileViewModel
import com.micaserito.app.ui.Main.Profile.adapters.ProductListAdapter

class ProductListFragment : Fragment() {

    private val viewModel: ProfileViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductListAdapter
    private var idNegocio: Int = 0

    companion object {
        private const val ARG_ID_NEGOCIO = "id_negocio"

        fun newInstance(idNegocio: Int): ProductListFragment {
            val fragment = ProductListFragment()
            val args = Bundle()
            args.putInt(ARG_ID_NEGOCIO, idNegocio)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        idNegocio = arguments?.getInt(ARG_ID_NEGOCIO) ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_product_list, container, false)
        recyclerView = view.findViewById(R.id.rvProducts)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = GridLayoutManager(context, 2)
        adapter = ProductListAdapter(emptyList())
        recyclerView.adapter = adapter

        viewModel.products.observe(viewLifecycleOwner) { products ->
            adapter.updateData(products)
        }

        viewModel.loadProducts(idNegocio)
    }
}