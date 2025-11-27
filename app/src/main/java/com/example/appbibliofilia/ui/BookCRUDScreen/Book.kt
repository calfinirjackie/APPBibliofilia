package com.example.appbibliofilia.ui.BookCRUDScreen

enum class BookFormat { FISICO, DIGITAL }

data class Book(
    val id: Long,
    val name: String,
    val author: String,
    val format: BookFormat,
    // Nuevo: número de páginas (por defecto 0 para compatibilidad)
    val pages: Int = 0,
    // Nuevo: ISBN del libro (por defecto cadena vacía)
    val isbn: String = ""
)
