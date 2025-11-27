package com.example.appbibliofilia.ui.register

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Density
import androidx.compose.ui.platform.LocalDensity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onBackClick: () -> Unit,
    onRegisterSuccess: (name: String?, email: String?) -> Unit,
    usersRemoteRepo: com.example.appbibliofilia.data.repository.UsersRemoteRepository? = null
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

    // Haptic feedback para vibración en caso de errores de validación (sutil)
    val haptic = LocalHapticFeedback.current

    // Vibrator para una vibración más fuerte y controlada
    val vibrator = remember { context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator }

    // Animación de shake: offset horizontal en pixeles
    val density = LocalDensity.current
    val shakeOffset = remember { Animatable(0f) }
    var shakeTrigger by remember { mutableStateOf(0) }

    fun vibrateStrong() {
        vibrator?.let { v ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                try {
                    // patrón: pausa 0, vibrar 100, pausa 30, vibrar 120
                    val timings = longArrayOf(0, 100, 30, 120)
                    val amplitudes = intArrayOf(0, 255, 0, 255)
                    val effect = VibrationEffect.createWaveform(timings, amplitudes, -1)
                    v.vibrate(effect)
                } catch (e: Throwable) {
                    // fallback
                    try {
                        v.vibrate(300)
                    } catch (_: Throwable) {
                    }
                }
            } else {
                @Suppress("DEPRECATION")
                v.vibrate(300)
            }
        }
    }

    // instancia del ViewModel para validaciones
    val registerViewModel: RegisterViewModel = viewModel()

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    // lanzar animación de shake cuando cambie el trigger
    LaunchedEffect(shakeTrigger) {
        if (shakeTrigger <= 0) return@LaunchedEffect
        val px = with(density) { 12.dp.toPx() }
        repeat(4) { i ->
            val target = if (i % 2 == 0) px else -px
            shakeOffset.animateTo(target, animationSpec = tween(durationMillis = 60))
        }
        shakeOffset.animateTo(0f, animationSpec = tween(durationMillis = 60))
    }

    // 📅 DatePicker con validaciones delegadas al ViewModel
    val datePickerDialog = DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            val err = registerViewModel.validateBirthDateComponents(selectedYear, selectedMonth, selectedDay)
            if (err.isNotEmpty()) {
                birthDateError = err
                birthDate = ""
                // vibrar al marcar error de fecha: haptic + fuerte + shake
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                vibrateStrong()
                shakeTrigger++
            } else {
                birthDateError = ""
                birthDate = registerViewModel.formatDate(selectedDay, selectedMonth, selectedYear)
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
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
                .offset { IntOffset(shakeOffset.value.roundToInt(), 0) }
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
                    // delegar validaciones al ViewModel
                    val validation = registerViewModel.validateAll(name, email, password, birthDate)
                    nameError = validation.nameError
                    emailError = validation.emailError
                    passwordError = validation.passwordError
                    birthDateError = validation.birthDateError

                    if (validation.isValid) {
                        scope.launch {
                            // Intentar crear usuario en remoto si se proporcionó repo
                            var ok = true
                            usersRemoteRepo?.let { repo ->
                                val dto = com.example.appbibliofilia.data.remote.model.UsuarioDto(
                                    idUsuario = 0L,
                                    nombre = name,
                                    correo = email,
                                    contrasena = password,
                                    rol = null
                                )
                                val res = repo.createUser(dto)
                                if (res.isFailure) {
                                    ok = false
                                    snackbarHostState.showSnackbar("Error al registrar: ${res.exceptionOrNull()?.localizedMessage}")
                                }
                            }

                            if (ok) {
                                val snackbarJob = launch {
                                    snackbarHostState.showSnackbar(
                                        "Registro exitoso 🎉",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                                delay(1000)
                                snackbarJob.cancel()
                                // pasamos name y email al onRegisterSuccess
                                onRegisterSuccess(name, email)
                            }
                        }
                    } else {
                        // si hay errores, producir vibración fuerte y animación shake
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        vibrateStrong()
                        shakeTrigger++
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
    RegisterScreen(onBackClick = {}, onRegisterSuccess = { _, _ -> })
}
