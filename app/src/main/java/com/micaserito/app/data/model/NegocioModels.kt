package com.micaserito.app.data.model

import com.google.gson.annotations.SerializedName

data class NegocioProfile(
    @SerializedName("id_negocio") val idNegocio: Int,
    @SerializedName("nombre_negocio") val nombreNegocio: String,
    @SerializedName("descripcion") val descripcion: String?,
    @SerializedName("rubro") val rubro: String?,
    @SerializedName("profile_url") val profileUrl: String?,
    @SerializedName("calificacion_promedio") val calificacionPromedio: Double,
    @SerializedName("ventas_totales") val ventasTotales: Int,
    @SerializedName("horario_resumen") val horarioResumen: String?
)

// Nota: Para las listas de productos y posts del perfil,
// reusaremos ItemDetails de FeedModels.kt