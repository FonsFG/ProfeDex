package com.example.profedex.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvaluationScreen(onBackClick: () -> Unit = {}) {
    var dominioDelTemaRating by remember { mutableStateOf(0) }
    var claridadRating by remember { mutableStateOf(0) }
    var comentario by remember { mutableStateOf("") }
    
    val typography = MaterialTheme.typography
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Evaluar Profesor", 
                        style = typography.titleLarge.copy(fontSize = 18.sp, color = colorScheme.onPrimary)
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Profesor: Ing. Pepe Policia", 
                style = typography.titleLarge.copy(fontSize = 20.sp)
            )
            Text(
                text = "Materia: Control II", 
                style = typography.bodyLarge.copy(fontSize = 16.sp, color = Color.Gray)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(text = "Dominio del Tema:", style = typography.bodyLarge)
            RatingBar(
                currentRating = dominioDelTemaRating,
                onRatingChanged = { dominioDelTemaRating = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Claridad:", style = typography.bodyLarge)
            RatingBar(
                currentRating = claridadRating,
                onRatingChanged = { claridadRating = it }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- SECCIÓN DE COMENTARIOS RECUPERADA ---
            Text(text = "Tu opinión personal:", style = typography.bodyLarge)
            OutlinedTextField(
                value = comentario,
                onValueChange = { comentario = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(top = 8.dp),
                placeholder = { Text("Escribe aquí tu experiencia con el profesor...", style = typography.bodyLarge.copy(color = Color.Gray)) },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorScheme.primary,
                    unfocusedBorderColor = colorScheme.outline.copy(alpha = 0.5f)
                ),
                textStyle = typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { onBackClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("SUBIR CALIFICACIÓN", style = typography.titleLarge.copy(fontSize = 16.sp))
            }
        }
    }
}

@Composable
fun RatingBar(
    currentRating: Int,
    onRatingChanged: (Int) -> Unit
) {
    Row {
        for (i in 1..5) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Estrella $i",
                tint = if (i <= currentRating) MaterialTheme.colorScheme.tertiary else Color.LightGray,
                modifier = Modifier
                    .clickable { onRatingChanged(i) }
                    .padding(4.dp)
                    .size(36.dp)
            )
        }
    }
}
