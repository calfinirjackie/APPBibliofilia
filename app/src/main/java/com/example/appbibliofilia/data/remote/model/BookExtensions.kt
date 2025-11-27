package com.example.appbibliofilia.data.remote.model

import com.example.appbibliofilia.ui.BookCRUDScreen.Book
import com.example.appbibliofilia.ui.BookCRUDScreen.BookFormat

@Suppress("unused")
fun Book.toLibroDto(): LibroDto =
    LibroDto(
        id = id,
        titulo = name,
        autor = author,
        paginas = pages,
        isbn = isbn,
        formato = when (format) {
            BookFormat.FISICO -> "FISICO"
            BookFormat.DIGITAL -> "DIGITAL"
        }
    )
