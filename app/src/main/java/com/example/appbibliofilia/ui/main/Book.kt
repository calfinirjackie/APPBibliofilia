package com.example.appbibliofilia.ui.main

enum class BookFormat { FISICO, DIGITAL }

data class Book(
    val id: Long,
    val name: String,
    val author: String,
    val format: BookFormat
)

