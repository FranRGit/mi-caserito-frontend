package com.micaserito.app.data.Local

import android.content.Context
import android.content.SharedPreferences

object SessionManager {

    private const val PREF_NAME = "micaserito_session"
    private const val KEY_TOKEN = "token"
    private const val KEY_USER_TYPE = "user_type"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    /** Guardar sesión */
    fun saveSession(
        context: Context,
        token: String,
        userType: String
    ) {
        getPrefs(context).edit()
            .putString(KEY_TOKEN, token)
            .putString(KEY_USER_TYPE, userType)
            .apply()
    }

    /** Obtener sesión */
    fun getSession(context: Context): SessionData? {
        val prefs = getPrefs(context)
        val token = prefs.getString(KEY_TOKEN, null)
        val userType = prefs.getString(KEY_USER_TYPE, null)

        return if (token != null && userType != null) {
            SessionData(token, userType)
        } else {
            null
        }
    }

    /** Cerrar sesión */
    fun clearSession(context: Context) {
        getPrefs(context).edit().clear().apply()
    }
}

/** Modelo simple de sesión */
data class SessionData(
    val token: String,
    val userType: String
)
