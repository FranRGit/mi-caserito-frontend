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
    private val myTicketsList = mutableListOf<TicketSummary>()

    private val allItems = listOf(
        // Alimentos
        FeedItem("product", ItemDetails(idProducto = 1, nombreProducto = "Pollo a la Brasa", precioBase = 55.90, nombreNegocio = "Pardos Chicken", ciudad = "Miraflores", imageUrl = "https://i.imgur.com/iUzGAnp.jpg", categoria = "Alimentos")),
        FeedItem("product", ItemDetails(idProducto = 2, nombreProducto = "Leche Gloria (lata)", precioBase = 4.20, nombreNegocio = "Bodega El Chino", ciudad = "Surquillo", imageUrl = "https://i.imgur.com/8u1D5sR.jpg", categoria = "Alimentos")),
        FeedItem("business", ItemDetails(idNegocio = 101, nombreNegocio = "Verdulería Doña Juana", calificacionPromedio = 4.8, direccion = "Mercado N°2, Surco", profileUrl = "https://i.pravatar.cc/150?img=1", categoria = "Alimentos")),
        // Ropa
        FeedItem("product", ItemDetails(idProducto = 3, nombreProducto = "Polo Básico de Algodón", precioBase = 39.90, nombreNegocio = "Gamarra Express", ciudad = "La Victoria", imageUrl = "https://i.imgur.com/9aA2N6V.jpg", categoria = "Ropa")),
        FeedItem("business", ItemDetails(idNegocio = 102, nombreNegocio = "Boutique Elegancia", calificacionPromedio = 4.9, direccion = "C.C. Jockey Plaza", profileUrl = "https://i.pravatar.cc/150?img=2", categoria = "Ropa")),
        // Campo (Ferretería)
        FeedItem("product", ItemDetails(idProducto = 4, nombreProducto = "Martillo de Uña", precioBase = 25.00, nombreNegocio = "Ferretería El Tornillo", ciudad = "Independencia", imageUrl = "https://i.imgur.com/4z2zL9E.jpg", categoria = "Campo")),
        FeedItem("business", ItemDetails(idNegocio = 103, nombreNegocio = "Sodimac", calificacionPromedio = 4.5, direccion = "Av. Angamos", profileUrl = "https://i.pravatar.cc/150?img=3", categoria = "Campo")),
        // Belleza
        FeedItem("product", ItemDetails(idProducto = 5, nombreProducto = "Protector Solar SPF 50", precioBase = 60.00, nombreNegocio = "Farmacia MiFarma", ciudad = "San Isidro", imageUrl = "https://i.imgur.com/sC050R4.jpg", categoria = "Belleza")),
        // Hogar
        FeedItem("product", ItemDetails(idProducto = 6, nombreProducto = "Juego de Sábanas 2 Plazas", precioBase = 120.00, nombreNegocio = "CasaIdeas", ciudad = "Lince", imageUrl = "https://i.imgur.com/v2Jv27J.jpg", categoria = "Hogar")),
        // Post (sin categoría específica)
        FeedItem("post", ItemDetails(idPost = 501, nombreNegocio = "Panadería El Buen Sabor", profileUrl = "https://i.pravatar.cc/150?img=12", fechaCreacion = "hace 2 horas", descripcion = "¡Salieron los panes calientes! 🥖🥖", imageUrl = "https://i.imgur.com/L8a1qj9.jpg"))
    )

    // --- 1. USUARIO / AUTH ---
    fun getFakeSession() = UserSessionData(10, "cliente", "...token...")

    // --- 2. HOME & DISCOVER ---
    fun getCategories(): List<CategoriaNegocio> {
        return listOf(
            CategoriaNegocio(1, "Alimentos"), CategoriaNegocio(2, "Ropa"), CategoriaNegocio(3, "Campo"),
            CategoriaNegocio(4, "Belleza"), CategoriaNegocio(5, "Hogar"), CategoriaNegocio(6, "Tecnología")
        )
    }

    // RESTAURADA: Función para el Home, usada por MockApiService
    fun getHomeFeed(): HomeFeedResponse {
        val products = allItems.filter { it.type == "product" }.take(2)
        val posts = allItems.filter { it.type == "post" }.take(1)
        val sectionProducts = FeedSection(type = "featured_products", title = "Ofertas cerca de ti", items = products)
        val sectionPosts = FeedSection(type = "latest_posts", title = "Novedades de tus caseritos", items = posts)
        return HomeFeedResponse("success", FeedData(sections = listOf(sectionProducts, sectionPosts)), PaginationMeta(1, 1, false))
    }

    fun getDiscoverResults(filterType: String, category: String? = null): HomeFeedResponse {
        var results = allItems
        if (category != null) {
            results = results.filter { it.details.categoria == category }
        }
        results = when (filterType) {
            "product" -> results.filter { it.type == "product" }
            "business" -> results.filter { it.type == "business" }
            else -> results
        }
        return HomeFeedResponse("success", FeedData(items = results), PaginationMeta(1, 1, false))
    }

    // --- 3. PERFIL NEGOCIO, CHAT, etc. (RESTAURADAS) ---
    fun getNegocioProfile(id: Int): NegocioProfile {
        return NegocioProfile(id, "Bodega Don Pepe", "La mejor bodega del barrio.", "Bodega", "https://i.pravatar.cc/150?img=33", 4.8, 1540, "Lun-Dom: 7am - 10pm")
    }

    fun getBusinessProducts(): List<ItemDetails> {
        return allItems.filter { it.type == "product" && it.details.idNegocio == 101 }.map { it.details }
    }
    
    fun getChatList(): List<ChatSummary> {
        return listOf(
            ChatSummary(1, "Bodega Don Pepe", "https://i.pravatar.cc/150?img=33", "Ya salió tu pedido", 2, "2023-10-25T14:30:00Z")
        )
    }

    fun getChatMessages(): List<ChatMessage> {
        return listOf(
            ChatMessage(1, 99, "Hola casero, ¿tienes gas?", "2023-10-25T14:00:00Z", true)
        )
    }

    // --- 4. USUARIO (TICKETS & SECURITY) ---
    fun getMyTickets(): List<TicketSummary> = myTicketsList.toList()
    fun addMyTicket(ticket: TicketSummary) { myTicketsList.add(0, ticket) }
    fun getReports(tipo: String): List<ReportSummary> {
        if (tipo == "sent") {
            return listOf(ReportSummary(1, "entrega", "Pedido nunca llegó", "Bodega Mala Fama", "pendiente", "2023-10-25"))
        } else {
            return listOf(ReportSummary(5, "conducta", "Vendedor grosero", "Juan Perez", "desestimado", "2023-08-05"))
        }
    }
}