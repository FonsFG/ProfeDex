package com.example.profedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.profedex.data.model.Profesor
import com.example.profedex.data.model.Review
import com.example.profedex.ui.screens.ProfesorProfileScreen
import com.example.profedex.ui.theme.InicioApp
import com.example.profedex.ui.theme.theme.ProfeDexTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
    setContent {
        ProfeDexTheme {
            // 1. Creamos un profesor de prueba (Mecatrónico de corazón)
            val profeEjemplo = Profesor(
                id = "1",
                name = "Ing. Pabel Escutia",
                photo = "https://tu-url-de-imagen.com/foto.jpg", // O usa un placeholder
                department = "Ingeniería Mecatrónica",
                email = "pabel.escutia@ingenieria.unam.edu",
                descripcion = "Experto en control y automatización. Sus clases son leyenda en la Facultad de Ingeniería.",
                averageRating = 9.5,
                difficulty = 8.0,
                tags = listOf("Mecatrónica", "Leyenda", "Control", "UNAM")
            )

            // 2. Creamos unas reseñas de prueba
            val reseñasEjemplo = listOf(
                Review("Fonsi", "Es el mejor profe de control, se aprende muchísimo.", 1, "uwu"),
                Review("Papu", "Sus exámenes están rudos pero es justo.", 5, "owo")
            )

            // 3. Llamamos a la pantalla y le pasamos los datos
            ProfesorProfileScreen(
                professor = profeEjemplo,
                reviews = reseñasEjemplo,
                onBackClick = {
                    // Aquí podrías cerrar la app o navegar atrás
                    finish()
                }
            )
        }
    }
}
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
        style = MaterialTheme.typography.titleLarge
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProfeDexTheme {
        Greeting("Android")
    }
}