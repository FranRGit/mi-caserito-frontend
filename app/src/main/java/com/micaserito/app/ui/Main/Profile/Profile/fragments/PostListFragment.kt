package com.micaserito.app.ui.Main.Profile.Profile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.micaserito.app.R
import com.micaserito.app.ui.Main.Profile.ProfileViewModel
import com.micaserito.app.ui.Main.Profile.adapters.PostListAdapter

class PostListFragment : Fragment() {

    private val viewModel: ProfileViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostListAdapter
    private var idNegocio: Int = 0

    companion object {
        private const val ARG_ID_NEGOCIO = "id_negocio"

        fun newInstance(idNegocio: Int): PostListFragment {
            val fragment = PostListFragment()
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
        val view = inflater.inflate(R.layout.fragment_post_list, container, false)
        recyclerView = view.findViewById(R.id.rvPosts)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = PostListAdapter(emptyList())
        recyclerView.adapter = adapter

        viewModel.posts.observe(viewLifecycleOwner) { posts ->
            adapter.updateData(posts)
        }

        viewModel.loadPosts(idNegocio)
    }
}