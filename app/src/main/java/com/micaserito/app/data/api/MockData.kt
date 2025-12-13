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
        // (Items de ejemplo para Discover)
        FeedItem("product", ItemDetails(idProducto = 1, nombreProducto = "Pollo a la Brasa", precioBase = 55.90, nombreNegocio = "Pardos Chicken", ciudad = "Miraflores", imageUrl = "https://i.imgur.com/iUzGAnp.jpg", categoria = "Alimentos")),
        FeedItem("product", ItemDetails(idProducto = 3, nombreProducto = "Polo Básico de Algodón", precioBase = 39.90, nombreNegocio = "Gamarra Express", ciudad = "La Victoria", imageUrl = "https://i.imgur.com/9aA2N6V.jpg", categoria = "Ropa"))
    )

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

    // --- Funciones Públicas ---

    fun getFakeSession() = UserSessionData(10, "cliente", "...token...")

    fun getCategories(): List<CategoriaNegocio> {
        return listOf(
            CategoriaNegocio(1, "Alimentos"), CategoriaNegocio(2, "Ropa"), CategoriaNegocio(3, "Campo"),
            CategoriaNegocio(4, "Belleza"), CategoriaNegocio(5, "Hogar"), CategoriaNegocio(6, "Tecnología")
        )
    }

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

    fun getNegocioProfile(id: Int) = NegocioProfile(id, "Bodega Don Pepe", "La mejor bodega del barrio.", "Bodega", "https://i.pravatar.cc/150?img=33", 4.8, 1540, "Lun-Dom: 7am - 10pm")
    fun getBusinessProducts(): List<ItemDetails> = allItems.filter { it.type == "product" && it.details.idNegocio == 101 }.map { it.details }
    fun getChatList(): List<ChatSummary> = listOf(ChatSummary(1, "Bodega Don Pepe", "url", "Msg", 2, "date"))
    fun getChatMessages(): List<ChatMessage> = listOf(ChatMessage(1, 99, "Hola", "date", true))

    fun getMyTickets(): List<TicketSummary> = myTicketsList.toList()
    fun addMyTicket(ticket: TicketSummary) { myTicketsList.add(0, ticket) }

    fun getReports(tipo: String): List<ReportSummary> {
        return if (tipo == "sent") sentReports else receivedReports
    }
}