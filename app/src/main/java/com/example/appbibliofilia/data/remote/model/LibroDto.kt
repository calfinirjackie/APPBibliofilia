package com.example.appbibliofilia.data.remote.model

data class LibroDto(
    val id: Long,
    val titulo: String,
    val autor: String,
    val paginas: Int?,
    val isbn: String?,
    val formato: String? // "FISICO" / "DIGITAL"
)

