package com.example.appbibliofilia.data.repository

import com.example.appbibliofilia.data.remote.TokenManager
import com.example.appbibliofilia.data.remote.api.LibroApiService
import com.example.appbibliofilia.ui.BookCRUDScreen.Book
import com.example.appbibliofilia.ui.BookCRUDScreen.BookFormat

class LibrosRepository(
    private val api: LibroApiService,
    private val tokenManager: TokenManager
) {

    private fun authHeader(): Result<String> {
        val token = tokenManager.getToken() ?: return Result.failure(Exception("No auth token"))
        return Result.success("Bearer $token")
    }

    private fun dtoToBook(dto: com.example.appbibliofilia.data.remote.model.LibroDto): Book {
        val format = when (dto.formato?.uppercase()) {
            "FISICO" -> BookFormat.FISICO
            "DIGITAL" -> BookFormat.DIGITAL
            else -> BookFormat.DIGITAL
        }
        return Book(id = dto.id, name = dto.titulo, author = dto.autor, format = format)
    }

    private fun bookToDto(book: Book): com.example.appbibliofilia.data.remote.model.LibroDto {
        return com.example.appbibliofilia.data.remote.model.LibroDto(
            id = book.id,
            titulo = book.name,
            autor = book.author,
            paginas = null,
            isbn = null,
            formato = when (book.format) {
                BookFormat.FISICO -> "FISICO"
                BookFormat.DIGITAL -> "DIGITAL"
            }
        )
    }

    suspend fun getLibros(): Result<List<Book>> {
        return try {
            val headerRes = authHeader()
            if (headerRes.isFailure) return Result.failure(Exception("No auth token"))
            val resp = api.getLibros(headerRes.getOrThrow())
            if (resp.isSuccessful) {
                val dtoList = resp.body() ?: emptyList()
                Result.success(dtoList.map { dtoToBook(it) })
            } else {
                Result.failure(Exception("Get libros failed: ${resp.code()}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun crearLibro(book: Book): Result<Book> {
        return try {
            val header = authHeader().getOrThrow()
            val dto = bookToDto(book)
            val resp = api.crearLibro(header, dto)
            if (resp.isSuccessful) {
                val body = resp.body() ?: return Result.failure(Exception("Empty body"))
                Result.success(dtoToBook(body))
            } else Result.failure(Exception("Crear libro failed: ${resp.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun actualizarLibro(book: Book): Result<Book> {
        return try {
            val header = authHeader().getOrThrow()
            val dto = bookToDto(book)
            val resp = api.actualizarLibro(header, book.id, dto)
            if (resp.isSuccessful) {
                val body = resp.body() ?: return Result.failure(Exception("Empty body"))
                Result.success(dtoToBook(body))
            } else Result.failure(Exception("Actualizar libro failed: ${resp.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun eliminarLibro(id: Long): Result<Unit> {
        return try {
            val header = authHeader().getOrThrow()
            val resp = api.eliminarLibro(header, id)
            if (resp.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Eliminar libro failed: ${resp.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
