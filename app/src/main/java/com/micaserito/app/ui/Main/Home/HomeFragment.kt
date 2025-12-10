package com.micaserito.app.ui.Main.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.micaserito.app.R
import com.micaserito.app.data.api.MockData // Usamos tu data falsa
import com.micaserito.app.ui.Home.HomeFeedAdapter

class HomeFragment : Fragment() {
    //EJEMPLO BORRA TODO
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // 1. Buscar el RecyclerView del XML
        val recyclerView: RecyclerView = view.findViewById(R.id.rv_home_feed)

        // 2. Configurar cómo se ven los items (Lista vertical)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // 3. Obtener datos (Usando tu MockData para probar ya mismo)
        // Nota: getHomeFeed devuelve un objeto Response, sacamos los items de adentro
        val datosFalsos = MockData.getHomeFeed().data.sections?.flatMap { it.items } ?: emptyList()

        // 4. Crear el Adapter y dárselo al RecyclerView
        val adapter = HomeFeedAdapter(datosFalsos)
        recyclerView.adapter = adapter

        return view
    }
}