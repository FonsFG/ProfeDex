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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.profedex.data.model.Profesor
import com.example.profedex.data.model.Review
import com.example.profedex.ui.screens.ProfesorProfileScreen
import com.example.profedex.ui.theme.InicioApp
import com.example.profedex.ui.theme.theme.ProfeDexTheme
import com.example.profedex.viewmodel.ProfesorViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Obtenemos el ViewModel (necesitas importar androidx.lifecycle.viewmodel.compose.viewModel)
        setContent {
            ProfeDexTheme {
                val viewModel: ProfesorViewModel = viewModel()
                // Observamos el estado (si el profesor cambia, la pantalla se refresca sola)
                val profesor by viewModel.profesorState.collectAsState()

                // Si ya cargó el profesor, mostramos la pantalla
                profesor?.let { profe ->
                    ProfesorProfileScreen(
                        professor = profe,
                        reviews = viewModel.getReviewsDePrueba(),
                        onBackClick = { finish() }
                    )
                }
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