package com.example.appbibliofilia.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appbibliofilia.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onRegisterClick: () -> Unit = {}) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val mintGreen = Color(0xFFD2EDDB)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("BIBLIOFILIA") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = mintGreen,
                    titleContentColor = Color.Black // color del texto del título
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "¡Donde tus libros encuentran su biblihogar!",
                color = MaterialTheme.colorScheme.onBackground
            )

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo Bibliofilia",
                modifier = Modifier.size(180.dp)
            )


            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Usuario") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )


            Button(
                onClick = {
                    if (username.isBlank() || password.isBlank()) {
                        errorMessage = "Por favor completa todos los campos"
                    } else {
                        errorMessage = ""
                        println("Iniciando sesión con $username / $password")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = mintGreen,
                    contentColor = Color.Black
                )
            ) {
                Text("Iniciar Sesión")
            }

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }


            Button(
                onClick = { onRegisterClick() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = mintGreen,
                    contentColor = Color.Black
                )
            ) {
                Text("Register")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
