package com.example.appbibliofilia.ui.register

import org.junit.Assert.*
import org.junit.Test
import java.util.Calendar

class RegisterViewModelUnitTest {
    private val vm = RegisterViewModel()

    @Test
    fun `validateName returns error when empty`() {
        assertEquals("El nombre no puede estar vacío", vm.validateName(""))
    }

    @Test
    fun `validateName returns empty when valid`() {
        assertEquals("", vm.validateName("Juan Pérez"))
    }

    @Test
    fun `validateEmail returns error for empty and invalid`() {
        assertEquals("Correo inválido", vm.validateEmail(""))
        assertEquals("Correo inválido", vm.validateEmail("useratexample.com"))
    }

    @Test
    fun `validateEmail returns empty when valid`() {
        assertEquals("", vm.validateEmail("user@example.com"))
    }

    @Test
    fun `validatePassword enforces minimum length`() {
        assertEquals("La contraseña debe tener al menos 6 caracteres", vm.validatePassword("12345"))
        assertEquals("", vm.validatePassword("123456"))
    }

    @Test
    fun `formatDate formats correctly`() {
        val formatted = vm.formatDate(1, 0, 2000) // month 0 => January
        assertEquals("01/01/2000", formatted)
    }

    @Test
    fun `validateBirthDateString handles empty and valid`() {
        assertEquals("Selecciona una fecha válida", vm.validateBirthDateString(""))
        assertEquals("", vm.validateBirthDateString("01/01/2000"))
    }

    @Test
    fun `validateBirthDateComponents rejects future dates`() {
        val today = Calendar.getInstance()
        val futureYear = today.get(Calendar.YEAR) + 1
        val month = today.get(Calendar.MONTH)
        val day = today.get(Calendar.DAY_OF_MONTH)
        assertEquals("No puedes seleccionar una fecha futura", vm.validateBirthDateComponents(futureYear, month, day))
    }

    @Test
    fun `validateBirthDateComponents rejects too young (under 10)`() {
        val today = Calendar.getInstance()
        val yearTooYoung = today.get(Calendar.YEAR) - 9 // age 9
        assertEquals("Debes tener al menos 10 años", vm.validateBirthDateComponents(yearTooYoung, 0, 1))
    }

    @Test
    fun `validateBirthDateComponents rejects too old (over 100)`() {
        val today = Calendar.getInstance()
        val yearTooOld = today.get(Calendar.YEAR) - 101
        assertEquals("Fecha no válida (mayor de 100 años)", vm.validateBirthDateComponents(yearTooOld, 0, 1))
    }

    @Test
    fun `validateAll aggregates errors`() {
        val result = vm.validateAll("", "", "123", "")
        assertFalse(result.isValid)
        assertEquals("El nombre no puede estar vacío", result.nameError)
        assertEquals("Correo inválido", result.emailError)
        assertEquals("La contraseña debe tener al menos 6 caracteres", result.passwordError)
        assertEquals("Selecciona una fecha válida", result.birthDateError)
    }
}

