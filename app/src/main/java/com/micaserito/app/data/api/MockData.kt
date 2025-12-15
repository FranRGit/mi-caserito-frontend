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
import com.micaserito.app.data.model.User
import com.micaserito.app.data.model.RegisterRequest
import com.micaserito.app.data.model.BusinessInfo

object MockData {

    private val usuariosRegistrados = mutableListOf<User>(
        User(1, "cliente@demo.com", "cliente", "token_cliente_123"),
        User(2, "vendedor@demo.com", "vendedor", "token_vendedor_123")
    )

    fun loginFake(email: String, password: String): User? {
        return usuariosRegistrados.firstOrNull { it.email == email }
    }

    fun registrarUsuario(request: RegisterRequest): Boolean {
        val nuevo = User(
            id = usuariosRegistrados.size + 1,
            email = request.email,
            tipoUsuario = request.tipoUsuario,
            token = "token_mock_${request.email}"
        )
        usuariosRegistrados.add(nuevo)
        return true
    }

    fun registrarNegocio(business: BusinessInfo): Boolean {
        return true
    }

    private val myTickets = mutableListOf<TicketSummary>(
        TicketSummary(101, "ORD-8821", "Bodega Don Pepe", 54.50, "pendiente", "2023-10-25"),
        TicketSummary(102, "ORD-7743", "Farmacia Salud", 12.00, "completado", "2023-10-20"),
        TicketSummary(103, "ORD-1120", "Pollería El Sabroso", 89.90, "cancelado", "2023-10-15")
    )

    fun getFakeSession() = UserSessionData(
        idUsuario = 10,
        tipoUsuario = "cliente",
        token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.fake.token"
    )

    fun getCategories(): List<CategoriaNegocio> {
        return listOf(
            CategoriaNegocio(1, "Bodegas"),
            CategoriaNegocio(2, "Verdulerías"),
            CategoriaNegocio(3, "Ferreterías"),
            CategoriaNegocio(4, "Farmacias"),
            CategoriaNegocio(5, "Restaurantes")
        )
    }

    fun getHomeFeed(): HomeFeedResponse {
        val prod1 = FeedItem(
            type = "product",
            details = ItemDetails(
                idProducto = 101,
                nombreProducto = "Papa Amarilla (Kilo)",
                precioBase = 6.50,
                imageUrl = "https://i.imgur.com/M9k7qj9.jpg",
                nombreNegocio = "Verdulería Doña Juana",
                ciudad = "Lima",
                categoria = "Verdulerías"
            )
        )

        val prod2 = FeedItem(
            type = "product",
            details = ItemDetails(
                idProducto = 102,
                nombreProducto = "Leche Gloria Azul (Pack x6)",
                precioBase = 22.00,
                imageUrl = "https://i.imgur.com/8u1D5sR.jpg",
                nombreNegocio = "Bodega El Chino",
                ciudad = "Pueblo Libre",
                categoria = "Bodegas"
            )
        )

        val post1 = FeedItem(
            type = "post",
            details = ItemDetails(
                idPost = 501,
                nombreNegocio = "Panadería El Buen Sabor",
                profileUrl = "https://i.pravatar.cc/150?img=12",
                fechaCreacion = "2023-10-25T10:30:00Z",
                descripcion = "¡Salieron los panes calientes! Baguette y Ciabatta recién horneados. 🥖🥖",
                imageUrl = "https://i.imgur.com/L8a1qj9.jpg"
            )
        )

        val sectionProducts = FeedSection(
            type = "featured_products",
            title = "Ofertas cerca de ti",
            items = listOf(prod1, prod2, prod1)
        )

        val sectionPosts = FeedSection(
            type = "latest_posts",
            title = "Novedades de tus caseritos",
            items = listOf(post1, post1)
        )

        return HomeFeedResponse(
            status = "success",
            data = FeedData(sections = listOf(sectionProducts, sectionPosts)),
            pagination = PaginationMeta(1, 1, false)
        )
    }

    fun getDiscoverResults(filter: String, categoryId: Int?): HomeFeedResponse {
        val allItems = getHomeFeed().data.sections!!.flatMap { it.items }
        var results = allItems

        if (categoryId != null) {
            val categoryName = getCategories().find { it.id == categoryId }?.nombre
            if (categoryName != null) {
                results = results.filter { it.details.categoria == categoryName }
            }
        }

        results = when (filter) {
            "business" -> results.filter { it.type == "business" }
            "product" -> results.filter { it.type == "product" }
            else -> results
        }

        return HomeFeedResponse(
            status = "success",
            data = FeedData(items = results),
            pagination = PaginationMeta(1, 1, false)
        )
    }

    fun getNegocioProfile(id: Int): NegocioProfile {
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

    fun getBusinessProducts(): List<ItemDetails> {
        return listOf(
            ItemDetails(
                idProducto = 201,
                nombreProducto = "Arroz Costeño",
                precioBase = 4.20,
                imageUrl = "https://via.placeholder.com/150"
            ),
            ItemDetails(
                idProducto = 202,
                nombreProducto = "Aceite Primor",
                precioBase = 11.50,
                imageUrl = "https://via.placeholder.com/150"
            ),
            ItemDetails(
                idProducto = 203,
                nombreProducto = "Atún Florida",
                precioBase = 5.80,
                imageUrl = "https://via.placeholder.com/150"
            )
        )
    }

    fun getChatList(): List<ChatSummary> {
        return listOf(
            ChatSummary(
                1,
                "Bodega Don Pepe",
                "https://i.pravatar.cc/150?img=33",
                "Ya salió tu pedido, llega en 5 min",
                2,
                "2023-10-25T14:30:00Z"
            ),
            ChatSummary(
                2,
                "Ferretería El Tornillo",
                "https://i.pravatar.cc/150?img=11",
                "Sí tenemos stock de pintura blanca",
                0,
                "2023-10-24T09:15:00Z"
            ),
            ChatSummary(
                3,
                "Juan Perez (Cliente)",
                "https://i.pravatar.cc/150?img=5",
                "Gracias joven",
                0,
                "2023-10-20T18:00:00Z"
            )
        )
    }

    fun getChatMessages(): List<ChatMessage> {
        return listOf(
            ChatMessage(1, 99, "Hola casero, ¿tienes gas?", "2023-10-25T14:00:00Z", true),
            ChatMessage(2, 10, "Sí, el balón de 10kg está 45 soles", "2023-10-25T14:05:00Z", true),
            ChatMessage(3, 99, "Mándame uno por favor", "2023-10-25T14:06:00Z", true),
            ChatMessage(4, 10, "Ya salió tu pedido, llega en 5 min", "2023-10-25T14:30:00Z", false)
        )
    }

    fun getMyTickets(): List<TicketSummary> {
        return myTickets
    }

    fun addMyTicket(ticket: TicketSummary) {
        myTickets.add(0, ticket)
    }

    fun getReports(tipo: String): List<ReportSummary> {
        if (tipo == "sent") {
            return listOf(
                ReportSummary(
                    1,
                    "entrega",
                    "Pedido nunca llegó",
                    "Bodega Mala Fama",
                    "pendiente",
                    "2023-10-25"
                ),
                ReportSummary(
                    2,
                    "producto",
                    "Producto vencido",
                    "Market Express",
                    "resuelto",
                    "2023-09-10"
                )
            )
        } else {
            return listOf(
                ReportSummary(
                    5,
                    "conducta",
                    "Vendedor grosero",
                    "Juan Perez",
                    "desestimado",
                    "2023-08-05"
                )
            )
        }
    }
}