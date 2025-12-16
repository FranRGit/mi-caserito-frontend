package com.micaserito.app.data.api

import com.micaserito.app.data.model.*
import kotlinx.coroutines.delay
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response

class MockApiService : ApiService {

    // Simula el tiempo de respuesta de red (2 segundos)
    private val NETWORK_DELAY = 1500L

    // ================= AUTENTICACIÓN =================
    override suspend fun login(request: LoginRequest): Response<LoginResponse> {
        delay(NETWORK_DELAY) // Simula espera de internet

        // 1. Llamamos a MockData pasando el email y password reales del formulario
        val userSession = MockData.loginFake(request.email, request.password)

        // 2. Verificamos si MockData encontró al usuario
        return if (userSession != null) {
            // ¡ÉXITO! Devolvemos los datos de ese usuario específico
            val response = LoginResponse("success", userSession)
            Response.success(response)
        } else {
            // FALLO: Email o contraseña incorrectos
            Response.error(401, ResponseBody.create(null, "Credenciales incorrectas (Prueba: cliente@demo.com / 123)"))
        }
    }

    override suspend fun register(
        dni_image: MultipartBody.Part,
        profile_image: MultipartBody.Part?,
        data: Map<String, RequestBody>
    ): Response<RegisterResponse> {
        delay(NETWORK_DELAY)
        return Response.success(RegisterResponse("success", "Usuario registrado"))
    }

    // ================= HOME & DISCOVER =================
    override suspend fun getHomeFeed(page: Int, limit: Int): Response<HomeFeedResponse> {
        delay(NETWORK_DELAY)
        return Response.success(MockData.getHomeFeed())
    }

    override suspend fun searchDiscover(
        query: String?,
        filter: String,
        categoryId: Int?,
        page: Int
    ): Response<HomeFeedResponse> {
        delay(500) // Búsqueda más rápida

        // Usamos MockData fusionado que acepta Int? para categoryId
        return Response.success(
            MockData.getDiscoverResults(
                filter = filter,
                categoryId = categoryId, // Pasamos el ID directamente como Int?
                query = query ?: ""       // Pasamos la query (o string vacío si es nula)
            )
        )
    }

    override suspend fun getCategories(): Response<List<CategoriaNegocio>> {
        delay(300)
        return Response.success(MockData.getCategories())
    }

    // ================= GESTIÓN (Vendedor) =================
    override suspend fun createProduct(
        image: MultipartBody.Part,
        data: Map<String, RequestBody>
    ): Response<FeedItem> {
        delay(2000)
        // Devolvemos un item dummy como si se hubiera creado
        val dummyProduct = FeedItem("product", ItemDetails(idProducto = 999, nombreProducto = "Nuevo Producto"))
        return Response.success(dummyProduct)
    }

    override suspend fun createPost(
        image: MultipartBody.Part,
        descripcion: RequestBody
    ): Response<FeedItem> {
        delay(2000)
        val dummyPost = FeedItem("post", ItemDetails(idPost = 888, descripcion = "Nueva Publicación"))
        return Response.success(dummyPost)
    }

    // ================= PERFIL NEGOCIO =================
    override suspend fun getBusinessInfo(id: Int): Response<NegocioProfile> {
        delay(NETWORK_DELAY)
        return Response.success(MockData.getNegocioProfile(id))
    }

    override suspend fun getBusinessProducts(id: Int, page: Int): Response<List<ItemDetails>> {
        delay(1000)
        val products = MockData.getBusinessProducts(id)
        return Response.success(products)
    }

    override suspend fun getBusinessPosts(id: Int, page: Int): Response<List<ItemDetails>> {
        delay(1000)
        // [CÓDIGO CORREGIDO] Ahora llama a la función de MockData y pasa el 'id'
        val posts = MockData.getBusinessPosts(id)
        return Response.success(posts)
    }
    // ================= CHAT =================
    override suspend fun getChats(filter: String): Response<List<ChatSummary>> {
        delay(NETWORK_DELAY)
        return Response.success(MockData.getChatList())
    }

    override suspend fun getChatMessages(idChat: Int, page: Int): Response<ChatHistoryResponse> {
        delay(500)
        val msgs = MockData.getChatMessages()
        val meta = PaginationMeta(1, 1, false)
        return Response.success(ChatHistoryResponse(msgs, meta))
    }

    override suspend fun sendMessage(idChat: Int, request: SendMessageRequest): Response<ChatMessage> {
        delay(300)
        // Simular que el mensaje "rebotó" del servidor
        val newMsg = ChatMessage(100, 10, request.contenido, "Ahora", false)
        return Response.success(newMsg)
    }

    // ================= USUARIO (Tickets y Seguridad) =================
    override suspend fun getMyTickets(page: Int): Response<List<TicketSummary>> {
        delay(NETWORK_DELAY)
        return Response.success(MockData.getMyTickets(userId = 1, userRole = "cliente"))
    }

    override suspend fun getSentReports(page: Int): Response<List<ReportSummary>> {
        delay(1000)
        return Response.success(MockData.getReports("sent"))
    }

    override suspend fun getReceivedReports(page: Int): Response<List<ReportSummary>> {
        delay(1000)
        return Response.success(MockData.getReports("received"))
    }
}