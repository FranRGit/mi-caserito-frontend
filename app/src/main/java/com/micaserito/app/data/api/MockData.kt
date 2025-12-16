package com.micaserito.app.data.api

import com.micaserito.app.data.model.*

object MockData {

    // --- CONSTANTES DEL ECOSISTEMA ---
    private const val ID_CLIENTE = 1

    // Negocio 1: Bodega
    private const val ID_VENDEDOR_1 = 2
    private const val ID_NEGOCIO_1 = 100
    private const val NOMBRE_NEGOCIO_1 = "Bodega Don Pepe"
    private const val IMG_NEGOCIO_1 = "https://i.pinimg.com/originals/c7/d8/28/c7d828551b87693525283839d3326532.gif"

    // Negocio 2: Ferretería (NUEVO)
    private const val ID_VENDEDOR_2 = 3
    private const val ID_NEGOCIO_2 = 200
    private const val NOMBRE_NEGOCIO_2 = "Ferretería El Clavo"
    private const val IMG_NEGOCIO_2 = "https://www.pngall.com/wp-content/uploads/2/Nail-PNG-Image.png"

    // --- BASE DE DATOS DE USUARIOS ---
    private val usuariosRegistrados = mutableListOf<User>(
        User(ID_CLIENTE, "cliente@demo.com", "cliente", "123"),
        User(ID_VENDEDOR_1, "vendedor@demo.com", "vendedor", "123"), // Dueño Bodega
        User(ID_VENDEDOR_2, "ferretero@demo.com", "vendedor", "123")  // Dueño Ferretería
    )

    // Mapa para relacionar Vendedor -> Negocio
    private val vendedorToNegocioMap = mapOf(
        ID_VENDEDOR_1 to ID_NEGOCIO_1,
        ID_VENDEDOR_2 to ID_NEGOCIO_2
    )

    // Tiene valores por defecto (Vendedor 1) para no romper tus componentes antiguos
    fun getFakeSession(
        idUsuario: Int = ID_VENDEDOR_1,
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
        // === NEGOCIO 1: BODEGA ===
        FeedItem("business", ItemDetails(idNegocio = ID_NEGOCIO_1, nombreNegocio = NOMBRE_NEGOCIO_1, calificacionPromedio = 4.8, direccion = "Av. Principal 123", profileUrl = IMG_NEGOCIO_1, categoria = "Bodegas")),
        FeedItem("product", ItemDetails(idProducto = 1, nombreProducto = "Pollo a la Brasa", precioBase = 55.90, nombreNegocio = NOMBRE_NEGOCIO_1, imageUrl = "https://i.imgur.com/iUzGAnp.jpg", categoria = "Alimentos", idNegocio = ID_NEGOCIO_1)),
        FeedItem("product", ItemDetails(idProducto = 2, nombreProducto = "Leche Gloria", precioBase = 4.20, nombreNegocio = NOMBRE_NEGOCIO_1, imageUrl = "https://i.imgur.com/8u1D5sR.jpg", categoria = "Alimentos", idNegocio = ID_NEGOCIO_1)),
        FeedItem("post", ItemDetails(idPost = 501, nombreNegocio = NOMBRE_NEGOCIO_1, profileUrl = IMG_NEGOCIO_1, fechaCreacion = "hace 2 horas", descripcion = "¡Pan caliente! 🥖", imageUrl = "https://i.imgur.com/L8a1qj9.jpg", idNegocio = ID_NEGOCIO_1)),

        // === NEGOCIO 2: FERRETERÍA (NUEVO) ===
        FeedItem("business", ItemDetails(idNegocio = ID_NEGOCIO_2, nombreNegocio = NOMBRE_NEGOCIO_2, calificacionPromedio = 4.5, direccion = "Jr. Herramientas 456", profileUrl = IMG_NEGOCIO_2, categoria = "Ferreterías")),
        FeedItem("product", ItemDetails(idProducto = 10, nombreProducto = "Taladro Percutor", precioBase = 150.00, nombreNegocio = NOMBRE_NEGOCIO_2, imageUrl = "https://i.imgur.com/4z2zL9E.jpg", categoria = "Ferreterías", idNegocio = ID_NEGOCIO_2)),
        FeedItem("product", ItemDetails(idProducto = 11, nombreProducto = "Martillo", precioBase = 25.00, nombreNegocio = NOMBRE_NEGOCIO_2, imageUrl = "https://i.imgur.com/9aA2N6V.jpg", categoria = "Ferreterías", idNegocio = ID_NEGOCIO_2)),
        FeedItem("post", ItemDetails(idPost = 502, nombreNegocio = NOMBRE_NEGOCIO_2, profileUrl = IMG_NEGOCIO_2, fechaCreacion = "hace 5 horas", descripcion = "Oferta en pinturas 🎨", imageUrl = "https://i.imgur.com/sC050R4.jpg", idNegocio = ID_NEGOCIO_2))
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
    
    // --- CENTRO DE SEGURIDAD ---
    private val sentReports = listOf(
        ReportSummary(1, "Retraso", "Producto no entregado", NOMBRE_NEGOCIO_1, "En revisión", "2024-05-20"),
        ReportSummary(2, "Estafa", "Comentarios inapropiados", "Usuario 'Molesto256'", "Pendiente", "2024-05-18"),
        ReportSummary(3, "Entrega", "Se realizo la entrega con exito", "Usuario 'Feliz123'", "Resuelta", "2024-05-01"),
        ReportSummary(4, "Problema", "Falta un componente", "Usuario 'Molesto123'", "Rechazada", "2024-05-12"),
        ReportSummary(5, "Problema", "Producto equivocado", "Usuario 'Molesto124'", "En revisión", "2024-12-24")
    )

    private val receivedReports = listOf(
        ReportSummary(101, "Sanción", "Advertencia por entrega tardía", "Sistema Mi Caserito", "Activa", "2024-05-15"),
        ReportSummary(102, "Advertencia", "Suspensión temporal (48h)", "Sistema Mi Caserito", "Expirada", "2024-04-22"),
        ReportSummary(103, "Falta", "Suspensión temporal (5h)", "Sistema Mi Caserito", "Activa", "2024-04-22")
    )

    fun getReports(tipo: String): List<ReportSummary> {
        return when (tipo) {
            "sent" -> sentReports
            "received" -> receivedReports
            else -> emptyList()
        }
    }

    fun getChatList() = listOf(ChatSummary(1, NOMBRE_NEGOCIO_1, IMG_NEGOCIO_1, "Hola", 0, "2025-10-25"))
    fun getChatMessages() = emptyList<ChatMessage>()

    // Helper extra por si necesitas saber qué negocio es de quién
    fun getBusinessIdByUserId(userId: Int): Int? = vendedorToNegocioMap[userId]
}