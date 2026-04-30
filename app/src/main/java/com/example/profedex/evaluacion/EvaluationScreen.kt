package com.example.profedex.evaluacion

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvaluationScreen() {
    // ESTADO: Recordamos las calificaciones. Inician en 0 estrellas.
    var dominioDelTemaRating by remember { mutableStateOf(0) }
    var claridadRating by remember { mutableStateOf(0) }

    // Scaffold es el componente principal de Material Design para estructurar la pantalla
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Evaluar Profesor") },
                // Colores extraídos directamente de tu tema base (Theme.kt)
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        // Column organiza los elementos verticalmente uno debajo del otro
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // 1. Cabecera del Profesor
            Text(text = "Profesor: Fulanito", style = MaterialTheme.typography.titleLarge)
            Text(text = "Materia: Tal", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(24.dp))

            // 2. Sistema de Calificación con Estrellas
            Text(text = "Dominio del Tema:")
            RatingBar(
                currentRating = dominioDelTemaRating,
                onRatingChanged = { nuevaCalificacion ->
                    dominioDelTemaRating = nuevaCalificacion
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Claridad:")
            RatingBar(
                currentRating = claridadRating,
                onRatingChanged = { nuevaCalificacion ->
                    claridadRating = nuevaCalificacion
                }
            )

            // Espacio dinámico que empuja el botón de guardar hacia el fondo de la pantalla
            Spacer(modifier = Modifier.weight(1f))

            // 3. Botón de Acción (CTA)
            Button(
                onClick = { /* TODO: Lógica de Corrutinas para guardar en base de datos */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("SUBIR CALIFICACIÓN")
            }
        }
    }
}

// COMPONENTE PERSONALIZADO: Barra de 5 Estrellas
@Composable
fun RatingBar(
    currentRating: Int, // Recibe la calificación actual
    onRatingChanged: (Int) -> Unit // Avisa cuando se hace clic en una estrella
) {
    // Row alinea las estrellas de forma horizontal
    Row {
        for (i in 1..5) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Estrella $i",
                // Si la estrella es menor o igual a la calificación actual, se pinta del color principal
                tint = if (i <= currentRating) MaterialTheme.colorScheme.primary else Color.LightGray,
                modifier = Modifier
                    .clickable { onRatingChanged(i) } // Detecta el toque
                    .padding(4.dp)
            )
        }
    }
}