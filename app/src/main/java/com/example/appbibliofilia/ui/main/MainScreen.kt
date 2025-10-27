package com.example.appbibliofilia.ui.main

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun MainScreen(isLoggedIn: Boolean = false, userName: String? = null, onLogout: (() -> Unit)? = null) {
    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF7F0))
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxSize()
        ) {
            HeaderSection(isLoggedIn = isLoggedIn, userName = userName, onLogout = onLogout)
            HeroSection()
            HowItWorksSection()
            FAQSection()
            FooterSection()
        }
    }
}

@Composable
fun HeaderSection(isLoggedIn: Boolean = false, userName: String? = null, onLogout: (() -> Unit)? = null) {
    val shouldShowGreeting = isLoggedIn && !userName.isNullOrBlank()

    // bandera local para controlar la visibilidad y forzar la animación al montar
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(shouldShowGreeting) {
        Log.d("MainScreen", "LaunchedEffect shouldShowGreeting=$shouldShowGreeting")
        if (shouldShowGreeting) {
            visible = false
            Log.d("MainScreen", "set visible=false")
            delay(500) // retraso corto para forzar el toggle
            visible = true
            Log.d("MainScreen", "set visible=true")
        } else {
            visible = false
            Log.d("MainScreen", "set visible=false (shouldShowGreeting=false)")
        }
    }

    // animaciones explícitas
    val greetingAlpha by animateFloatAsState(targetValue = if (visible) 1f else 0f, animationSpec = tween(durationMillis = 1000))
    val greetingOffsetY by animateDpAsState(targetValue = if (visible) 0.dp else 12.dp, animationSpec = tween(durationMillis = 1000))
    val greetingScale by animateFloatAsState(targetValue = if (visible) 1f else 0.98f, animationSpec = tween(durationMillis = 1000))

    // Loguear alpha para ver la transición
    LaunchedEffect(greetingAlpha) {
        Log.d("MainScreen", "greetingAlpha=$greetingAlpha visible=$visible shouldShowGreeting=$shouldShowGreeting")
    }

    Surface(
        color = Color(0xFFFFF7F0),
        tonalElevation = 4.dp,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Greeting block (animado)
            val showGreetingBlock = visible || greetingAlpha > 0.01f
            if (showGreetingBlock && !userName.isNullOrBlank()) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .graphicsLayer { alpha = greetingAlpha; scaleX = greetingScale; scaleY = greetingScale }
                        .offset(y = greetingOffsetY)
                ) {
                    Text(
                        text = "Hola, ${userName}",
                        style = TextStyle(
                            fontFamily = FontFamily.Serif,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2B2B2B)
                        )
                    )
                    Text(
                        text = "Bienvenido a tu Biblihogar",
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 14.sp,
                            color = Color(0xFF5A5A5A)
                        )
                    )
                }
            } else {
                // título por defecto
                Text(
                    text = "Bibliofilia",
                    style = TextStyle(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2B2B2B)
                    )
                )
            }

            if (isLoggedIn) {
                Button(
                    onClick = { onLogout?.invoke() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFD6D6),
                        contentColor = Color(0xFF2B2B2B)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Cerrar sesión")
                }
            }
        }
    }
}

@Composable
fun HeroSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFF7F0))
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Tus colecciones y progresos de lectura en un solo lugar 📚",
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = Color(0xFF2B2B2B),
                textAlign = TextAlign.Center
            ),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = "Registra lo que lees, prioriza lo que viene y cierra más libros cada mes.",
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontSize = 16.sp,
                color = Color(0xFF2B2B2B),
                textAlign = TextAlign.Center
            ),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = { /* TODO */ },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFBFE3D0),
                contentColor = Color(0xFF2B2B2B)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Escríbenos")
        }
    }
}

@Composable
fun HowItWorksSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Cómo funciona",
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2B2B2B)
            )
        )
        Spacer(Modifier.height(24.dp))
        // Cuatro tarjetas distintas con número dentro de círculo mint
        val cards = listOf(
            Pair("Información del libro actual", "Libro físico o digital, páginas de progreso, colección a la que pertenece"),
            Pair("Timer de lectura", "Sesiones con temporizador que grabarán tus progresos."),
            Pair("Registro de libros leídos", "Historial, reportes, tus opiniones, notas y objetivos de lectura."),
            Pair("Colecciones", "Sagas, géneros, autores, categoriza por libros físicos o digitales.")
        )

        cards.forEachIndexed { index, item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F6F8))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(Color(0xFFBFE3D0), RoundedCornerShape(50)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${index + 1}",
                            style = TextStyle(
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = Color(0xFF2B2B2B),
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = item.first,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF2B2B2B)
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = item.second,
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 14.sp,
                        color = Color(0xFF5A5A5A),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

    }
}

@Composable
fun FAQSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Preguntas Frecuentes",
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2B2B2B)
            )
        )
        Spacer(Modifier.height(16.dp))
        val faqs = listOf(
            "¿Qué es Bibliofilia?" to "Una plataforma para amantes de los libros.",
            "¿Puedo compartir mis lecturas?" to "Sí, puedes compartir tus libros favoritos.",
            "¿Tiene costo?" to "No, es totalmente gratuita."
        )
        faqs.forEach { (q, a) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F6F8)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(q, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(Modifier.height(4.dp))
                    Text(a, fontSize = 14.sp, color = Color(0xFF5A5A5A))
                }
            }
        }
    }
}

@Composable
fun FooterSection() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFF3F6F8)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "© 2025 Bibliofilia. Todos los derechos reservados.",
                fontSize = 14.sp,
                color = Color(0xFF2B2B2B)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(isLoggedIn = true, userName = "Alice")
}
