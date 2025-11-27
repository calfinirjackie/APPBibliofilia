package com.example.appbibliofilia.data.remote.model

import com.example.appbibliofilia.ui.BookCRUDScreen.Book
import com.example.appbibliofilia.ui.BookCRUDScreen.BookFormat

@Suppress("unused")
fun LibroDto.toBook(): Book =
    Book(
        id = id,
        name = titulo,
        author = autor,
        format = when (formato?.uppercase()) {
            "FISICO" -> BookFormat.FISICO
            "DIGITAL" -> BookFormat.DIGITAL
            else -> BookFormat.DIGITAL
        },
        pages = paginas ?: 0,
        isbn = isbn ?: ""
    )
