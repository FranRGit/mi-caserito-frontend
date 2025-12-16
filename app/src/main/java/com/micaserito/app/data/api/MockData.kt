package com.micaserito.app.data.api

import com.micaserito.app.data.model.*

object MockData {

    // --- CONSTANTES DEL ECOSISTEMA ---
    private const val ID_CLIENTE = 1

    // Negocio 1: Bodega
    private const val ID_VENDEDOR_1 = 2
    private const val ID_NEGOCIO_1 = 100
    private const val NOMBRE_NEGOCIO_1 = "Bodega Don Pepe"
    private const val IMG_NEGOCIO_1 = "https://i.pravatar.cc/150?img=33"

    // Negocio 2: Ferretería (NUEVO)
    private const val ID_VENDEDOR_2 = 3
    private const val ID_NEGOCIO_2 = 200
    private const val NOMBRE_NEGOCIO_2 = "Ferretería El Clavo"
    private const val IMG_NEGOCIO_2 = "https://i.pravatar.cc/150?img=11"


    // Negocio 3: Tecnología y Servicio Técnico
    private const val ID_VENDEDOR_3 = 4
    private const val ID_NEGOCIO_3 = 300
    private const val NOMBRE_NEGOCIO_3 = "TecnoFix Express"
    private const val IMG_NEGOCIO_3 = "https://i.pravatar.cc/150?img=21"

    // Negocio 4: Farmacia
    private const val ID_VENDEDOR_4 = 5
    private const val ID_NEGOCIO_4 = 400
    private const val NOMBRE_NEGOCIO_4 = "Farmacia Salud Vida"
    private const val IMG_NEGOCIO_4 = "https://i.pravatar.cc/150?img=45"

    // Negocio 5: Restaurante
    private const val ID_VENDEDOR_5 = 6
    private const val ID_NEGOCIO_5 = 500
    private const val NOMBRE_NEGOCIO_5 = "Restaurante El Sabor"
    private const val IMG_NEGOCIO_5 = "https://i.pravatar.cc/150?img=12"

    // Negocio 6: Ropa y Moda
    private const val ID_VENDEDOR_6 = 7
    private const val ID_NEGOCIO_6 = 600
    private const val NOMBRE_NEGOCIO_6 = "Moda Urbana"
    private const val IMG_NEGOCIO_6 = "https://i.pravatar.cc/150?img=60"



    // --- BASE DE DATOS DE USUARIOS ---
    private val usuariosRegistrados = mutableListOf(
        User(ID_CLIENTE, "cliente@demo.com", "cliente", "123"),

        User(ID_VENDEDOR_1, "vendedor@demo.com", "vendedor", "123"),
        User(ID_VENDEDOR_2, "ferretero@demo.com", "vendedor", "123"),
        User(ID_VENDEDOR_3, "tecnico@demo.com", "vendedor", "123"),
        User(ID_VENDEDOR_4, "farmacia@demo.com", "vendedor", "123"),
        User(ID_VENDEDOR_5, "restaurante@demo.com", "vendedor", "123"),
        User(ID_VENDEDOR_6, "moda@demo.com", "vendedor", "123")
    )

    // Mapa para relacionar Vendedor -> Negocio
    private val vendedorToNegocioMap = mapOf(
        ID_VENDEDOR_1 to ID_NEGOCIO_1,
        ID_VENDEDOR_2 to ID_NEGOCIO_2,
        ID_VENDEDOR_3 to ID_NEGOCIO_3,
        ID_VENDEDOR_4 to ID_NEGOCIO_4,
        ID_VENDEDOR_5 to ID_NEGOCIO_5,
        ID_VENDEDOR_6 to ID_NEGOCIO_6
    )

    // Tiene valores por defecto (Vendedor 1) para no romper tus componentes antiguos
    fun getFakeSession(
        idUsuario: Int = ID_VENDEDOR_2,
        tipoUsuario: String = "vendedor"
    ): UserSessionData {
        return UserSessionData(
            idUsuario = idUsuario,
            tipoUsuario = tipoUsuario,
            token = "eyJ_TOKEN_SIMULADO_${idUsuario}_${tipoUsuario}"
        )
    }

