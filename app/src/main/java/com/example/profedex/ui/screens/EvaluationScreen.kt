package com.example.profedex.ui.screens

import android.widget.Toast
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.profedex.data.model.ProfesorFB
import com.example.profedex.viewmodel.ProfesorViewModelFB

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvaluationScreen(
    onBackClick: () -> Unit = {},
    viewModel: ProfesorViewModelFB = viewModel()
) {
    // ESTADOS DEL FORMULARIO
    var nombre by remember { mutableStateOf("") }
    var departamento by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var materiasText by remember { mutableStateOf("") } // Ingresadas por coma
    var descripcion by remember { mutableStateOf("") }
    var tagsText by remember { mutableStateOf("") } // Ingresadas por coma
    var rating by remember { mutableStateOf(0) }
    var dificultad by remember { mutableStateOf(0) }
    var pokemonNum by remember { mutableStateOf("") }

    val context = LocalContext.current
    val typography = MaterialTheme.typography
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text("Registrar ProfeMon", style = typography.titleLarge.copy(fontSize = 18.sp, color = colorScheme.primary)) 
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colorScheme.onError)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 1. NOMBRE
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del Profesor") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            // 2. DEPARTAMENTO Y EMAIL
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = departamento,
                    onValueChange = { departamento = it },
                    label = { Text("Depto (ej. DIMEI)") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // 3. MATERIAS
            OutlinedTextField(
                value = materiasText,
                onValueChange = { materiasText = it },
                label = { Text("Materias (separa con comas)") },
                placeholder = { Text("Cálculo, Álgebra...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            // 4. DESCRIPCIÓN
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción / Reseña") },
                modifier = Modifier.fillMaxWidth().height(100.dp),
                shape = RoundedCornerShape(12.dp)
            )

            // 5. CALIFICACIONES (ESTRELLAS)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Calificación:", style = typography.labelLarge)
                    RatingBar(currentRating = rating, onRatingChanged = { rating = it })
                }
                Column {
                    Text("Dificultad:", style = typography.labelLarge)
                    RatingBar(currentRating = dificultad, onRatingChanged = { dificultad = it })
                }
            }

            // 6. TAGS Y POKEMON
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = tagsText,
                    onValueChange = { tagsText = it },
                    label = { Text("Tags (comas)") },
                    modifier = Modifier.weight(1.5f),
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = pokemonNum,
                    onValueChange = { if(it.length <= 3) pokemonNum = it },
                    label = { Text("N° Pokemon") },
                    placeholder = { Text("1-151") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // BOTÓN DE GUARDAR
            Button(
                onClick = {
                    if (nombre.isBlank() || materiasText.isBlank()) {
                        Toast.makeText(context, "Mínimo pon el nombre y materias", Toast.LENGTH_SHORT).show()
                    } else {
                        val nuevoProfe = ProfesorFB(
                            name = nombre,
                            department = departamento,
                            email = email,
                            descripcion = descripcion,
                            averageRating = rating.toDouble(),
                            difficulty = dificultad.toDouble(),
                            avatarUrl = pokemonNum,
                            materia = materiasText.split(",").map { it.trim() }.filter { it.isNotEmpty() },
                            tags = tagsText.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                        )

                        viewModel.guardarNuevoProfesor(
                            nuevoProfe = nuevoProfe,
                            onSuccess = {
                                Toast.makeText(context, "¡ProfeMon registrado! 🚀", Toast.LENGTH_LONG).show()
                                onBackClick()
                            },
                            onFailure = { e ->
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorScheme.error)
            ) {
                Text("REGISTRAR EN PROFEDEX", style = typography.titleLarge.copy(fontSize = 16.sp, color = Color.White))
            }
        }
    }
}

@Composable
fun RatingBar(currentRating: Int, onRatingChanged: (Int) -> Unit) {
    Row {
        for (i in 1..5) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = if (i <= currentRating) Color(0xFFFFD700) else Color.LightGray,
                modifier = Modifier.clickable { onRatingChanged(i) }.size(28.dp)
            )
        }
    }
}
