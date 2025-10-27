package com.example.appbibliofilia.ui.main

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf

class BooksViewModel : ViewModel() {
    private val _books = mutableStateListOf<Book>()
    val books: List<Book> get() = _books

    private var nextId = 1L

    var editingId by mutableStateOf<Long?>(null)
        private set

    fun addBook(name: String, author: String, format: BookFormat) {
        val book = Book(id = nextId++, name = name, author = author, format = format)
        _books.add(0, book)
    }

    fun updateBook(id: Long, name: String, author: String, format: BookFormat) {
        val index = _books.indexOfFirst { it.id == id }
        if (index >= 0) {
            _books[index] = Book(id = id, name = name, author = author, format = format)
        }
    }

    fun deleteBook(id: Long) {
        _books.removeAll { it.id == id }
        if (editingId == id) editingId = null
    }

    fun startEditing(id: Long) {
        editingId = id
    }

    fun stopEditing() {
        editingId = null
    }

    fun getBook(id: Long): Book? = _books.find { it.id == id }
}