    // AUTH: Busca el usuario y usa getFakeSession para generar la respuesta
    fun loginFake(email: String, password: String): UserSessionData? {
        val userFound = usuariosRegistrados.firstOrNull { it.email == email && it.password == password }

        return if (userFound != null) {
            // Aquí pasamos el ID y TIPO dinámicamente
            getFakeSession(userFound.id, userFound.tipoUsuario)
        } else {
            null
        }
    }

    // --- BASE DE DATOS DE ÍTEMS ---
    private val allItems = listOf(

        // === NEGOCIO 1: BODEGA (POST) ===
        FeedItem("post", ItemDetails(
            idPost = 1001,
            idNegocio = ID_NEGOCIO_1,
            nombreNegocio = NOMBRE_NEGOCIO_1,
            profileUrl = "https://zbkfgbomayjogdmquwua.supabase.co/storage/v1/object/public/Categoria_producto/bodega.jpg",
            fechaCreacion = "Hace 1 hora",
            descripcion = "Productos frescos todos los días 🥖🥛",
            imageUrl = "https://zbkfgbomayjogdmquwua.supabase.co/storage/v1/object/public/producto%20y%20negocio%20(categoria)/Pollo%20a%20la%20brasa.jpg",
            categoria = "Bodegas"
        )),
        FeedItem("product", ItemDetails(
            idProducto = 1,
            nombreProducto = "Pollo a la Brasa",
            precioBase = 55.90,
            nombreNegocio = NOMBRE_NEGOCIO_1,
            imageUrl = "https://zbkfgbomayjogdmquwua.supabase.co/storage/v1/object/public/producto%20y%20negocio%20(categoria)/Pollo%20a%20la%20brasa.jpg",
            categoria = "Alimentos",
            idNegocio = ID_NEGOCIO_1
        )),
        FeedItem("product", ItemDetails(
            idProducto = 2,
            nombreProducto = "Leche Gloria",
            precioBase = 4.20,
            nombreNegocio = NOMBRE_NEGOCIO_1,
            imageUrl = "https://zbkfgbomayjogdmquwua.supabase.co/storage/v1/object/public/producto%20y%20negocio%20(categoria)/Arroz%20blanco.webp",
            categoria = "Alimentos",
            idNegocio = ID_NEGOCIO_1
        )),

        // === NEGOCIO 2: FERRETERÍA (POST) ===
        FeedItem("post", ItemDetails(
            idPost = 1002,
            idNegocio = ID_NEGOCIO_2,
            nombreNegocio = NOMBRE_NEGOCIO_2,
            profileUrl = "https://zbkfgbomayjogdmquwua.supabase.co/storage/v1/object/public/Categoria_producto/ferreteria.jpg",
            fechaCreacion = "Hace 3 horas",
            descripcion = "Nuevas herramientas disponibles 🔧",
            imageUrl = "https://zbkfgbomayjogdmquwua.supabase.co/storage/v1/object/public/producto%20y%20negocio%20(categoria)/cargador%20de%20celular.jpg",
            categoria = "Ferreterías"
        )),
        FeedItem("product", ItemDetails(
            idProducto = 10,
            nombreProducto = "Taladro Percutor",
            precioBase = 150.00,
            nombreNegocio = NOMBRE_NEGOCIO_2,
            imageUrl = "https://zbkfgbomayjogdmquwua.supabase.co/storage/v1/object/public/producto%20y%20negocio%20(categoria)/cargador%20de%20celular.jpg",
            categoria = "Ferreterías",
            idNegocio = ID_NEGOCIO_2
        )),

        // === NEGOCIO 3: SUPERMERCADO (POST) ===
        FeedItem("post", ItemDetails(
            idPost = 1003,
            idNegocio = ID_NEGOCIO_3,
            nombreNegocio = "Supermercado La Colina",
            profileUrl = "https://zbkfgbomayjogdmquwua.supabase.co/storage/v1/object/public/Categoria_producto/supermercado.jpg",
            fechaCreacion = "Hace 5 horas",
            descripcion = "Ofertas en carnes y pescados 🥩🐟",
            imageUrl = "https://zbkfgbomayjogdmquwua.supabase.co/storage/v1/object/public/producto%20y%20negocio%20(categoria)/Chuleta%20de%20cerdo.webp",
            categoria = "Supermercado"
        )),
        FeedItem("product", ItemDetails(
            idProducto = 12,
            nombreProducto = "Atún en lata",
            precioBase = 6.50,
            nombreNegocio = "Supermercado La Colina",
            imageUrl = "https://zbkfgbomayjogdmquwua.supabase.co/storage/v1/object/public/producto%20y%20negocio%20(categoria)/Atun%20en%20lata.jpg",
            categoria = "Carnicería y Pescadería",
            idNegocio = ID_NEGOCIO_3
        )),

        // === NEGOCIO 4: RESTAURANTE (POST) ===
        FeedItem("post", ItemDetails(
            idPost = 1004,
            idNegocio = ID_NEGOCIO_4,
            nombreNegocio = "Restaurante El Sabor",
            profileUrl = "https://zbkfgbomayjogdmquwua.supabase.co/storage/v1/object/public/Categoria_producto/restaurante.jpg",
            fechaCreacion = "Hace 30 min",
            descripcion = "Menú especial del día 🍔",
            imageUrl = "https://zbkfgbomayjogdmquwua.supabase.co/storage/v1/object/public/producto%20y%20negocio%20(categoria)/Hamburguesa%20clasica.jpg",
            categoria = "Restaurante"
        )),
        FeedItem("product", ItemDetails(
            idProducto = 15,
            nombreProducto = "Hamburguesa clásica",
            precioBase = 15.00,
            nombreNegocio = "Restaurante El Sabor",
            imageUrl = "https://zbkfgbomayjogdmquwua.supabase.co/storage/v1/object/public/producto%20y%20negocio%20(categoria)/Hamburguesa%20clasica.jpg",
            categoria = "Restaurante",
            idNegocio = ID_NEGOCIO_4
        )),

        // === NEGOCIO 5: FARMACIA (POST) ===
        FeedItem("post", ItemDetails(
            idPost = 1005,
            idNegocio = ID_NEGOCIO_5,
            nombreNegocio = "Farmacia Santa Rosa",
            profileUrl = "https://zbkfgbomayjogdmquwua.supabase.co/storage/v1/object/public/Categoria_producto/Farmacia%20y%20Salud.jpg",
            fechaCreacion = "Hace 2 horas",
            descripcion = "Cuida tu salud 💊",
            imageUrl = "https://zbkfgbomayjogdmquwua.supabase.co/storage/v1/object/public/producto%20y%20negocio%20(categoria)/Gel%20antibacterial.jpg",
            categoria = "Farmacia y Salud"
        )),

        // === NEGOCIO 6: TECNOLOGÍA (POST) ===
        FeedItem("post", ItemDetails(
            idPost = 1006,
            idNegocio = ID_NEGOCIO_6,
            nombreNegocio = "TechRepair",
            profileUrl = "https://i.pravatar.cc/150?img=40",
            fechaCreacion = "Hace 1 día",
            descripcion = "Reparación de celulares 📱⚡",
            imageUrl = "https://zbkfgbomayjogdmquwua.supabase.co/storage/v1/object/public/producto%20y%20negocio%20(categoria)/Servicio%20de%20reparacion%20de%20celulares.jpg",
            categoria = "Tecnología y Servicio Técnico"
        ))
    )



