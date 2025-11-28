plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.appbibliofilia"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.appbibliofilia"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
// permite escribir codigo mas compacto con kotlin
    implementation(libs.androidx.core.ktx)
    //maneja el ciclo de vida de la Activity y los ViewModel, para que tareas en segundo plano, se controlen automáticamente
    implementation(libs.androidx.lifecycle.runtime.ktx)
    // Hace posible que el Activity muestre la UI hecha con Compose
    implementation(libs.androidx.activity.compose)
    // mantiene las versiones Compose ordenadas y sincronizadas
    implementation(platform(libs.androidx.compose.bom))
    //Nucleo de la UI en compose, contiene muchos componentes y funciones para la interfaz
    implementation(libs.androidx.ui)
    //para construir cosas mas visuales, personalizadas
    implementation(libs.androidx.ui.graphics)
    //Permite ver previews de composables sin ejecutar toda la APP
    implementation(libs.androidx.ui.tooling.preview)
    //Componentes visuales listos para construir interfaces bonitas
    implementation(libs.androidx.material3)
    //para las pruebas unitarias
    testImplementation(libs.junit)
    //para las pruebas instrumentadas de ANDROID que requieran el emulador
    androidTestImplementation(libs.androidx.junit)
    //Pruebas de UI clásicas, que muestre "haz clic en este boton" y cosas asi
    androidTestImplementation(libs.androidx.espresso.core)
    //asegura que las versiones de librerías de prueba de Compose sean compatibles entre sí.
    androidTestImplementation(platform(libs.androidx.compose.bom))
    // Equivalente de Espresso, para interfaces hechas con Compose
    androidTestImplementation(libs.androidx.ui.test.junit4)
    // Permite usar herramientas de debug de la UI
    debugImplementation(libs.androidx.ui.tooling)
    // Ayuda a configurar manifest de prueba para test de UI en modo debug
    debugImplementation(libs.androidx.ui.test.manifest)
    // Sistema de navegación entre pantallas para Compose
    implementation("androidx.navigation:navigation-compose:2.7.7")
    // Para cargar imagenes desde internet o recursos de forma eficiente
    implementation("io.coil-kt:coil-compose:2.6.0")
    // Soporte para imagenes GIF
    implementation("io.coil-kt:coil-gif:2.6.0")
    // Librerias de Retrofit para consumir APIs REST
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    // Convertidor de JSON a objetos Kotlin usando Gson
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    // Librería de red que usa Retrofit por debajo, muestra en el logcat las peticiones y respuestas HTTP
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    // Permiten hacer tareas en segundo plano sin bloquear la UI (llamadas a API, DB, etc)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")


}