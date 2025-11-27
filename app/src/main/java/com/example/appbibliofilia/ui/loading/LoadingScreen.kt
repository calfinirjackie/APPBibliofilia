package com.example.appbibliofilia.ui.loading

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.ImageLoader
import com.example.appbibliofilia.R
import kotlinx.coroutines.delay

@Composable
fun LoadingScreen(onTimeout: () -> Unit) {

    LaunchedEffect(Unit) {
        delay(12000)
        onTimeout()
    }

    val context = LocalContext.current

    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (android.os.Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()


    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp),
        color = Color.Black
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(R.drawable.loading)
                    .crossfade(true)
                    .build(),
                imageLoader = imageLoader,
                contentDescription = "Pantalla de carga",
                modifier = Modifier.fillMaxSize()
            )


            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                TextButton(
                    onClick = { onTimeout() },
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                    Text(text = "Skip")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    LoadingScreen(onTimeout = {})
}
