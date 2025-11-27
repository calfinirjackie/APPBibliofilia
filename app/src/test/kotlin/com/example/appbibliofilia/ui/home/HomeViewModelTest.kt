package com.example.appbibliofilia.ui.home

import com.example.appbibliofilia.data.model.UserRecord
import org.junit.Assert.*
import org.junit.Test

class HomeViewModelTest {

    @Test
    fun `handleLogin with empty fields returns null and sets errorMessage`() {
        val vm = HomeViewModel()

        // username y password iniciales son "" por defecto
        val user = vm.handleLogin { _, _ ->
            // no debería llamarse si los campos están vacíos
            fail("El callback de login no debería ejecutarse cuando faltan campos")
            null
        }

        assertNull(user)
        assertEquals("Por favor completa todos los campos", vm.errorMessage)
        assertFalse(vm.isSubmitting)
    }

    @Test
    fun `handleLogin with invalid credentials sets incorrect user error`() {
        val vm = HomeViewModel()
        vm.updateUsername("jackie")
        vm.updatePassword("wrong")

        val user = vm.handleLogin { _, _ -> null }

        assertNull(user)
        assertEquals("Usuario o contraseña incorrectos", vm.errorMessage)
        assertFalse(vm.isSubmitting)
    }

    @Test
    fun `handleLogin with valid user clears errorMessage and returns user`() {
        val vm = HomeViewModel()
        vm.updateUsername("jackie")
        vm.updatePassword("secret")

        val expected = UserRecord(
            username = "jackie",
            password = "secret",
            name = "Jackie",
            email = "j@example.com"
        )

        var callbackCalled = false

        val result = vm.handleLogin { username, password ->
            callbackCalled = true
            assertEquals("jackie", username)
            assertEquals("secret", password)
            expected
        }

        assertTrue(callbackCalled)
        assertEquals(expected, result)
        assertEquals("", vm.errorMessage)
        assertFalse(vm.isSubmitting)
    }

    @Test
    fun `updateUsername and updatePassword update state and clear previous error`() {
        val vm = HomeViewModel()

        // Forzamos un error previo
        val user = vm.handleLogin { _, _ -> null }
        assertNull(user)
        assertEquals("Por favor completa todos los campos", vm.errorMessage)

        vm.updateUsername("nuevoUser")
        vm.updatePassword("nuevaPass")
        vm.clearError()

        assertEquals("nuevoUser", vm.username)
        assertEquals("nuevaPass", vm.password)
        assertEquals("", vm.errorMessage)
    }
}

