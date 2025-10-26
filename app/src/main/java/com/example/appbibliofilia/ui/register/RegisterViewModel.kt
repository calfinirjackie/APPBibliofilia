package com.example.appbibliofilia.ui.register

import androidx.lifecycle.ViewModel
import java.util.*
import java.util.Locale

data class RegisterValidationResult(
    val nameError: String = "",
    val emailError: String = "",
    val passwordError: String = "",
    val birthDateError: String = "",
) {
    val isValid: Boolean
        get() = nameError.isEmpty() && emailError.isEmpty() && passwordError.isEmpty() && birthDateError.isEmpty()
}

class RegisterViewModel : ViewModel() {

    fun validateName(name: String): String {
        return if (name.isBlank()) "El nombre no puede estar vacío" else ""
    }

    fun validateEmail(email: String): String {
        return if (email.isBlank() || !email.contains("@")) "Correo inválido" else ""
    }

    fun validatePassword(password: String): String {
        return if (password.length < 6) "La contraseña debe tener al menos 6 caracteres" else ""
    }

    /**
     * Valida la fecha a partir de componentes (year, month, day).
     * Devuelve una cadena vacía si es válida o un mensaje de error.
     */
    fun validateBirthDateComponents(selectedYear: Int, selectedMonth: Int, selectedDay: Int): String {
        val selectedDate = Calendar.getInstance()
        selectedDate.set(selectedYear, selectedMonth, selectedDay)
        val today = Calendar.getInstance()
        val age = today.get(Calendar.YEAR) - selectedYear

        return when {
            selectedDate.after(today) -> "No puedes seleccionar una fecha futura"
            age < 10 -> "Debes tener al menos 10 años"
            age > 100 -> "Fecha no válida (mayor de 100 años)"
            else -> ""
        }
    }

    fun formatDate(selectedDay: Int, selectedMonth: Int, selectedYear: Int): String {
        return String.format(Locale.US, "%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
    }

    fun validateBirthDateString(birthDate: String): String {
        return if (birthDate.isBlank()) "Selecciona una fecha válida" else ""
    }

    fun validateAll(name: String, email: String, password: String, birthDate: String): RegisterValidationResult {
        val n = validateName(name)
        val e = validateEmail(email)
        val p = validatePassword(password)
        val b = validateBirthDateString(birthDate)
        return RegisterValidationResult(n, e, p, b)
    }
}
