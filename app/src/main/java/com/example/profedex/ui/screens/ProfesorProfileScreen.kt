package com.example.profedex.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.profedex.data.model.Profesor
import com.example.profedex.data.model.ProfesorFB
import com.example.profedex.data.model.Review
import com.example.profedex.ui.components.ReviewCard
import com.example.profedex.viewmodel.ProfesorViewModelFB

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfesorProfileScreen(
    professor: ProfesorFB,
    viewModel: ProfesorViewModelFB,          // ← recibe el ViewModel
    onBackClick: () -> Unit,
    onEvaluarClick: () -> Unit
) {
    // collectAsState convierte el Flow (río de datos) en algo que Compose entiende
    val reviews by viewModel.reviews.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // LaunchedEffect (efecto lanzado una sola vez) llama fetchReviews cuando
    // la pantalla aparece por primera vez
    LaunchedEffect(professor.idDoc) {
        viewModel.fetchReviews(professor.idDoc)
    }

    val typography = MaterialTheme.typography
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
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
                                // 1. La fuente de la imagen (URL, URI, recurso local o File)
                                model = obtenerPokemonUrl(professor.avatarUrl, professor.idDoc),

                                // 2. Descripción de accesibilidad (para lectores de pantalla)
                                contentDescription = "Avatar de ${professor.name}",

                                // 3. Imagen temporal mientras se descarga de internet
                                placeholder = painterResource(id = R.drawable.profe_placeholder),

                                // 4. Imagen de respaldo por si no hay internet o la URL no existe
                                error = painterResource(id = R.drawable.profe_placeholder),

                                // 5. Modificadores de diseño (tamaño, recortes, fondos, bordes)
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                                    .border(3.dp, Color.LightGray, CircleShape),

                                // 6. Cómo se adapta la imagen al contenedor (recorte, estirado, etc.)
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = professor.name,
                                style = typography.titleLarge.copy(fontSize = 18.sp),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                text = professor.department,
                                style = typography.bodyLarge.copy(fontSize = 12.sp),
                                color = Color.Gray,
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

                    // ==========================================
                    // 1. SECCIÓN DE TAGS (AHORA ARRIBA)
                    // ==========================================
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
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // ==========================================
                    // 2. SECCIÓN DE MATERIAS (BARRA DESLIZABLE HORIZONTAL)
                    // ==========================================
                    Text(
                        text = "MATERIAS QUE IMPARTE:",
                        style = typography.titleLarge.copy(fontSize = 12.sp),
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    // Estado que recuerda la posición del scroll horizontal
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
                                    labelColor = colorScheme.primary
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
                                textAlign = TextAlign.Justify
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

                    HorizontalDivider(Modifier.padding(vertical = 24.dp), thickness = 0.5.dp)

                    Text(
                        text = "RESEÑAS DE ALUMNOS",
                        style = typography.titleLarge.copy(fontSize = 16.sp),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // ← muestra spinner mientras carga, reseñas cuando ya llegaron
            if (isLoading) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()   // ruedita de carga
                    }
                }
            } else {
                items(reviews) { review ->
                    ReviewCard(review)
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
            color = Color.Gray
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

/**
 * Genera una URL de arte oficial de Pokémon de forma consistente basada en el ID del profesor.
 */
fun obtenerPokemonUrl(avatarUrl: String, profesorId: String): String {
    val pokemonId = if (avatarUrl.isNotEmpty()) {
        avatarUrl
    } else {
        (kotlin.math.abs(profesorId.hashCode()) % 151 + 1).toString()
    }

    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$pokemonId.png"
}