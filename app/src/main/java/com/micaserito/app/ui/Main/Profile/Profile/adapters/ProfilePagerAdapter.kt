package com.micaserito.app.ui.Main.Profile.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.micaserito.app.ui.Main.Profile.fragments.BannerFragment
import com.micaserito.app.ui.Main.Profile.Profile.fragments.PostListFragment
import com.micaserito.app.ui.Main.Profile.Profile.fragments.ProductListFragment

class ProfilePagerAdapter(
    fragment: Fragment,
    private val isVendedor: Boolean,
    private val idNegocio: Int
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PostListFragment.newInstance(idNegocio)
            1 -> {
                if (isVendedor) {
                    ProductListFragment.newInstance(idNegocio)
                } else {
                    BannerFragment()
                }
            }
            else -> throw IllegalStateException("Posición inválida")
        }
    }
}