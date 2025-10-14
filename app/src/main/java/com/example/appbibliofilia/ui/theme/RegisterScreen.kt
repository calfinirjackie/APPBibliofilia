package com.example.appbibliofilia.ui.theme

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onBackClick: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var birthDateError by remember { mutableStateOf("") }

    val mintGreen = Color(0xFFD2EDDB)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    // 📅 DatePicker con validaciones realistas
    val datePickerDialog = DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(selectedYear, selectedMonth, selectedDay)
            val today = Calendar.getInstance()
            val age = today.get(Calendar.YEAR) - selectedYear

            when {
                selectedDate.after(today) -> {
                    birthDateError = "No puedes seleccionar una fecha futura"
                    birthDate = ""
                }
                age < 10 -> {
                    birthDateError = "Debes tener al menos 10 años"
                    birthDate = ""
                }
                age > 100 -> {
                    birthDateError = "Fecha no válida (mayor de 100 años)"
                    birthDate = ""
                }
                else -> {
                    birthDateError = ""
                    birthDate = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
                }
            }
        },
        year, month, day
    )

    datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Registro de Usuario") },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = mintGreen,
                    titleContentColor = Color.Black
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Crea tu cuenta", style = MaterialTheme.typography.headlineMedium)

            // Nombre
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    nameError = ""
                },
                label = { Text("Nombre completo") },
                modifier = Modifier.fillMaxWidth(),
                isError = nameError.isNotEmpty()
            )
            if (nameError.isNotEmpty()) {
                Text(nameError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
            }

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = ""
                },
                label = { Text("Correo electrónico") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                isError = emailError.isNotEmpty()
            )
            if (emailError.isNotEmpty()) {
                Text(emailError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
            }

            // Fecha de nacimiento
            OutlinedTextField(
                value = birthDate,
                onValueChange = {},
                label = { Text("Fecha de nacimiento") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { datePickerDialog.show() },
                readOnly = true,
                isError = birthDateError.isNotEmpty()
            )
            if (birthDateError.isNotEmpty()) {
                Text(birthDateError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
            }

            // Contraseña
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = ""
                },
                label = { Text("Contraseña") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                isError = passwordError.isNotEmpty()
            )
            if (passwordError.isNotEmpty()) {
                Text(passwordError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
            }

            // Botón Registrar
            Button(
                onClick = {
                    var valid = true

                    if (name.isBlank()) {
                        nameError = "El nombre no puede estar vacío"
                        valid = false
                    }
                    if (email.isBlank() || !email.contains("@")) {
                        emailError = "Correo inválido"
                        valid = false
                    }
                    if (birthDate.isBlank()) {
                        birthDateError = "Selecciona una fecha válida"
                        valid = false
                    }
                    if (password.length < 6) {
                        passwordError = "La contraseña debe tener al menos 6 caracteres"
                        valid = false
                    }

                    if (valid) {
                        scope.launch {
                            snackbarHostState.showSnackbar("Registro exitoso 🎉")
                        }
                        onRegisterSuccess()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = mintGreen,
                    contentColor = Color.Black
                )
            ) {
                Text("Registrar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(onBackClick = {}, onRegisterSuccess = {})
}