    // --- HOME & DISCOVER ---
    fun getHomeFeed(): HomeFeedResponse {
        val products = allItems.filter { it.type == "product" }.take(5)
        val posts = allItems.filter { it.type == "post" }

        return HomeFeedResponse(
            "success",
            FeedData(sections = listOf(
                FeedSection("featured_products", "Ofertas Destacadas", products),
                FeedSection("latest_posts", "Novedades", posts)
            )),
            PaginationMeta(1, 1, false)
        )
    }

    fun getDiscoverResults(filter: String, categoryId: Int? = null, query: String = ""): HomeFeedResponse {
        var results = allItems

        results = when (filter) {
            "negocios" -> results.filter { it.type == "business" }
            "productos" -> results.filter { it.type == "product" }
            else -> results.filter { it.type != "post" }
        }

        if (categoryId != null) {
            val catName = getCategories().find { it.id == categoryId }?.nombre
            if (catName != null) results = results.filter { it.details.categoria == catName }
        }

        if (query.isNotEmpty()) {
            results = results.filter {
                it.details.nombreProducto?.contains(query, ignoreCase = true) == true ||
                        it.details.nombreNegocio?.contains(query, ignoreCase = true) == true
            }
        }
        return HomeFeedResponse("success", FeedData(items = results), PaginationMeta(1, 1, false))
    }

