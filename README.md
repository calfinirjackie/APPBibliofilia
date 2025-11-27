# Bibliofilia - Aplicación de Gestión de Biblioteca Personal

## Descripción del Proyecto
Bibliofilia es una aplicación móvil Android desarrollada en Kotlin que permite a los usuarios gestionar su biblioteca personal de libros, realizar un seguimiento de su progreso de lectura y organizar sus colecciones literarias.

## Integrantes
- **Sebastián Zanabria**
- **Jacqueline Calfinir**

## Funcionalidades

### 1. Autenticación de Usuarios
- **Registro de usuarios**: Creación de nuevas cuentas con validación de datos
- **Inicio de sesión**: Acceso seguro con gestión de tokens JWT
- **Validación de formularios**: Verificación de email, contraseña y datos de usuario
- **Retroalimentación háptica**: Vibración del dispositivo en caso de errores de validación

### 2. Gestión de Libros (CRUD)
- **Listar libros**: Visualización de todos los libros registrados
- **Crear libro**: Agregar nuevos libros a la biblioteca personal
- **Actualizar libro**: Editar información de libros existentes
- **Eliminar libro**: Remover libros de la biblioteca

### 3. Pantalla Principal (Dashboard)
- **Tarjetas informativas** con las siguientes funcionalidades:
  - **Información del libro actual**: Visualización de libro físico o digital, páginas de progreso y colección
  - **Timer de lectura**: Sesiones con temporizador para registrar progresos
  - **Registro de libros leídos**: Historial, reportes, opiniones, notas y objetivos de lectura
  - **Colecciones**: Organización por sagas, géneros, autores, y categorización por formato (físico/digital)

### 4. Interfaz de Usuario
- **Diseño MVVM**: Arquitectura moderna con separación de responsabilidades
- **Material Design**: UI moderna con Jetpack Compose
- **Tema personalizado**: Colores personalizados con verde menta como color principal
- **Animaciones**: Efectos visuales y vibración para mejorar la experiencia de usuario

## Endpoints Utilizados

### API Externa - Microservicio de Usuarios
**Base URL**: `http://10.0.2.2:8090/` (Emulador Android)

| Método | Endpoint | Descripción | Autenticación |
|--------|----------|-------------|---------------|
| POST | `/auth/login` | Iniciar sesión con credenciales | No |
| POST | `/api/usuarios` | Crear nuevo usuario | No |

**Ejemplo de Request - Login:**
```json
{
  "username": "user@example.com",
  "password": "secret123"
}
```

**Ejemplo de Response - Login:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "usuario": {
    "id": 1,
    "username": "user@example.com",
    "nombre": "Usuario"
  }
}
```

### Microservicio de Gestión de Libros
**Base URL**: `http://10.0.2.2:8070/` (Emulador Android)

| Método | Endpoint | Descripción | Autenticación |
|--------|----------|-------------|---------------|
| GET | `/libro` | Obtener lista de libros | Bearer Token |
| POST | `/libro` | Crear un nuevo libro | Bearer Token |
| PUT | `/libro/{id}` | Actualizar libro existente | Bearer Token |
| DELETE | `/libro/{id}` | Eliminar libro por ID | Bearer Token |

**Ejemplo de Request - Crear Libro:**
```json
{
  "titulo": "Cien años de soledad",
  "autor": "Gabriel García Márquez",
  "isbn": "978-0307474728",
  "genero": "Realismo mágico",
  "anioPublicacion": 1967,
  "estado": "LEYENDO"
}
```

**Headers requeridos para endpoints protegidos:**
```
Authorization: Bearer {token}
Content-Type: application/json
```

## Pasos para Ejecutar el Proyecto

### Prerrequisitos
- Android Studio (versión Arctic Fox o superior)
- JDK 11 o superior
- Gradle 7.0+
- Emulador Android o dispositivo físico (Android 7.0+)
- Los microservicios backend ejecutándose localmente

### 1. Configuración de los Microservicios Backend

#### Microservicio de Usuarios
```powershell
# Windows
cd C:\ruta\a\ms-api-usuarios-bibliofilia
.\mvnw.cmd spring-boot:run

# macOS/Linux
cd /ruta/a/ms-api-usuarios-bibliofilia
./mvnw spring-boot:run
```

El servicio estará disponible en: `http://localhost:8090`

