package com.micaserito.app.data.repository

import com.micaserito.app.data.api.MockData // Se asume que MockData está en esta ruta
import com.micaserito.app.data.model.ChatSummary
import com.micaserito.app.data.model.ChatRepository
import com.micaserito.app.data.model.ChatMessage

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOn

// SIMULACIÓN DE CONSTANTES (PARA SOLUCIONAR EL ERROR DE VISIBILIDAD PRIVATE EN MockData)
private const val NOMBRE_NEGOCIO_1_MOCK = "Bodega Don Pepe"
private const val IMG_NEGOCIO_1_MOCK = "https://i.pravatar.cc/150?img=33"
private const val NOMBRE_NEGOCIO_2_MOCK = "Ferretería El Clavo"
private const val IMG_NEGOCIO_2_MOCK = "https://i.pravatar.cc/150?img=11"


class MockChatRepository : ChatRepository {

    private val FAKE_CURRENT_USER_ID = MockData.getFakeSession().idUsuario

    private val fakeChats = listOf(
        // CORRECCIÓN: Usamos las constantes MOCK para evitar el error de acceso privado
        ChatSummary(idChat = 1, nombreParticipante = NOMBRE_NEGOCIO_1_MOCK, profileUrl = IMG_NEGOCIO_1_MOCK,
            ultimoMensaje = "¡Pan caliente! 🥖", mensajesNoLeidos = 2, fechaUltimoMensaje = "10:30"),
        ChatSummary(idChat = 2, nombreParticipante = NOMBRE_NEGOCIO_2_MOCK, profileUrl = IMG_NEGOCIO_2_MOCK,
            ultimoMensaje = "Oferta en pinturas 🎨", mensajesNoLeidos = 0, fechaUltimoMensaje = "Ayer"),
        ChatSummary(idChat = 3, nombreParticipante = "Javier López", profileUrl = "https://i.pravatar.cc/150?img=5",
            ultimoMensaje = "¿Tienen stock de tornillos?", mensajesNoLeidos = 5, fechaUltimoMensaje = "15/12/24"),
    )

    private val fakeMessagesChat1 = listOf(
        ChatMessage(100, 2, "Buenos días, ¿hay pollo?", "10:00", true),
        ChatMessage(101, FAKE_CURRENT_USER_ID, "Estoy en camino.", "10:05", true),
        ChatMessage(102, 2, "Ok.", "10:10", true),
        ChatMessage(103, FAKE_CURRENT_USER_ID, "Te veo luego.", "10:15", false)
    )

    fun getFakeUserId(): Int = FAKE_CURRENT_USER_ID

    override fun getChats(filter: String?): Flow<List<ChatSummary>> = flow {
        delay(500)
        emit(fakeChats.filter { filter != "unread" || it.mensajesNoLeidos > 0 })
    }.flowOn(Dispatchers.IO)

    override fun getMessages(chatId: String, page: Int): Flow<List<ChatMessage>> = flow {
        delay(500)
        emit(fakeMessagesChat1)
    }.flowOn(Dispatchers.IO)

    override fun sendMessage(chatId: String, content: String): Flow<ChatMessage> = flow {
        delay(300)
        val returnedMessage = ChatMessage(
            idMensaje = (-System.currentTimeMillis()).toInt(),
            idUsuario = FAKE_CURRENT_USER_ID,
            contenido = content,
            fechaEnvio = "Ahora",
            leido = false
        )
        emit(returnedMessage)
    }.flowOn(Dispatchers.IO)
}