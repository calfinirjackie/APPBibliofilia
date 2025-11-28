package com.example.appbibliofilia.data.repository

import com.example.appbibliofilia.data.remote.TokenManager
import com.example.appbibliofilia.data.remote.api.LibroApiService
import com.example.appbibliofilia.ui.BookCRUDScreen.Book
import com.example.appbibliofilia.ui.BookCRUDScreen.BookFormat
import com.example.appbibliofilia.data.remote.model.toBook
import com.example.appbibliofilia.data.remote.model.toLibroDto

class LibrosRepository(
    private val api: LibroApiService,
    private val tokenManager: TokenManager
) {

    private fun authHeader(): Result<String> {
        val token = tokenManager.getToken() ?: return Result.failure(Exception("No auth token"))
        return Result.success("Bearer $token")
    }

    suspend fun getLibros(): Result<List<Book>> {
        return try {
            val headerRes = authHeader()
            if (headerRes.isFailure) return Result.failure(Exception("No auth token"))
            val resp = api.getLibros(headerRes.getOrThrow())
            if (resp.isSuccessful) {
                val dtoList = resp.body() ?: emptyList()
                Result.success(dtoList.map { it.toBook() })
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
            val dto = book.toLibroDto()
            val resp = api.crearLibro(header, dto)
            if (resp.isSuccessful) {
                val body = resp.body() ?: return Result.failure(Exception("Empty body"))
                Result.success(body.toBook())
            } else Result.failure(Exception("Crear libro failed: ${resp.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun actualizarLibro(book: Book): Result<Book> {
        return try {
            val header = authHeader().getOrThrow()
            val dto = book.toLibroDto()
            val resp = api.actualizarLibro(header, book.id, dto)
            if (resp.isSuccessful) {
                val body = resp.body() ?: return Result.failure(Exception("Empty body"))
                Result.success(body.toBook())
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
