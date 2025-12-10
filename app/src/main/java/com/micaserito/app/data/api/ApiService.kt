package com.micaserito.app.data.api

import com.micaserito.app.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ================= AUTENTICACIÓN =================
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @Multipart
    @POST("auth/register")
    suspend fun register(
        @Part dni_image: MultipartBody.Part,
        @Part profile_image: MultipartBody.Part?,
        @PartMap data: Map<String, @JvmSuppressWildcards RequestBody>
        // Tip: Usar PartMap ahorra escribir 20 @Part individuales para los strings
    ): Response<RegisterResponse>

    // ================= HOME & DISCOVER =================
    @GET("home/feed")
    suspend fun getHomeFeed(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): Response<HomeFeedResponse>

    @GET("discover/search")
    suspend fun searchDiscover(
        @Query("q") query: String?,
        @Query("filter") filter: String, // "all", "business", "product"
        @Query("category_id") categoryId: Int?,
        @Query("page") page: Int = 1
    ): Response<HomeFeedResponse> // Reusamos la respuesta porque la estructura es igual

    @GET("categories/business")
    suspend fun getCategories(): Response<List<CategoriaNegocio>>

    // ================= GESTIÓN (Vendedor) =================
    @Multipart
    @POST("products")
    suspend fun createProduct(
        @Part image: MultipartBody.Part,
        @PartMap data: Map<String, @JvmSuppressWildcards RequestBody>
    ): Response<FeedItem> // Devuelve el item creado

    @Multipart
    @POST("posts")
    suspend fun createPost(
        @Part image: MultipartBody.Part,
        @Part("descripcion") descripcion: RequestBody
    ): Response<FeedItem>

    // ================= PERFIL NEGOCIO =================
    @GET("business/{id}/info")
    suspend fun getBusinessInfo(@Path("id") id: Int): Response<NegocioProfile>

    @GET("business/{id}/products")
    suspend fun getBusinessProducts(
        @Path("id") id: Int,
        @Query("page") page: Int
    ): Response<List<ItemDetails>>

    @GET("business/{id}/posts")
    suspend fun getBusinessPosts(
        @Path("id") id: Int,
        @Query("page") page: Int
    ): Response<List<ItemDetails>>

    // ================= CHAT =================
    @GET("chats")
    suspend fun getChats(
        @Query("filter") filter: String = "all"
    ): Response<List<ChatSummary>>

    @GET("chats/{id_chat}/messages")
    suspend fun getChatMessages(
        @Path("id_chat") idChat: Int,
        @Query("page") page: Int
    ): Response<ChatHistoryResponse>

    @POST("chats/{id_chat}/messages")
    suspend fun sendMessage(
        @Path("id_chat") idChat: Int,
        @Body request: SendMessageRequest
    ): Response<ChatMessage>

    // ================= USUARIO (Tickets y Seguridad) =================
    @GET("tickets/my-tickets")
    suspend fun getMyTickets(
        @Query("page") page: Int
    ): Response<List<TicketSummary>>

    @GET("security/reports/sent")
    suspend fun getSentReports(
        @Query("page") page: Int
    ): Response<List<ReportSummary>>

    @GET("security/reports/received")
    suspend fun getReceivedReports(
        @Query("page") page: Int
    ): Response<List<ReportSummary>>
}