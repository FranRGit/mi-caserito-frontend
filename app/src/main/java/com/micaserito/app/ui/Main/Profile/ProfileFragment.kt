package com.micaserito.app.ui.Main.Profile
import com.micaserito.app.data.Local.SessionManager
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

        // 🚨 CAMBIO CLAVE: Usar SessionManager para obtener los datos REALES
        val currentUserId = SessionManager.getUserId(requireContext())
        tipoUsuario = SessionManager.getUserType(requireContext())

        // 🚨 LÓGICA DE MAPEO (el mapeo aún usa MockData para la lógica del ID de negocio)
        if (tipoUsuario == "vendedor") {
            // Si es vendedor, usamos el ID del usuario actual para encontrar el ID de su negocio asociado.
            val negocioId = MockData.getBusinessIdByUserId(currentUserId)
            idNegocio = negocioId ?: 0

            if (idNegocio == 0) {
                Toast.makeText(requireContext(), "Error: ID de negocio no encontrado para el vendedor.", Toast.LENGTH_LONG).show()
            }

            setupVendedorProfile()
        } else {
            // Si es cliente, usamos su ID de usuario.
            idNegocio = currentUserId
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

        // El idNegocio ya tiene el valor correcto (ej. 200)
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

        // El idNegocio ahora es el idUsuario, pero como no se usa para cargar publicaciones
        // de un cliente en este flujo, no afecta el comportamiento.
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