package com.example.profedex.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.profedex.R
import com.example.profedex.data.model.ProfesorFB
import com.example.profedex.viewmodel.IAViewModel
import com.example.profedex.viewmodel.ProfesorViewModelFB
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvaluarProfesorScreen(
    professor: ProfesorFB,
    onBackClick: () -> Unit = {},
    viewModel: ProfesorViewModelFB = viewModel(),
    iaViewModel: IAViewModel = viewModel()
) {
    var puntuacionRating by remember { mutableIntStateOf(5) }
    var dificultadRating by remember { mutableIntStateOf(3) }
    var nuevoComentario by remember { mutableStateOf("") }
    var moderando by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val typography = MaterialTheme.typography
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        containerColor = colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text("Evaluar Profesor", style = typography.titleLarge.copy(fontSize = 18.sp, color = colorScheme.primary))
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
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ── INFO DEL PROFESOR ─────────────────────────────────
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = colorScheme.surfaceVariant.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = obtenerPokemonUrl(professor.avatarUrl, professor.idDoc),
                        contentDescription = "Avatar de ${professor.name}",
                        placeholder = painterResource(id = R.drawable.profe_placeholder),
                        error = painterResource(id = R.drawable.profe_placeholder),
                        modifier = Modifier
                            .size(74.dp)
                            .clip(CircleShape)
                            .background(colorScheme.surfaceVariant.copy(alpha = 0.6f))
                            .border(2.dp, colorScheme.outline.copy(alpha = 0.2f), CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = professor.name,
                            style = typography.titleLarge.copy(fontSize = 16.sp),
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.onSurface
                        )
                        Text(
                            text = professor.department,
                            style = typography.bodyLarge.copy(fontSize = 12.sp, color = colorScheme.onSurfaceVariant)
                        )
                    }
                }
            }

            HorizontalDivider(thickness = 0.5.dp, color = colorScheme.outline.copy(alpha = 0.2f))

            // ── PUNTUACIÓN ──────────────────────────
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "PUNTUACIÓN GENERAL",
                    style = typography.titleSmall.copy(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                    color = colorScheme.error
                )
                Text(
                    text = "¿Qué tan buen profesor es?",
                    style = typography.bodyMedium.copy(fontSize = 11.sp, color = colorScheme.onSurfaceVariant),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                RatingBarProfesor(currentRating = puntuacionRating, onRatingChanged = { puntuacionRating = it })
            }

            // ── DIFICULTAD ──────────────────────
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "NIVEL DE DIFICULTAD",
                    style = typography.titleSmall.copy(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                    color = colorScheme.error
                )
                Text(
                    text = "¿Qué tan pesado es pasar o aprender con él?",
                    style = typography.bodyMedium.copy(fontSize = 11.sp, color = colorScheme.onSurfaceVariant),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                RatingBarProfesor(currentRating = dificultadRating, onRatingChanged = { dificultadRating = it })
            }

            // ── COMENTARIO ─────────────────────────
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "TU COMENTARIO",
                    style = typography.titleSmall.copy(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                    color = colorScheme.error,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = nuevoComentario,
                    onValueChange = { nuevoComentario = it },
                    label = { Text("Cuéntale a la FI tu experiencia...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorScheme.error,
                        unfocusedBorderColor = colorScheme.outline.copy(alpha = 0.5f),
                        focusedLabelColor = colorScheme.error,
                        cursorColor = colorScheme.error
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── BOTÓN PUBLICAR EVALUACIÓN ─────────────────────────
            Button(
                onClick = {
                    if (nuevoComentario.isBlank()) {
                        Toast.makeText(context, "Por favor deja un comentario", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    // Moderación con IA antes de publicar
                    scope.launch {
                        moderando = true
                        val resultado = iaViewModel.moderarComentario(nuevoComentario, professor.name)
                        moderando = false

                        if (!resultado.aprobado) {
                            Toast.makeText(
                                context,
                                "Comentario bloqueado: ${resultado.razon.ifBlank { "lenguaje no permitido" }}",
                                Toast.LENGTH_LONG
                            ).show()
                            return@launch
                        }

                        val nuevaListaRating = professor.listaRating.toMutableList().apply { add(puntuacionRating) }
                        val nuevaListaDifficulty = professor.listaDifficulty.toMutableList().apply { add(dificultadRating) }
                        val nuevaListaComment = professor.listaComment.toMutableList().apply { add(nuevoComentario) }

                        val profesorActualizado = professor.copy(
                            averageRating = nuevaListaRating.average(),
                            difficulty = nuevaListaDifficulty.average(),
                            listaRating = nuevaListaRating,
                            listaDifficulty = nuevaListaDifficulty,
                            listaComment = nuevaListaComment
                        )

                        viewModel.actualizarProfesor(
                            profesorId = professor.idDoc,
                            profesorActualizado = profesorActualizado,
                            onSuccess = {
                                Toast.makeText(context, "¡Evaluación publicada! 🚀", Toast.LENGTH_LONG).show()
                                onBackClick()
                            },
                            onFailure = { e ->
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        )
                    }
                },
                enabled = !moderando,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.onError,
                    contentColor = colorScheme.primary
                )
            ) {
                if (moderando) {
                    CircularProgressIndicator(
                        color = colorScheme.primary,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(22.dp)
                    )
                } else {
                    Text(
                        text = "PUBLICAR EVALUACIÓN",
                        style = typography.titleLarge.copy(fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Composable
fun RatingBarProfesor(currentRating: Int, onRatingChanged: (Int) -> Unit) {
    val colorScheme = MaterialTheme.colorScheme
    val estrellaDorada = Color(0xFFFFB800)
    Row {
        for (i in 1..5) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = if (i <= currentRating) estrellaDorada else colorScheme.outline.copy(alpha = 0.3f),
                modifier = Modifier
                    .clickable { onRatingChanged(i) }
                    .padding(4.dp)
                    .size(32.dp)
            )
        }
    }
}
