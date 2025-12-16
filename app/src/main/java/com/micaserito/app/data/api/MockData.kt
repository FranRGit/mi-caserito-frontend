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


    // --- GESTIÓN DE TICKETS (Simulación de BD) ---

    // 1. LISTA MAESTRA (Fuente única de verdad)
    private val ticketsDB = mutableListOf<com.micaserito.app.data.model.Ticket>(
        // Ticket 1: Completado (Bodega)
        com.micaserito.app.data.model.Ticket(
            idTicket = 801,
            codigoTicket = "TCK-88123",
            idCliente = 1,
            idNegocio = 100,
            nombreNegocio = "Bodega Don Pepe",
            items = listOf(
                com.micaserito.app.data.model.TicketItem(1, nombre = "Pollo", precioUnitario = 55.90, cantidad = 1),
                com.micaserito.app.data.model.TicketItem(2, nombre = "Leche", precioUnitario = 4.20, cantidad = 2)
            ),
            total = 64.30,
            estado = com.micaserito.app.data.model.TicketStatus.COMPLETADO,
            fechaEntrega = "15 diciembre, 18:00"
        ),

        // Ticket 2: En proceso (Bodega - Para probar QR)
        com.micaserito.app.data.model.Ticket(
            idTicket = 802,
            codigoTicket = "TCK-99551",
            idCliente = 1,
            idNegocio = 100,
            nombreNegocio = "Bodega Don Pepe",
            items = listOf(
                com.micaserito.app.data.model.TicketItem(3, nombre = "Polo", precioUnitario = 39.90, cantidad = 1)
            ),
            total = 39.90,
            estado = com.micaserito.app.data.model.TicketStatus.EN_PROCESO,
            puntoEntrega = "Parque Kennedy",
            fechaEntrega = "Hoy, 20:00",
            codigoVerificacion = "12345"
        ),

        // Ticket 3: Anulado (Ferretería)
        com.micaserito.app.data.model.Ticket(
            idTicket = 803,
            codigoTicket = "TCK-11002",
            idCliente = 1,
            idNegocio = 200,
            nombreNegocio = "Ferretería El Clavo",
            items = listOf(
                com.micaserito.app.data.model.TicketItem(10, nombre = "Taladro", precioUnitario = 150.00, cantidad = 1)
            ),
            total = 150.00,
            estado = com.micaserito.app.data.model.TicketStatus.ANULADO
        )
    )

    // 2. CREAR TICKET (Desde el Carrito)
    fun createTicketFromCart(idCliente: Int): com.micaserito.app.data.model.Ticket? {
        val businessId = com.micaserito.app.data.Local.CartManager.getCurrentBusinessId() ?: return null
        val businessName = com.micaserito.app.data.Local.CartManager.getCurrentBusinessName() ?: "Negocio"

        // Copia profunda de items
        val items = com.micaserito.app.data.Local.CartManager.items.map { it.copy() }
        val total = com.micaserito.app.data.Local.CartManager.getTotal()

        if (items.isEmpty()) return null

        val newTicket = com.micaserito.app.data.model.Ticket(
            idTicket = ticketsDB.size + 1000,
            codigoTicket = "TCK-${(10000..99999).random()}",
            idCliente = idCliente,
            idNegocio = businessId,
            nombreNegocio = businessName,
            items = items,
            total = total,
            estado = com.micaserito.app.data.model.TicketStatus.PENDIENTE
        )

        ticketsDB.add(newTicket)
        com.micaserito.app.data.Local.CartManager.clearCart()
        return newTicket
    }

    // 3. OBTENER TICKET POR ID (Para el detalle)
    fun getTicketById(ticketId: Int): com.micaserito.app.data.model.Ticket? {
        return ticketsDB.find { it.idTicket == ticketId }
    }

    // 4. LISTAR TICKETS (FILTRADO POR USUARIO)
    fun getMyTickets(userId: Int, userRole: String = "cliente"): List<TicketSummary> {
        // Filtramos la DB según quién pregunta
        val ticketsFiltrados = if (userRole == "vendedor") {
            // Si soy vendedor, busco tickets dirigidos a MI negocio
            val myBusinessId = getBusinessIdByUserId(userId)
            ticketsDB.filter { it.idNegocio == myBusinessId }
        } else {
            // Si soy cliente, busco tickets creados por MI
            ticketsDB.filter { it.idCliente == userId }
        }

        return ticketsFiltrados.map { ticket ->
            TicketSummary(
                idTicket = ticket.idTicket,
                codigoTicket = ticket.codigoTicket,
                titulo = ticket.nombreNegocio, // O nombre del cliente si eres vendedor
                total = ticket.total,
                estado = ticket.estado.name.lowercase().replaceFirstChar { it.uppercase() },
                fecha = ticket.fechaEntrega ?: "Por definir"
            )
        }.reversed()
    }
    //Agregar un producto al ticket
    fun addProductToTicket(userId: Int, product: ItemDetails): String {
        val businessId = product.idNegocio ?: return "Error: Sin negocio"

        // 1. BUSCAR: ¿Ya tengo un ticket abierto (PENDIENTE) con este negocio?
        val existingTicket = ticketsDB.find {
            it.idCliente == userId &&
                    it.idNegocio == businessId &&
                    it.estado == com.micaserito.app.data.model.TicketStatus.PENDIENTE
        }

        if (existingTicket != null) {
            // A. ACTUALIZAR: El ticket existe, sumamos el producto

            // Verificamos si el producto ya está en la lista para sumar cantidad
            val itemInTicket = existingTicket.items.find { it.idProducto == product.idProducto }

            if (itemInTicket != null) {
                itemInTicket.cantidad += 1
            } else {
                // Si es un producto nuevo en este ticket, lo agregamos a la lista
                val nuevaLista = existingTicket.items.toMutableList()
                nuevaLista.add(
                    com.micaserito.app.data.model.TicketItem(
                        idProducto = product.idProducto ?: 0,
                        nombre = product.nombreProducto ?: "Item",
                        precioUnitario = product.precioBase ?: 0.0,
                        cantidad = 1
                    )
                )

            }

            // Recalcular Total
            existingTicket.total = existingTicket.items.sumOf { it.precioUnitario * it.cantidad }

            return "Producto agregado a ticket ${existingTicket.codigoTicket}"

        } else {
            val newItems = listOf(
                com.micaserito.app.data.model.TicketItem(
                    idProducto = product.idProducto ?: 0,
                    nombre = product.nombreProducto ?: "Item",
                    precioUnitario = product.precioBase ?: 0.0,
                    cantidad = 1
                )
            )

            val newTicket = com.micaserito.app.data.model.Ticket(
                idTicket = ticketsDB.size + 1000,
                codigoTicket = "TCK-${(10000..99999).random()}",
                idCliente = userId,
                idNegocio = businessId,
                nombreNegocio = product.nombreNegocio ?: "Negocio",
                items = newItems, // Si cambiaste a MutableList, usa mutableListOf(...)
                total = product.precioBase ?: 0.0,
                estado = com.micaserito.app.data.model.TicketStatus.PENDIENTE // <--- CLAVE: Nace como pendiente
            )

            ticketsDB.add(0, newTicket) // Lo ponemos al inicio
            return "Nuevo ticket creado: ${newTicket.codigoTicket}"
        }
    }
    fun isProductAdded(userId: Int, productId: Int): Boolean {
        // 1. Buscamos si hay un ticket activo (PENDIENTE) para este usuario
        // Nota: Ojo, aquí deberías filtrar también por negocio si quisieras ser estricto,
        // pero generalmente basta con saber si el usuario ya lo pidió.
        val activeTicket = ticketsDB.find {
            it.idCliente == userId &&
                    it.estado == com.micaserito.app.data.model.TicketStatus.PENDIENTE
        } ?: return false

        // 2. Si hay ticket, buscamos si el producto está en la lista de items
        return activeTicket.items.any { it.idProducto == productId }
    }

    // A. Modificar cantidad (+1 o -1)
    fun updateTicketItemQuantity(ticketId: Int, productId: Int, delta: Int): Double {
        val ticket = ticketsDB.find { it.idTicket == ticketId } ?: return 0.0

        // Solo permitir editar si está PENDIENTE
        if (ticket.estado != com.micaserito.app.data.model.TicketStatus.PENDIENTE) return ticket.total

        val item = ticket.items.find { it.idProducto == productId } ?: return ticket.total

        // Actualizar cantidad
        val nuevaCantidad = item.cantidad + delta
        if (nuevaCantidad > 0) {
            item.cantidad = nuevaCantidad
        } else {
            // Opcional: Si baja a 0, ¿lo borramos? Por ahora dejémoslo en 1 mínimo.
            item.cantidad = 1
        }

        // Recalcular Total del Ticket
        ticket.total = ticket.items.sumOf { it.precioUnitario * it.cantidad }

        return ticket.total
    }

    // B. Cambiar estado a NEGOCIANDO (Botón "Solicitar")
    fun submitTicket(ticketId: Int): Boolean {
        val ticket = ticketsDB.find { it.idTicket == ticketId } ?: return false

        if (ticket.estado == com.micaserito.app.data.model.TicketStatus.PENDIENTE) {
            ticket.estado = com.micaserito.app.data.model.TicketStatus.NEGOCIANDO
            return true
        }
        return false
    }

    // C. Eliminar Ticket (Botón "Eliminar")
    fun deleteTicket(ticketId: Int): Boolean {
        return ticketsDB.removeIf { it.idTicket == ticketId }
    }

    // D. Aprobar Ticket (Vendedor define fecha y lugar)
    fun approveTicket(ticketId: Int, puntoEntrega: String, fechaEntrega: String): Boolean {
        val ticket = ticketsDB.find { it.idTicket == ticketId } ?: return false

        // Solo si está NEGOCIANDO pasa a EN_PROCESO
        if (ticket.estado == com.micaserito.app.data.model.TicketStatus.NEGOCIANDO) {
            ticket.estado = com.micaserito.app.data.model.TicketStatus.EN_PROCESO
            ticket.puntoEntrega = puntoEntrega
            ticket.fechaEntrega = fechaEntrega
            return true
        }
        return false
    }

    // E. Rechazar Ticket
    fun rejectTicket(ticketId: Int): Boolean {
        val ticket = ticketsDB.find { it.idTicket == ticketId } ?: return false
        ticket.estado = com.micaserito.app.data.model.TicketStatus.ANULADO
        return true
    }

    // F. Completar Ticket (Verificación de código)
    fun completeTicket(ticketId: Int, inputCode: String): Boolean {
        val ticket = ticketsDB.find { it.idTicket == ticketId } ?: return false
        val validCode = ticket.codigoVerificacion ?: "12345"

        if (inputCode == validCode) {
            ticket.estado = com.micaserito.app.data.model.TicketStatus.COMPLETADO
            return true
        }
        return false
    }
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