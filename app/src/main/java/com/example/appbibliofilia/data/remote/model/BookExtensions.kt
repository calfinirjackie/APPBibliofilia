package com.example.appbibliofilia.data.remote.model

import com.example.appbibliofilia.ui.BookCRUDScreen.Book
import com.example.appbibliofilia.ui.BookCRUDScreen.BookFormat

fun Book.toLibroDto(): LibroDto =
    LibroDto(
        id = id,
        titulo = name,
        autor = author,
        paginas = null,
        isbn = null,
        formato = when (format) {
            BookFormat.FISICO -> "FISICO"
            BookFormat.DIGITAL -> "DIGITAL"
        }
    )
