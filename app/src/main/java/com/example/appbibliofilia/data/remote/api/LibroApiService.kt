package com.example.appbibliofilia.data.remote.api

import com.example.appbibliofilia.data.remote.model.LibroDto
import retrofit2.Response
import retrofit2.http.*

interface LibroApiService {
    @Headers("Content-Type: application/json")
    @GET("libro")
    suspend fun getLibros(
        @Header("Authorization") token: String
    ): Response<List<LibroDto>>
    @Headers("Content-Type: application/json")
    @POST("libro")
    suspend fun crearLibro(
        @Header("Authorization") token: String,
        @Body libro: LibroDto
    ): Response<LibroDto>
    @Headers("Content-Type: application/json")
    @PUT("libro/{id}")
    suspend fun actualizarLibro(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body libro: LibroDto
    ): Response<LibroDto>
    @Headers("Content-Type: application/json")
    @DELETE("libro/{id}")
    suspend fun eliminarLibro(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Response<Unit>
}

