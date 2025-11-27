# Conectar app Android con microservicios (Usuarios y Libros)

Resumen rápido:
- La app ahora se conecta a dos microservicios:
  - ms-api-usuarios-bibliofilia (por defecto http://10.0.2.2:8090/)
  - ms-api-gestion-libro-bibliofilia (por defecto http://10.0.2.2:8070/)
- Archivos clave añadidos/actualizados:
  - `app/src/main/java/com/example/appbibliofilia/data/remote/ApiConfig.kt`
  - `app/src/main/java/com/example/appbibliofilia/data/remote/TokenManager.kt`
  - `app/src/main/java/com/example/appbibliofilia/data/remote/api/UserApiService.kt`
  - `app/src/main/java/com/example/appbibliofilia/data/remote/api/LibroApiService.kt`
  - DTOs: `AuthDto.kt`, `LibroDto.kt` y mappers en `data/remote/model`
  - Repositorios: `UsersRemoteRepository.kt`, `LibrosRepository.kt`
  - UI: `HomeScreen`, `RegisterScreen`, `BooksCrudScreen` updated to use repos; `BooksViewModel` integrado con `LibrosRepository`.

Pasos para levantar los microservicios (Windows, desde los repositorios locales):

1) Usuarios

- Abrir terminal PowerShell en la carpeta del microservicio `ms-api-usuarios-bibliofilia`.
- Ejecutar:

```powershell
cd C:\ruta\a\ms-api-usuarios-bibliofilia
.\mvnw.cmd spring-boot:run
```

2) Libros

- Abrir otra terminal PowerShell en `ms-api-gestion-libro-bibliofilia`.

```powershell
cd C:\ruta\a\ms-api-gestion-libro-bibliofilia
.\mvnw.cmd spring-boot:run
```

3) Verificar puertos

- Revisa `src/main/resources/application.properties` en cada microservicio para confirmar `server.port`.
- Si los puertos son distintos, actualiza `ApiConfig.kt` en la app con las URLs correctas.

Probar desde la máquina (curl / Postman):

- Login:
```powershell
curl -X POST -H "Content-Type: application/json" -d '{"username":"user@example.com","password":"secret"}' http://localhost:8090/auth/login
```

- Obtener libros (requiere token):
```powershell
curl -H "Authorization: Bearer <TOKEN>" http://localhost:8070/libro
```

Probar desde el emulador Android

- Asegúrate de usar `10.0.2.2` como host (reseñado en `ApiConfig.kt`).
- Ejecuta la app desde Android Studio en un emulador.
- En la pantalla de login, ingresa credenciales existentes del servicio de usuarios o regístrate desde la app.

Notas y troubleshooting

- 401 Unauthorized: revisa que el token que devuelve `/auth/login` se guarde y se envíe como `Bearer <token>`.
- Si usas un dispositivo físico, reemplaza `10.0.2.2` por la IP local de tu PC (por ejemplo `http://192.168.1.50:8090/`).
- Para ver requests/responses en Logcat, ya incluí `HttpLoggingInterceptor` en `ApiConfig`.

Siguientes mejoras sugeridas

- Añadir manejo de refresh tokens si el backend los expone.
- Mejorar persistencia del token con `EncryptedSharedPreferences`.
- Integrar Hilt para DI para evitar crear repos en `MainActivity`.

Si quieres, puedo:
- Ajustar `BooksViewModel` para soportar paginación.
- Añadir Snackbar/AlertDialogs más robustos para errores.
- Añadir tests unitarios para los repositorios (mock Retrofit).


