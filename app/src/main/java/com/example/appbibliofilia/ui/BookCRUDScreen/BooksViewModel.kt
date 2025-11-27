package com.example.appbibliofilia.ui.BookCRUDScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appbibliofilia.data.repository.LibrosRepository
import kotlinx.coroutines.launch

class BooksViewModel(private val repo: LibrosRepository? = null) : ViewModel() {
    private val _books = mutableStateListOf<Book>()
    val books: List<Book> get() = _books

    private var nextId = 1L

    private val _editingId = mutableStateOf<Long?>(null)
    val editingId: Long? get() = _editingId.value

    private val _isLoading = mutableStateOf(false)
    val isLoading: Boolean get() = _isLoading.value

    private val _errorMessage = mutableStateOf("")
    val errorMessage: String get() = _errorMessage.value

    init {
        // si recibimos repo, cargar al iniciar
        if (repo != null) {
            loadBooks()
        }
    }

    fun loadBooks() {
        val r = repo ?: return
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val res = r.getLibros()
                if (res.isSuccess) {
                    _books.clear()
                    _books.addAll(res.getOrThrow())
                    // asegurar nextId mayor que el mayor id en la lista
                    val maxId = _books.maxOfOrNull { it.id } ?: 0L
                    nextId = maxId + 1
                    _errorMessage.value = ""
                } else {
                    _errorMessage.value = res.exceptionOrNull()?.localizedMessage ?: "Error al cargar libros"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "Error inesperado"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addBook(name: String, author: String, format: BookFormat) {
        val book = Book(id = nextId++, name = name, author = author, format = format)
        _books.add(0, book)
        // intentar crear en remoto si hay repo
        repo?.let { r ->
            viewModelScope.launch {
                val res = r.crearLibro(book)
                if (res.isFailure) {
                    // opcional: marcar error pero mantener el libro local
                    _errorMessage.value = res.exceptionOrNull()?.localizedMessage ?: "Error al crear libro"
                }
            }
        }
    }

    fun updateBook(id: Long, name: String, author: String, format: BookFormat) {
        val index = _books.indexOfFirst { it.id == id }
        if (index >= 0) {
            val updated = Book(id = id, name = name, author = author, format = format)
            _books[index] = updated
            repo?.let { r ->
                viewModelScope.launch {
                    val res = r.actualizarLibro(updated)
                    if (res.isFailure) {
                        _errorMessage.value = res.exceptionOrNull()?.localizedMessage ?: "Error al actualizar libro"
                    }
                }
            }
        }
    }

    fun deleteBook(id: Long) {
        _books.removeAll { it.id == id }
        if (_editingId.value == id) _editingId.value = null
        repo?.let { r ->
            viewModelScope.launch {
                val res = r.eliminarLibro(id)
                if (res.isFailure) {
                    _errorMessage.value = res.exceptionOrNull()?.localizedMessage ?: "Error al eliminar libro"
                }
            }
        }
    }

    fun startEditing(id: Long) {
        _editingId.value = id
    }

    fun stopEditing() {
        _editingId.value = null
    }

    fun getBook(id: Long): Book? = _books.find { it.id == id }
}

// Factory simple para crear el ViewModel con repo
class BooksViewModelFactory(private val repo: LibrosRepository?) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BooksViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BooksViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
