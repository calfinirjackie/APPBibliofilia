package com.example.appbibliofilia.ui.BookCRUDScreen

import org.junit.Assert.*
import org.junit.Test

class BooksViewModelTest {

    @Test
    fun `addBook adds a book with incremental id and inserts at beginning`() {
        val vm = BooksViewModel()

        vm.addBook("Libro 1", "Autor 1", BookFormat.FISICO)
        vm.addBook("Libro 2", "Autor 2", BookFormat.DIGITAL)

        val books = vm.books
        assertEquals(2, books.size)

        // El último agregado va primero
        assertEquals("Libro 2", books[0].name)
        assertEquals("Libro 1", books[1].name)

        // IDs autoincrementales locales
        assertEquals(2L, books[0].id)
        assertEquals(1L, books[1].id)
    }

    @Test
    fun `updateBook changes fields but keeps same id`() {
        val vm = BooksViewModel()
        vm.addBook("Libro 1", "Autor 1", BookFormat.FISICO)
        val original = vm.books.first()

        vm.updateBook(
            id = original.id,
            name = "Nuevo nombre",
            author = "Nuevo autor",
            format = BookFormat.DIGITAL
        )

        val updated = vm.books.first()
        assertEquals(original.id, updated.id)
        assertEquals("Nuevo nombre", updated.name)
        assertEquals("Nuevo autor", updated.author)
        assertEquals(BookFormat.DIGITAL, updated.format)
    }

    @Test
    fun `deleteBook removes book and clears editingId if it was being edited`() {
        val vm = BooksViewModel()
        vm.addBook("Libro 1", "Autor 1", BookFormat.FISICO)
        vm.addBook("Libro 2", "Autor 2", BookFormat.DIGITAL)

        val toDeleteId = vm.books.first().id
        vm.startEditing(toDeleteId)
        assertEquals(toDeleteId, vm.editingId)

        vm.deleteBook(toDeleteId)

        assertEquals(1, vm.books.size)
        assertNull(vm.editingId)
        assertTrue(vm.books.none { it.id == toDeleteId })
    }

    @Test
    fun `getBook returns book when id exists and null otherwise`() {
        val vm = BooksViewModel()
        vm.addBook("Libro 1", "Autor 1", BookFormat.FISICO)
        val id = vm.books.first().id

        val found = vm.getBook(id)
        assertNotNull(found)
        assertEquals("Libro 1", found?.name)

        val missing = vm.getBook(999L)
        assertNull(missing)
    }
}

