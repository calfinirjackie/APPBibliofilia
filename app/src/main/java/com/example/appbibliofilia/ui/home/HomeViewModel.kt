package com.example.appbibliofilia.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.appbibliofilia.data.model.UserRecord

/**
 * ViewModel para la pantalla Home (login).
 * Se encarga del estado de los campos y de las validaciones, dejando
 * la interacción con repositorios al callback proporcionado por la UI.
 */
class HomeViewModel : ViewModel() {
    var username by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var errorMessage by mutableStateOf("")
        private set

    var isSubmitting by mutableStateOf(false)
        private set

    fun updateUsername(value: String) {
        username = value
    }

    fun updatePassword(value: String) {
        password = value
    }

    fun clearError() {
        errorMessage = ""
    }

    /**
     * Valida y ejecuta el callback de login proporcionado por la UI.
     * Retorna el UserRecord si el login fue exitoso o null en caso contrario.
     * También actualiza `errorMessage` apropiadamente.
     */
    fun handleLogin(onLoginAttempt: (username: String, password: String) -> UserRecord?): UserRecord? {
        // Validación de campos
        val fieldError = validateLoginFields(username, password)
        if (fieldError != null) {
            errorMessage = fieldError
            return null
        }

        // Ejecutar intento de login (sincrónico por contrato del callback usado actualmente)
        isSubmitting = true
        val user = try {
            onLoginAttempt(username, password)
        } finally {
            isSubmitting = false
        }

        // Mapear resultado a mensaje de error si aplica
        val loginError = loginErrorMessage(user)
        errorMessage = loginError ?: ""

        return user
    }

    // --------------------------------------------
    // Validadores (movidos aquí para encapsular la lógica)
    // --------------------------------------------

    fun validateLoginFields(username: String, password: String): String? {
        return if (username.isBlank() || password.isBlank()) {
            "Por favor completa todos los campos"
        } else null
    }

    private fun loginErrorMessage(user: UserRecord?): String? {
        return if (user == null) "Usuario o contraseña incorrectos" else null
    }
}
