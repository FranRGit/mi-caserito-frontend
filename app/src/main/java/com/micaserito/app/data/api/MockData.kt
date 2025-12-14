package com.micaserito.app.data.api

import com.micaserito.app.data.model.CategoriaNegocio
import com.micaserito.app.data.model.ChatMessage
import com.micaserito.app.data.model.ChatSummary
import com.micaserito.app.data.model.FeedData
import com.micaserito.app.data.model.FeedItem
import com.micaserito.app.data.model.FeedSection
import com.micaserito.app.data.model.HomeFeedResponse
import com.micaserito.app.data.model.ItemDetails
import com.micaserito.app.data.model.NegocioProfile
import com.micaserito.app.data.model.PaginationMeta
import com.micaserito.app.data.model.ReportSummary
import com.micaserito.app.data.model.TicketSummary
import com.micaserito.app.data.model.UserSessionData


object MockData {

    // --- Base de Datos en Memoria ---

    // 1. Lista de TODOS los ítems (Con idNegocio añadido para la lógica de permisos)
    private val allItems = listOf(
        // Alimentos (idNegocio: 10 = Dueño, 20 = Otro)
        FeedItem("product", ItemDetails(idProducto = 1, nombreProducto = "Pollo a la Brasa", precioBase = 55.90, nombreNegocio = "Pardos Chicken", ciudad = "Miraflores", imageUrl = "https://i.imgur.com/iUzGAnp.jpg", categoria = "Alimentos", idNegocio = 10)),
        FeedItem("product", ItemDetails(idProducto = 2, nombreProducto = "Leche Gloria (lata)", precioBase = 4.20, nombreNegocio = "Bodega El Chino", ciudad = "Surquillo", imageUrl = "https://i.imgur.com/8u1D5sR.jpg", categoria = "Alimentos", idNegocio = 20)),
        FeedItem("business", ItemDetails(idNegocio = 101, nombreNegocio = "Verdulería Doña Juana", calificacionPromedio = 4.8, direccion = "Mercado N°2, Surco", profileUrl = "https://i.pravatar.cc/150?img=1", categoria = "Alimentos")),
        // Ropa
        FeedItem("product", ItemDetails(idProducto = 3, nombreProducto = "Polo Básico de Algodón", precioBase = 39.90, nombreNegocio = "Gamarra Express", ciudad = "La Victoria", imageUrl = "https://i.imgur.com/9aA2N6V.jpg", categoria = "Ropa", idNegocio = 10)),
        FeedItem("business", ItemDetails(idNegocio = 102, nombreNegocio = "Boutique Elegancia", calificacionPromedio = 4.9, direccion = "C.C. Jockey Plaza", profileUrl = "https://i.pravatar.cc/150?img=2", categoria = "Ropa")),
        // Campo (Ferretería)
        FeedItem("product", ItemDetails(idProducto = 4, nombreProducto = "Martillo de Uña", precioBase = 25.00, nombreNegocio = "Ferretería El Tornillo", ciudad = "Independencia", imageUrl = "https://i.imgur.com/4z2zL9E.jpg", categoria = "Campo", idNegocio = 20)),
        FeedItem("business", ItemDetails(idNegocio = 103, nombreNegocio = "Sodimac", calificacionPromedio = 4.5, direccion = "Av. Angamos", profileUrl = "https://i.pravatar.cc/150?img=3", categoria = "Campo")),
        // Belleza
        FeedItem("product", ItemDetails(idProducto = 5, nombreProducto = "Protector Solar SPF 50", precioBase = 60.00, nombreNegocio = "Farmacia MiFarma", ciudad = "San Isidro", imageUrl = "https://i.imgur.com/sC050R4.jpg", categoria = "Belleza")),
        // Hogar
        FeedItem("product", ItemDetails(idProducto = 6, nombreProducto = "Juego de Sábanas 2 Plazas", precioBase = 120.00, nombreNegocio = "CasaIdeas", ciudad = "Lince", imageUrl = "https://i.imgur.com/v2Jv27J.jpg", categoria = "Hogar")),
        // Post
        FeedItem("post", ItemDetails(idPost = 501, nombreNegocio = "Panadería El Buen Sabor", profileUrl = "https://i.pravatar.cc/150?img=12", fechaCreacion = "hace 2 horas", descripcion = "¡Salieron los panes calientes! 🥖🥖", imageUrl = "https://i.imgur.com/L8a1qj9.jpg"))
    )

