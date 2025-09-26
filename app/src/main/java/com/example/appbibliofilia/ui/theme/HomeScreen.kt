package com.example.appbibliofilia.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.appbibliofilia.R
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.LineHeightStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(){
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("BIBLIOFILIA")},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ))
        }
    ) { Padding ->
        Column(
            modifier = Modifier
                .padding(Padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "¡Donde tus libros encuentran su biblihogar!")
            Button(onClick = { /* accion futura */}) {
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                Text("Explorar Libros")
                style = MaterialTheme.typography.headlineMedium
                color = MaterialTheme.colorScheme.onBackground
            )

         }

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo Bibliofilia",
                modifier = Modifier.size(120.dp)

            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