    // --- PERFIL DE NEGOCIO (Adaptado para 2 negocios) ---
    fun getNegocioProfile(idNegocio: Int): NegocioProfile {
        return if (idNegocio == ID_NEGOCIO_2) {
            NegocioProfile(
                idNegocio = ID_NEGOCIO_2,
                nombreNegocio = NOMBRE_NEGOCIO_2,
                descripcion = "Especialistas en construcción y acabados.",
                rubro = "Ferretería",
                profileUrl = IMG_NEGOCIO_2,
                calificacionPromedio = 4.5,
                ventasTotales = 120,
                horarioResumen = "Lun-Sab: 8am - 6pm"
            )
        } else {
            NegocioProfile(
                idNegocio = ID_NEGOCIO_1,
                nombreNegocio = NOMBRE_NEGOCIO_1,
                descripcion = "Bodega de confianza del barrio.",
                rubro = "Bodega",
                profileUrl = IMG_NEGOCIO_1,
                calificacionPromedio = 4.8,
                ventasTotales = 500,
                horarioResumen = "Lun-Dom: 7am - 11pm"
            )
        }
    }

    fun getBusinessProducts(negocioId: Int): List<ItemDetails> {
        return allItems
            .filter { it.type == "product" && it.details.idNegocio == negocioId }
            .map { it.details }
    }
    fun getBusinessPosts(negocioId: Int): List<ItemDetails> {
        return allItems
            .filter { it.type == "post" && it.details.idNegocio == negocioId }
            .map { it.details }
    }
    // --- UTILIDADES ---
    fun getCategories() = listOf(
        CategoriaNegocio(1, "Bodegas"),
        CategoriaNegocio(2, "Alimentos"),
        CategoriaNegocio(3, "Ferreterías")
    )

    fun getMockService() = MockApiService()

    // Auth Helpers (No cambiar)
    fun registrarUsuario(request: RegisterRequest) = true
    fun registrarNegocio(business: BusinessInfo) = true

    // Tickets (Solo ejemplo)
    fun getMyTickets() = listOf(TicketSummary(101, "ORD-8821", NOMBRE_NEGOCIO_1, 54.50, "pendiente", "2025-10-25"))
    fun addMyTicket(ticket: TicketSummary) {}
    fun getReports(tipo: String) = emptyList<ReportSummary>()
    fun getChatList() = listOf(ChatSummary(1, NOMBRE_NEGOCIO_1, IMG_NEGOCIO_1, "Hola", 0, "2025-10-25"))
    fun getChatMessages() = emptyList<ChatMessage>()

    // Helper extra por si necesitas saber qué negocio es de quién
    fun getBusinessIdByUserId(userId: Int): Int? = vendedorToNegocioMap[userId]
}