    // 2. Datos para reportes (Consolidado de ambas listas)
    private val sentReports = listOf(
        ReportSummary(1, "Problema", "El producto llegó roto", "Contra: Bodega Mala Fama", "Pendiente", "12 Oct 2025"),
        ReportSummary(2, "Estafa", "Nunca enviaron el pedido", "Contra: Tienda Fantasma", "En revisión", "11 Oct 2025"),
        ReportSummary(4, "Entrega", "Pedido incompleto", "Contra: Librería Central", "Resuelta", "08 Oct 2025"),
        ReportSummary(5, "Retraso", "Demora de 3 horas", "Contra: Pizza Express", "Rechazada", "01 Oct 2025")
    )
    private val receivedReports = listOf(
        ReportSummary(10, "Falta", "No se presentó a la entrega", "Motivo: Inasistencia", "Expirada", "12 Oct 2025"),
        ReportSummary(11, "Falta", "Canceló la venta sin motivo", "Motivo: Cancelación", "Expirada", "12 Oct 2025"),
        ReportSummary(12, "Advertencia", "Por lenguaje inapropiado", "Motivo: Mal vocabulario", "Expirada", "12 Oct 2025"),
        ReportSummary(13, "Sanción", "Cuenta suspendida por 30 días", "Motivo: Reincidencia en faltas", "Activa", "12 Oct 2025")
    )

    // 3. Almacén mutable para tickets (Usamos la inicialización detallada de la primera versión)
    private val _myTickets: MutableList<TicketSummary> = mutableListOf(
        TicketSummary(101, "ORD-8821", "Bodega Don Pepe", 54.50, "pendiente", "2023-10-25"),
        TicketSummary(102, "ORD-7743", "Farmacia Salud", 12.00, "completado", "2023-10-20"),
        TicketSummary(103, "ORD-1120", "Pollería El Sabroso", 89.90, "cancelado", "2023-10-15")
    )

    // --- Funciones de Utilidad y Auth ---

    // Permite que los Repositorios obtengan la instancia del MockService
    fun getMockService(): MockApiService {
        // Implementación mínima para evitar errores de compilación si no está definida en el contexto
        return MockApiService()
    }

    // Usamos la simulación de vendedor (idUsuario=10)
    fun getFakeSession() = UserSessionData(
        idUsuario = 10,
        tipoUsuario = "vendedor", // Para probar las opciones de Editar/Eliminar
        token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.fake.token"
    )

    // --- 2. HOME & DISCOVER ---

    fun getCategories(): List<CategoriaNegocio> {
        // Usamos la lista de categorías extendida y más moderna
        return listOf(
            CategoriaNegocio(1, "Alimentos"), CategoriaNegocio(2, "Ropa"), CategoriaNegocio(3, "Campo"),
            CategoriaNegocio(4, "Belleza"), CategoriaNegocio(5, "Hogar"), CategoriaNegocio(6, "Tecnología")
        )
    }

    fun getHomeFeed(): HomeFeedResponse {
        // Reutilizamos items de allItems para el feed
        val products = allItems.filter { it.type == "product" }.take(2)
        val posts = allItems.filter { it.type == "post" }.take(1)

        val sectionProducts = FeedSection(
            type = "featured_products",
            title = "Ofertas cerca de ti",
            items = products
        )

        val sectionPosts = FeedSection(
            type = "latest_posts",
            title = "Novedades de tus caseritos",
            items = posts
        )

        return HomeFeedResponse(
            status = "success",
            data = FeedData(sections = listOf(sectionProducts, sectionPosts)),
            pagination = PaginationMeta(1, 1, false)
        )
    }

