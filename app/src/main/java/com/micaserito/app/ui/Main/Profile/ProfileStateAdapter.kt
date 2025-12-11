package com.micaserito.app.ui.Main.Profile

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ProfileStateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2 // Tenemos 2 pestañas

    override fun createFragment(position: Int): Fragment {
        // Posición 0 = Publicaciones, Posición 1 = Productos
        return when (position) {
            0 -> PostListFragment()
            1 -> ProductListFragment()
            else -> PostListFragment()
        }
    }
}