package com.example.appbibliofilia.ui.home

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appbibliofilia.R
import com.example.appbibliofilia.data.model.UserRecord
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onRegisterClick: () -> Unit = {},
    // ahora acepta una lambda suspend para login remoto
    onLoginAttempt: suspend (username: String, password: String) -> UserRecord? = { _, _ -> null },
    homeViewModelParam: HomeViewModel? = null
) {
    // Resolver el ViewModel: usar la instancia pasada (útil para tests) o obtenerla del ViewModelStore
    val homeViewModel: HomeViewModel = homeViewModelParam ?: viewModel()

    // El estado ahora vive en HomeViewModel
    val username = homeViewModel.username
    val password = homeViewModel.password
    var isSubmitting by remember { mutableStateOf(false) }

    val mintGreen = Color(0xFFD2EDDB)

    // Vibrator (fuerte)
    val context = LocalContext.current
    val vibrator = remember { context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator }

    // Animación de shake: offset horizontal
    val density = LocalDensity.current
    val shakeOffset = remember { Animatable(0f) }
    var shakeTrigger by remember { mutableStateOf(0) }

    fun vibrateStrong() {
        vibrator?.let { v ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                try {
                    val timings = longArrayOf(0, 100, 40, 120)
                    val amplitudes = intArrayOf(0, 255, 0, 255)
                    val effect = VibrationEffect.createWaveform(timings, amplitudes, -1)
                    v.vibrate(effect)
                } catch (_: Throwable) {
                    @Suppress("DEPRECATION")
                    v.vibrate(300)
                }
            } else {
                @Suppress("DEPRECATION")
                v.vibrate(300)
            }
        }
    }

    // Lanzar animación de shake cuando cambie trigger
    LaunchedEffect(shakeTrigger) {
        if (shakeTrigger <= 0) return@LaunchedEffect
        val px = with(density) { 10.dp.toPx() }
        repeat(4) { i ->
            val target = if (i % 2 == 0) px else -px
            shakeOffset.animateTo(target, animationSpec = tween(durationMillis = 60))
        }
        shakeOffset.animateTo(0f, animationSpec = tween(durationMillis = 60))
    }

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
                .offset { IntOffset(shakeOffset.value.roundToInt(), 0) }
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
                onValueChange = {
                    homeViewModel.updateUsername(it)
                    homeViewModel.clearError()
                },
                label = { Text("Correo") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = {
                    homeViewModel.updatePassword(it)
                    homeViewModel.clearError()
                },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )


            var loginTrigger by remember { mutableStateOf(0) }

            if (isSubmitting) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            Button(
                onClick = {
                    // Validación local usando el ViewModel
                    val fieldError = homeViewModel.validateLoginFields(homeViewModel.username, homeViewModel.password)
                    if (fieldError != null) {
                        homeViewModel.clearError()
                        homeViewModel.clearError()
                        homeViewModel.updateUsername(homeViewModel.username) // force state
                        homeViewModel.updatePassword(homeViewModel.password)
                        // establecer mensaje de error en ViewModel
                        // como no hay setter público para errorMessage, usamos clearError + local handling
                        vibrateStrong()
                        shakeTrigger++
                        return@Button
                    }

                    // activar efecto que ejecuta el suspend login
                    loginTrigger++
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = mintGreen,
                    contentColor = Color.Black
                ),
                enabled = !isSubmitting
            ) {
                Text("Iniciar Sesión")
            }

            // Efecto que escucha cambios en loginTrigger y ejecuta la lambda onLoginAttempt
            LaunchedEffect(loginTrigger) {
                if (loginTrigger <= 0) return@LaunchedEffect
                isSubmitting = true
                try {
                    val user = onLoginAttempt(homeViewModel.username, homeViewModel.password)
                    if (user == null) {
                        vibrateStrong()
                        shakeTrigger++
                    }
                } finally {
                    isSubmitting = false
                }
            }

            // Mostrar mensaje de error del ViewModel
            if (homeViewModel.errorMessage.isNotEmpty()) {
                Text(
                    text = homeViewModel.errorMessage,
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
    // Llamar sin pasar ViewModel - preview usará viewModel() internamente
    HomeScreen()
}
