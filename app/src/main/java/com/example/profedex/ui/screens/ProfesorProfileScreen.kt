package com.example.profedex.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.profedex.R
import com.example.profedex.data.model.ProfesorFB
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.profedex.ui.components.ReviewCard
import com.example.profedex.viewmodel.IAViewModel
import com.example.profedex.viewmodel.ProfesorViewModelFB

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfesorProfileScreen(
    professor: ProfesorFB,
    viewModel: ProfesorViewModelFB,
    onBackClick: () -> Unit,
    onEvaluarClick: () -> Unit,
    iaViewModel: IAViewModel = viewModel()
) {
    val comentarios = professor.listaComment
    val ratings = professor.listaRating

    val resumen by iaViewModel.resumen.collectAsState()
    val resumenCargando by iaViewModel.resumenCargando.collectAsState()

    // Limpia el resumen al salir/cambiar de profesor
    DisposableEffect(professor.idDoc) {
        onDispose { iaViewModel.limpiarResumen() }
    }

    val typography = MaterialTheme.typography
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        containerColor = colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Perfil del Profesor",
                        style = typography.titleLarge.copy(fontSize = 18.sp, color = colorScheme.primary)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.onError
                )
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width(150.dp)
                        ) {
                            AsyncImage(
                                model = obtenerPokemonUrl(professor.avatarUrl, professor.idDoc),
                                contentDescription = "Avatar de ${professor.name}",
                                placeholder = painterResource(id = R.drawable.profe_placeholder),
                                error = painterResource(id = R.drawable.profe_placeholder),
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                                    .background(colorScheme.surfaceVariant.copy(alpha = 0.6f))
                                    .border(3.dp, colorScheme.outline.copy(alpha = 0.2f), CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = professor.name,
                                style = typography.titleLarge.copy(fontSize = 18.sp),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = colorScheme.onSurface,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                text = professor.department,
                                style = typography.bodyLarge.copy(fontSize = 12.sp),
                                color = colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }

                        Spacer(modifier = Modifier.width(24.dp))

                        Column(horizontalAlignment = Alignment.Start) {
                            RatingDisplay(
                                label = "PUNTUACIÓN",
                                value = "%.1f".format(professor.averageRating),
                                color = getRatingColor(professor.averageRating)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            RatingDisplay(
                                label = "DIFICULTAD",
                                value = "%.1f".format(professor.difficulty),
                                color = getDifficultyColor(professor.difficulty)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        professor.tags.forEach { tag ->
                            SuggestionChip(
                                onClick = {},
                                label = { Text(tag, style = typography.bodyLarge.copy(fontSize = 10.sp)) },
                                modifier = Modifier.padding(horizontal = 4.dp),
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    labelColor = colorScheme.onSurfaceVariant,
                                    containerColor = colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "MATERIAS QUE IMPARTE:",
                        style = typography.titleLarge.copy(fontSize = 12.sp),
                        color = colorScheme.outline,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    val scrollStateMaterias = rememberScrollState()

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(scrollStateMaterias)
                            .padding(vertical = 4.dp, horizontal = 4.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        professor.materia.forEach { clase ->
                            AssistChip(
                                onClick = {},
                                label = { Text(clase, style = typography.bodyLarge.copy(fontSize = 11.sp)) },
                                modifier = Modifier.padding(horizontal = 4.dp),
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = colorScheme.primary.copy(alpha = 0.1f),
                                    labelColor = colorScheme.outline
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(
                                text = "ACERCA DEL PROFESOR",
                                style = typography.titleLarge.copy(fontSize = 12.sp),
                                color = colorScheme.outline
                            )
                            Text(
                                text = professor.descripcion,
                                style = typography.bodyLarge.copy(fontSize = 14.sp),
                                modifier = Modifier.padding(top = 8.dp),
                                textAlign = TextAlign.Justify,
                                color = colorScheme.onSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = onEvaluarClick,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorScheme.onError,
                            contentColor = colorScheme.primary
                        )
                    ) {
                        Text("EVALUAR PROFESOR", style = typography.titleLarge.copy(fontSize = 14.sp))
                    }

                    HorizontalDivider(Modifier.padding(vertical = 24.dp), thickness = 0.5.dp, color = colorScheme.outline.copy(alpha = 0.2f))

                    Text(
                        text = "RESEÑAS DE ALUMNOS",
                        style = typography.titleLarge.copy(fontSize = 16.sp),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        color = colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // ── BOTÓN RESUMEN CON IA ─────────────────────────
                    if (comentarios.isNotEmpty()) {
                        OutlinedButton(
                            onClick = { iaViewModel.resumirReseñas(comentarios) },
                            enabled = !resumenCargando,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = colorScheme.onError
                            )
                        ) {
                            if (resumenCargando) {
                                CircularProgressIndicator(
                                    color = colorScheme.onError,
                                    strokeWidth = 2.dp,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Generando resumen…", style = typography.bodyLarge.copy(fontSize = 13.sp))
                            } else {
                                Icon(
                                    imageVector = Icons.Filled.AutoAwesome,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (resumen == null) "RESUMIR RESEÑAS CON IA" else "REGENERAR RESUMEN",
                                    style = typography.titleLarge.copy(fontSize = 13.sp)
                                )
                            }
                        }
                    }

                    resumen?.let { textoResumen ->
                        Spacer(modifier = Modifier.height(12.dp))
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = colorScheme.secondaryContainer.copy(alpha = 0.5f)
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Filled.AutoAwesome,
                                        contentDescription = null,
                                        tint = colorScheme.onSecondaryContainer,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "RESUMEN GENERADO POR IA",
                                        style = typography.titleLarge.copy(fontSize = 11.sp, fontWeight = FontWeight.Bold),
                                        color = colorScheme.onSecondaryContainer
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = textoResumen,
                                    style = typography.bodyLarge.copy(fontSize = 13.sp),
                                    color = colorScheme.onSecondaryContainer
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            if (comentarios.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Aún no hay reseñas. ¡Sé el primero!",
                            style = typography.bodyLarge.copy(fontSize = 13.sp),
                            color = colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                itemsIndexed(comentarios) { index, comentario ->
                    ReviewCard(
                        estrellas = ratings.getOrNull(index) ?: 0,
                        comentario = comentario
                    )
                }
            }
        }
    }
}

@Composable
fun RatingDisplay(label: String, value: String, color: Color) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 10.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
            fontWeight = FontWeight.Black,
            color = color
        )
    }
}

fun getRatingColor(value: Double): Color {
    return when {
        value <= 2 -> Color(0xFFF44336)
        value <= 3.5 -> Color(0xFFFFC107)
        else -> Color(0xFF4CAF50)
    }
}

fun getDifficultyColor(value: Double): Color {
    return when {
        value <= 2 -> Color(0xFF4CAF50)
        value <= 3.5 -> Color(0xFFFFC107)
        else -> Color(0xFFF44336)
    }
}

fun obtenerPokemonUrl(avatarUrl: String, profesorId: String): String {
    val pokemonId = if (avatarUrl.isNotEmpty()) {
        avatarUrl
    } else {
        (kotlin.math.abs(profesorId.hashCode()) % 151 + 1).toString()
    }

    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$pokemonId.png"
}
