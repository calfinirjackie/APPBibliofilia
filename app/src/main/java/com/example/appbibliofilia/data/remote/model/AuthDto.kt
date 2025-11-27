package com.example.appbibliofilia.data.remote.model

data class AuthRequestDto(
    val username: String, // correo que ingresa el usuario
    val password: String
)

data class AuthResponseDto(
    val token: String,
    val usuario: UsuarioDto
)

data class UsuarioDto(
    val idUsuario: Long,
    val nombre: String,
    val correo: String,
    val contrasena: String?,
    val rol: String?
)