    // FUNCIÓN para manejar los filtros de tipo, categoría y búsqueda. (Mejorada para usar allItems)
    fun getDiscoverResults(filter: String, category: String? = null, query: String = ""): HomeFeedResponse {

        var results = allItems // Empezamos con todos los items

        // 1. Filtrar por Tipo (product, business, or all)
        results = when (filter) {
            "product" -> results.filter { it.type == "product" }
            "business" -> results.filter { it.type == "business" }
            else -> results
        }

        // 2. Filtrar por Categoría
        if (category != null) {
            results = results.filter { it.details.categoria == category }
        }

        // 3. Filtrar por Búsqueda (opcional)
        if (query.isNotEmpty()) {
            results = results.filter {
                it.details.nombreProducto?.contains(query, ignoreCase = true) == true ||
                        it.details.nombreNegocio?.contains(query, ignoreCase = true) == true
            }
        }

        return HomeFeedResponse(
            "success",
            FeedData(items = results),
            PaginationMeta(1, 1, false)
        )
    }

    // --- 3. PERFIL NEGOCIO ---
    fun getNegocioProfile(id: Int): NegocioProfile {
        // Usamos la versión detallada de NegocioProfile
        return NegocioProfile(
            idNegocio = id,
            nombreNegocio = "Bodega Don Pepe",
            descripcion = "La mejor bodega del barrio. Aceptamos Yape y Plin. Delivery gratis en la zona.",
            rubro = "Bodega",
            profileUrl = "https://i.pravatar.cc/150?img=33",
            calificacionPromedio = 4.8,
            ventasTotales = 1540,
            horarioResumen = "Lun-Dom: 7am - 10pm"
        )
    }

    // Ahora filtramos desde allItems por ID de negocio
    fun getBusinessProducts(negocioId: Int): List<ItemDetails> {
        return allItems
            .filter { it.type == "product" && it.details.idNegocio == negocioId }
            .map { it.details }
    }

    // --- 4. CHAT ---
    fun getChatList(): List<ChatSummary> {
        // Usamos la versión detallada
        return listOf(
            ChatSummary(1, "Bodega Don Pepe", "https://i.pravatar.cc/150?img=33", "Ya salió tu pedido, llega en 5 min", 2, "2023-10-25T14:30:00Z"),
            ChatSummary(2, "Ferretería El Tornillo", "https://i.pravatar.cc/150?img=11", "Sí tenemos stock de pintura blanca", 0, "2023-10-24T09:15:00Z"),
            ChatSummary(3, "Juan Perez (Cliente)", "https://i.pravatar.cc/150?img=5", "Gracias joven", 0, "2023-10-20T18:00:00Z")
        )
    }

    fun getChatMessages(): List<ChatMessage> {
        // Usamos la versión detallada
        return listOf(
            ChatMessage(1, 99, "Hola casero, ¿tienes gas?", "2023-10-25T14:00:00Z", true),
            ChatMessage(2, 10, "Sí, el balón de 10kg está 45 soles", "2023-10-25T14:05:00Z", true),
            ChatMessage(3, 99, "Mándame uno por favor", "2023-10-25T14:06:00Z", true),
            ChatMessage(4, 10, "Ya salió tu pedido, llega en 5 min", "2023-10-25T14:30:00Z", false)
        )
    }

    // --- 5. USUARIO (TICKETS & SECURITY) ---

    fun getMyTickets(): List<TicketSummary> {
        return _myTickets.toList()
    }

    fun addMyTicket(ticket: TicketSummary) {
        _myTickets.add(0, ticket)
        println("DEBUG: Nuevo Ticket Añadido: ${ticket.codigoTicket}")
    }

    fun getReports(tipo: String): List<ReportSummary> {
        return if (tipo == "sent") sentReports else receivedReports
    }
}