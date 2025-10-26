package com.example.appbibliofilia.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import com.example.appbibliofilia.data.model.Session
import com.example.appbibliofilia.data.local.SessionRepository

/**
 * ViewModel responsable de la gestión de estado global de la app (sesión del usuario).
 * Mantiene un StateFlow<Session> y métodos para login/logout y persistencia.
 */
class MainViewModel : ViewModel() {
    private val _session = MutableStateFlow(Session())
    val session: StateFlow<Session> = _session.asStateFlow()

    fun login(name: String? = null, email: String? = null) {
        _session.value = _session.value.copy(isLoggedIn = true, name = name, email = email)
    }

    fun logout() {
        _session.value = Session()
    }

    suspend fun loadData(repo: SessionRepository) {
        val loaded: Session = withContext(Dispatchers.IO) { repo.loadData() }
        _session.value = loaded
    }

    suspend fun saveData(repo: SessionRepository) {
        withContext(Dispatchers.IO) { repo.saveData(_session.value) }
    }
}

