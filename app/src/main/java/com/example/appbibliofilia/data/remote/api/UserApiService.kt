package com.example.appbibliofilia.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import com.example.appbibliofilia.data.remote.model.AuthRequestDto
import com.example.appbibliofilia.data.remote.model.AuthResponseDto
import com.example.appbibliofilia.data.remote.model.UsuarioDto
import retrofit2.http.Headers

interface UserApiService {
    @Headers("Content-Type: application/json")
    @POST("auth/login")
    suspend fun login(
        @Body request: AuthRequestDto
    ): Response<AuthResponseDto>
    @Headers("Content-Type: application/json")
    @POST("api/usuarios")
    suspend fun createUser(
        @Body usuario: UsuarioDto
    ): Response<UsuarioDto>
}