#### Microservicio de Libros
```powershell
# Windows
cd C:\ruta\a\ms-api-gestion-libro-bibliofilia
.\mvnw.cmd spring-boot:run

# macOS/Linux
cd /ruta/a/ms-api-gestion-libro-bibliofilia
./mvnw spring-boot:run
```

El servicio estará disponible en: `http://localhost:8070`

### 2. Configuración de la Aplicación Android

#### Clonar el Repositorio
```bash
git clone [URL_DEL_REPOSITORIO]
cd APPBibliofilia
```

#### Configurar URLs de API (si es necesario)
Si los microservicios están en puertos diferentes, edita el archivo:
```
app/src/main/java/com/example/appbibliofilia/data/remote/ApiConfig.kt
```

Para **emulador Android**, usa:
```kotlin
private const val BASE_URL_USUARIOS = "http://10.0.2.2:8090/"
private const val BASE_URL_LIBROS   = "http://10.0.2.2:8070/"
```

Para **dispositivo físico**, usa la IP de tu computadora:
```kotlin
private const val BASE_URL_USUARIOS = "http://192.168.1.XXX:8090/"
private const val BASE_URL_LIBROS   = "http://192.168.1.XXX:8070/"
```

### 3. Compilar y Ejecutar

1. Abre el proyecto en Android Studio
2. Espera a que Gradle sincronice las dependencias
3. Conecta un dispositivo Android o inicia un emulador
4. Haz clic en el botón "Run" o presiona `Shift + F10`

### 4. Verificar Conectividad (Opcional)

#### Probar endpoint de Login
```bash
curl -X POST http://localhost:8090/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user@example.com","password":"secret"}'
```

#### Probar endpoint de Libros (requiere token)
```bash
curl -X GET http://localhost:8070/libro \
  -H "Authorization: Bearer {TU_TOKEN_AQUI}"
```

## Arquitectura del Proyecto

### Patrón MVVM (Model-View-ViewModel)
```
app/src/main/java/com/example/appbibliofilia/
├── data/
│   ├── local/          # Base de datos local (Room)
│   ├── remote/         # APIs y servicios remotos
│   │   ├── api/        # Interfaces Retrofit
│   │   └── model/      # DTOs y modelos de red
│   └── repository/     # Repositorios para acceso a datos
├── ui/
│   ├── home/           # Pantalla de login
│   ├── register/       # Pantalla de registro
│   ├── main/           # Pantalla principal (dashboard)
│   ├── BookCRUDScreen/ # Gestión de libros
│   ├── loading/        # Pantalla de carga
│   └── viewmodel/      # ViewModels compartidos
└── MainActivity.kt     # Actividad principal
```

## 🛠️ Tecnologías Utilizadas

- **Lenguaje**: Kotlin
- **UI Framework**: Jetpack Compose
- **Arquitectura**: MVVM
- **Networking**: Retrofit + OkHttp
- **Serialización**: Gson
- **Corrutinas**: Kotlin Coroutines
- **Inyección de Dependencias**: Manual (Factory pattern)
- **Manejo de Estado**: ViewModel + State
- **Logging**: HttpLoggingInterceptor

## Notas Técnicas

### Autenticación
- El token JWT se almacena en `SharedPreferences` mediante `TokenManager`
- Todos los endpoints de libros requieren el header `Authorization: Bearer {token}`
- La sesión persiste hasta que el usuario cierre sesión o el token expire

### Validaciones
- Los ViewModels contienen toda la lógica de validación
- Las validaciones incluyen: formato de email, longitud de contraseña, campos requeridos
- Retroalimentación visual y háptica en caso de errores

### Troubleshooting Común

| Problema | Solución |
|----------|----------|
| Error 401 Unauthorized | Verifica que el token se esté enviando correctamente |
| No se puede conectar al servidor | Verifica que los microservicios estén ejecutándose |
| Error de red en dispositivo físico | Cambia `10.0.2.2` por la IP local de tu PC |
| Error de compilación | Ejecuta `./gradlew clean build` |

## Seguridad

- Tokens JWT para autenticación
- Validación de entrada en cliente y servidor
- HTTPS recomendado para producción
- Considerar `EncryptedSharedPreferences` para almacenamiento de tokens en producción

## Licencia

Este proyecto es parte de un trabajo académico.

## Contacto

Para consultas sobre el proyecto, contactar a los integrantes del equipo.

---

**Versión**: 1.0.0  
**Última actualización**: Noviembre 2025

