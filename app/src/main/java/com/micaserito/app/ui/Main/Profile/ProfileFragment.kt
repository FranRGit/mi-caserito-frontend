package com.micaserito.app.ui.Main.Profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.micaserito.app.R
import com.micaserito.app.data.api.MockData
import com.micaserito.app.ui.Main.Profile.adapters.ProfilePagerAdapter

class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()

    private lateinit var ivProfileImage: ImageView
    private lateinit var tvProfileName: TextView
    private lateinit var tvProfileDescription: TextView
    private lateinit var tvCategory: TextView
    private lateinit var tvRating: TextView
    private lateinit var llStats: LinearLayout
    private lateinit var tvSalesCount: TextView
    private lateinit var tvSchedule: TextView
    private lateinit var ivMenuOptions: ImageView
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    private var tipoUsuario: String = "cliente"
    private var idNegocio: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)

        val sessionData = MockData.getFakeSession()
        tipoUsuario = sessionData.tipoUsuario
        idNegocio = sessionData.idUsuario

        if (tipoUsuario == "vendedor") {
            setupVendedorProfile()
        } else {
            setupClienteProfile()
        }

        setupMenuOptions()
        observeViewModel()
    }

    private fun initViews(view: View) {
        ivProfileImage = view.findViewById(R.id.ivProfileImage)
        tvProfileName = view.findViewById(R.id.tvProfileName)
        tvProfileDescription = view.findViewById(R.id.tvProfileDescription)
        tvCategory = view.findViewById(R.id.tvCategory)
        tvRating = view.findViewById(R.id.tvRating)
        llStats = view.findViewById(R.id.llStats)
        tvSalesCount = view.findViewById(R.id.tvSalesCount)
        tvSchedule = view.findViewById(R.id.tvSchedule)
        ivMenuOptions = view.findViewById(R.id.ivMenuOptions)
        tabLayout = view.findViewById(R.id.tabLayout)
        viewPager = view.findViewById(R.id.viewPager)
    }

    private fun setupVendedorProfile() {
        llStats.visibility = View.VISIBLE
        viewModel.loadBusinessInfo(idNegocio)

        val adapter = ProfilePagerAdapter(this, isVendedor = true, idNegocio = idNegocio)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = if (position == 0) "Publicaciones" else "Catálogo"
        }.attach()
    }

    private fun setupClienteProfile() {
        llStats.visibility = View.GONE
        tvProfileName.text = "Usuario Cliente"
        tvProfileDescription.text = "Miembro de Mi Caserito"
        tvCategory.text = "Cliente"
        tvRating.text = "5.0"

        val adapter = ProfilePagerAdapter(this, isVendedor = false, idNegocio = idNegocio)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = if (position == 0) "Publicaciones" else "Premium"
        }.attach()
    }

    private fun setupMenuOptions() {
        ivMenuOptions.setOnClickListener {
            val popup = android.widget.PopupMenu(requireContext(), ivMenuOptions)
            popup.menuInflater.inflate(R.menu.profile_menu, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_tickets -> {
                        Toast.makeText(requireContext(), "Ir a Mis Tickets", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.menu_security -> {
                        Toast.makeText(requireContext(), "Ir a Centro de Seguridad", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.menu_logout -> {
                        Toast.makeText(requireContext(), "Cerrando sesión...", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }

            popup.show()
        }
    }

    private fun observeViewModel() {
        viewModel.businessInfo.observe(viewLifecycleOwner) { negocio ->
            tvProfileName.text = negocio.nombreNegocio
            tvProfileDescription.text = negocio.descripcion ?: ""
            tvCategory.text = negocio.rubro ?: "Negocio"
            tvRating.text = String.format("%.1f", negocio.calificacionPromedio)
            tvSalesCount.text = "${negocio.ventasTotales} Ventas Mensuales"
            tvSchedule.text = negocio.horarioResumen ?: "Lun-Dom"
        }
    }
}