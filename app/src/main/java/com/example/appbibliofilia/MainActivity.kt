package com.example.appbibliofilia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appbibliofilia.ui.theme.APPBibliofiliaTheme
import com.example.appbibliofilia.ui.home.HomeScreen
import com.example.appbibliofilia.ui.register.RegisterScreen
import com.example.appbibliofilia.ui.loading.LoadingScreen
import com.example.appbibliofilia.ui.main.MainScreen
import com.example.appbibliofilia.ui.BookCRUDScreen.BooksCrudScreen
import com.example.appbibliofilia.data.local.SessionRepository
import com.example.appbibliofilia.data.repository.UsersRepository
import com.example.appbibliofilia.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            APPBibliofiliaTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    AppNavigation(navController)
                }
            }
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController, appViewModel: MainViewModel = viewModel()) {
    val context = LocalContext.current
    val repo = SessionRepository(context)
    // Instanciar TokenManager y APIs/remotes
    val tokenManager = com.example.appbibliofilia.data.remote.TokenManager(context)
    val userApi = com.example.appbibliofilia.data.remote.ApiConfig.createUserApi()
    val libroApi = com.example.appbibliofilia.data.remote.ApiConfig.createLibroApi()
    val usersRemoteRepo = com.example.appbibliofilia.data.repository.UsersRemoteRepository(userApi, tokenManager)
    val librosRepo = com.example.appbibliofilia.data.repository.LibrosRepository(libroApi, tokenManager)
    val usersRepo = UsersRepository(context)
    val session by appViewModel.session.collectAsState()
    val loaded = remember { mutableStateOf(false) }

    // Cargar la sesión al inicio y navegar a main si ya está logueado
    LaunchedEffect(Unit) {
        // cargar desde repo y establecer en ViewModel
        appViewModel.loadData(repo)
        loaded.value = true
        if (appViewModel.session.value.isLoggedIn) {
            navController.navigate("main") {
                popUpTo("loading") { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    // Guardar cada vez que cambie session, pero sólo después de la primera carga
    LaunchedEffect(loaded.value, session) {
        if (loaded.value) {
            appViewModel.saveData(repo)
        }
    }

    NavHost(navController = navController, startDestination = "loading") {

        // 🔹 Pantalla de carga inicial
        composable("loading") {
            LoadingScreen(onTimeout = {
                navController.navigate("home") {
                    popUpTo("loading") { inclusive = true }
                }
            })
        }

        // 🔹 Pantalla principal (Home)
        composable("home") {
            HomeScreen(
                onRegisterClick = { navController.navigate("register") },
                onLoginAttempt = { username, password ->
                    // Intento de login remoto; si falla usar el repo local como fallback
                    val remoteResult = try {
                        usersRemoteRepo.login(username, password)
                    } catch (e: Exception) {
                        Result.failure<com.example.appbibliofilia.data.remote.model.UsuarioDto>(e)
                    }

                    val userRecord = if (remoteResult.isSuccess) {
                        val usuarioDto = remoteResult.getOrNull()!!
                        // mapear UsuarioDto a UserRecord
                        val ur = com.example.appbibliofilia.data.model.UserRecord(
                            username = username,
                            password = password,
                            name = usuarioDto.nombre.takeIf { it.isNotBlank() },
                            email = usuarioDto.correo.takeIf { it.isNotBlank() }
                        )
                        // actualizar sesión
                        appViewModel.login(name = ur.name, email = ur.email)
                        navController.navigate("main") {
                            popUpTo("home") { inclusive = true }
                            launchSingleTop = true
                        }
                        ur
                    } else {
                        // fallback a repo local (assets)
                        usersRepo.findUser(username, password)
                    }

                    userRecord
                }
            )
        }

        // Mostrar la pantalla principal de la app en la ruta "main"
        composable("main") {
            MainScreen(isLoggedIn = session.isLoggedIn, userName = session.name, onLogout = {
                appViewModel.logout()
                navController.navigate("home") {
                    popUpTo("main") { inclusive = true }
                    launchSingleTop = true
                }
            }, onOpenBooks = { navController.navigate("books") })
        }

        // 🔹 Pantalla de registro con navegación de retorno
        composable("register") {
            RegisterScreen(
                onBackClick = { navController.popBackStack() },
                onRegisterSuccess = { name, email ->
                    // marcar estado de login antes de navegar y persistir
                    appViewModel.login(name = name, email = email)
                    navController.navigate("main") {
                        popUpTo("home") { inclusive = true }
                        launchSingleTop = true
                    }
                },
                usersRemoteRepo = usersRemoteRepo
            )
        }

        // Nueva ruta para el CRUD de libros
        composable("books") {
            BooksCrudScreen(librosRepo = librosRepo, onBack = { navController.popBackStack() })
        }
    }
}
