package com.example.appbibliofilia.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainScreen() {
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
            HeaderSection()
            HeroSection()
            HowItWorksSection()
            SignupSection()
            FAQSection()
            FooterSection()
        }
    }
}

@Composable
fun HeaderSection() {
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
            Text(
                text = "Bibliofilia",
                style = TextStyle(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2B2B2B)
                )
            )
            Button(
                onClick = { /* TODO: Navegar a registro o login */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFBFE3D0),
                    contentColor = Color(0xFF2B2B2B)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Comenzar")
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
            text = "Tu mundo de libros en un solo lugar 📚",
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
            text = "Explora, guarda y comparte tus lecturas favoritas con la comunidad de lectores.",
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
            Text("Descubrir Libros")
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
        repeat(3) { index ->
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
                            .background(Color(0xFFBFE3D0), RoundedCornerShape(50))
                    ) {
                        // TODO: Ícono o imagen
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Paso ${index + 1}",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF2B2B2B)
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "Descripción del paso ${index + 1} con detalles breves sobre el proceso.",
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
fun SignupSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF9F9FF))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Únete a Bibliofilia",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            color = Color(0xFF2B2B2B)
        )
        Spacer(Modifier.height(16.dp))

        var name by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre completo") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFBFE3D0),
                unfocusedBorderColor = Color(0xFFBFE3D0)
            )
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFBFE3D0),
                unfocusedBorderColor = Color(0xFFBFE3D0)
            )
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { /* TODO: acción de envío */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBFE3D0)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Registrarme", color = Color(0xFF2B2B2B), fontWeight = FontWeight.Bold)
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
            fontFamily = FontFamily.Serif,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2B2B2B)
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

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Vista previa - Pantalla Principal"
)
@Composable
fun MainScreenPreview() {
    MainScreen()
}

