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

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onRegisterClick = { navController.navigate("register") }
            )
        }
        composable("register") {
            RegisterScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
