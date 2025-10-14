package com.example.appbibliofilia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appbibliofilia.ui.theme.APPBibliofiliaTheme
import com.example.appbibliofilia.ui.theme.HomeScreen
import com.example.appbibliofilia.ui.theme.RegisterScreen
import com.example.appbibliofilia.ui.theme.LoadingScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            APPBibliofiliaTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    AppNavigation(navController)
                }
            }
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {
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
                onRegisterClick = { navController.navigate("register") }
            )
        }

        // 🔹 Pantalla de registro con navegación de retorno
        composable("register") {
            RegisterScreen(
                onBackClick = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate("home") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }
    }
}
