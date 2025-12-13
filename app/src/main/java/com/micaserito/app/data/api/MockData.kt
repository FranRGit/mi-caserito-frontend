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

    // --- 1. USUARIO / AUTH ---
    fun getFakeSession() = UserSessionData(
        idUsuario = 10,
        tipoUsuario = "vendedor", // Cambiar a "vendedor" para probar esa vista o "cliente"
        token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.fake.token"
    )

    // --- 2. HOME & DISCOVER ---
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
        // Items de ejemplo
        val prod1 = FeedItem(
            type = "product",
            details = ItemDetails(
                idProducto = 101,
                nombreProducto = "Papa Amarilla (Kilo)",
                precioBase = 6.50,
                imageUrl = "https://i.imgur.com/M9k7qj9.jpg", // Placeholder de comida
                nombreNegocio = "Verdulería Doña Juana",
                ciudad = "Lima"
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
                ciudad = "Pueblo Libre"
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

        // Secciones para el Home
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

    fun getDiscoverResults(filter: String): HomeFeedResponse {
        // Reutilizamos items para simular búsqueda
        val negocioItem = FeedItem(
            type = "business",
            details = ItemDetails(
                idNegocio = 99,
                nombreNegocio = "Bodega Don Pepe",
                calificacionPromedio = 4.8,
                direccion = "Av. Brasil 1234",
                profileUrl = "https://i.pravatar.cc/150?img=33",
                ciudad = "Jesús María"
            )
        )

        val listaItems = when (filter) {
            "business" -> listOf(negocioItem, negocioItem)
            "product" -> listOf(getHomeFeed().data.sections!![0].items[0])
            else -> listOf(negocioItem, getHomeFeed().data.sections!![0].items[0]) // Mixto
        }

        return HomeFeedResponse(
            status = "success",
            data = FeedData(items = listaItems),
            pagination = PaginationMeta(1, 1, false)
        )
    }

    // --- 3. PERFIL NEGOCIO ---
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
        // Solo detalles de producto para el catálogo
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

    // --- 4. CHAT ---
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
        // idUsuario 10 = YO, idUsuario 99 = EL OTRO
        return listOf(
            ChatMessage(1, 99, "Hola casero, ¿tienes gas?", "2023-10-25T14:00:00Z", true),
            ChatMessage(2, 10, "Sí, el balón de 10kg está 45 soles", "2023-10-25T14:05:00Z", true),
            ChatMessage(3, 99, "Mándame uno por favor", "2023-10-25T14:06:00Z", true),
            ChatMessage(4, 10, "Ya salió tu pedido, llega en 5 min", "2023-10-25T14:30:00Z", false)
        )
    }

    // --- 5. USUARIO (TICKETS & SECURITY) ---
    fun getMyTickets(): List<TicketSummary> {
        return listOf(
            TicketSummary(101, "ORD-8821", "Bodega Don Pepe", 54.50, "pendiente", "2023-10-25"),
            TicketSummary(102, "ORD-7743", "Farmacia Salud", 12.00, "completado", "2023-10-20"),
            TicketSummary(103, "ORD-1120", "Pollería El Sabroso", 89.90, "cancelado", "2023-10-15")
        )
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
            // Recibidos (en contra)
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