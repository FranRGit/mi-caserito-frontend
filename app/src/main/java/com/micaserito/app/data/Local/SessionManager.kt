package com.micaserito.app.data.Local

import android.content.Context
import android.content.SharedPreferences

object SessionManager {

    private const val PREF_NAME = "micaserito_session"
    private const val KEY_TOKEN = "token"
    private const val KEY_USER_TYPE = "user_type"
    private const val KEY_USER_ID = "user_id"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    /** Guardar sesión */
    fun saveSession(
        context: Context,
        token: String,
        userType: String,
        userId: Int // <-- 2. Nuevo parámetro
    ) {
        getPrefs(context).edit()
            .putString(KEY_TOKEN, token)
            .putString(KEY_USER_TYPE, userType)
            .putInt(KEY_USER_ID, userId) // <-- 3. Guardar el ID
            .apply()
    }

    /** Obtener sesión completa */
    fun getSession(context: Context): SessionData? {
        val prefs = getPrefs(context)
        val token = prefs.getString(KEY_TOKEN, null)
        val userType = prefs.getString(KEY_USER_TYPE, null)
        val userId = prefs.getInt(KEY_USER_ID, 0) // <-- 4. Leer el ID, con 0 como default

        // Aseguramos que haya token, tipo y que el ID sea válido (no 0)
        return if (token != null && userType != null && userId != 0) {
            SessionData(token, userType, userId) // <-- 5. Pasar el ID al modelo
        } else {
            null
        }
    }

    /** Obtener solo el ID del usuario (Helper) */
    fun getUserId(context: Context): Int {
        return getPrefs(context).getInt(KEY_USER_ID, 0)
    }

    /** Obtener solo el tipo de usuario (Helper) */
    fun getUserType(context: Context): String {
        return getPrefs(context).getString(KEY_USER_TYPE, "cliente") ?: "cliente"
    }

    /** Cerrar sesión */
    fun clearSession(context: Context) {
        getPrefs(context).edit().clear().apply()
    }
}

/** Modelo simple de sesión */
data class SessionData(
    val token: String,
    val userType: String,
    val userId: Int // <-- 6. Añadir el ID al data class
)