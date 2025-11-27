package com.example.appbibliofilia.data.repository

import android.util.Log
import com.example.appbibliofilia.data.remote.TokenManager
import com.example.appbibliofilia.data.remote.api.UserApiService
import com.example.appbibliofilia.data.remote.model.AuthRequestDto
import com.example.appbibliofilia.data.remote.model.UsuarioDto

class UsersRemoteRepository(
    private val api: UserApiService,
    private val tokenManager: TokenManager
) {

    suspend fun login(username: String, password: String): Result<UsuarioDto> {
        return try {
            val resp = api.login(AuthRequestDto(username, password))
            if (resp.isSuccessful) {
                val body = resp.body() ?: return Result.failure(Exception("Empty body"))
                val token = body.token
                val usuario = body.usuario
                tokenManager.saveToken(token)
                Result.success(usuario)
            } else {
                Result.failure(Exception("Login failed: ${resp.code()}"))
            }
        } catch (e: Exception) {
            Log.e("UsersRemoteRepo", "login error", e)
            Result.failure(e)
        }
    }

    suspend fun createUser(usuario: UsuarioDto): Result<UsuarioDto> {
        return try {
            val resp = api.createUser(usuario)
            if (resp.isSuccessful) Result.success(resp.body()!!)
            else Result.failure(Exception("Create user failed: ${resp.code()}"))
        } catch (e: Exception) {
            Log.e("UsersRemoteRepo", "createUser error", e)
            Result.failure(e)
        }
    }

    fun logout() {
        tokenManager.clearToken()
    }
}
