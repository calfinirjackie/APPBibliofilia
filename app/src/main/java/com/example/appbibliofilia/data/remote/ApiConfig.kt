package com.example.appbibliofilia.data.remote

import com.example.appbibliofilia.data.remote.api.UserApiService
import com.example.appbibliofilia.data.remote.api.LibroApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {

    // Cambia estos BASE_URL si usas un servidor externo
    private const val BASE_URL_USUARIOS = "http://10.0.2.2:8090/"
    private const val BASE_URL_LIBROS   = "http://10.0.2.2:8070/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private fun buildRetrofit(baseUrl: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    // factory methods to avoid early resolution issues during static analysis
    fun createUserApi(): UserApiService = buildRetrofit(BASE_URL_USUARIOS).create(UserApiService::class.java)

    fun createLibroApi(): LibroApiService = buildRetrofit(BASE_URL_LIBROS).create(LibroApiService::class.java)
}
