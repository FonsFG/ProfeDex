package com.example.profedex.ui.screens

import android.widget.ImageButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.profedex.R
import com.example.profedex.data.model.Profesor
import com.example.profedex.data.model.Review
import com.example.profedex.ui.components.ReviewCard
import androidx.compose.ui.text.style.TextAlign

@Composable
fun ProfesorProfileScreen(
    professor: Profesor,
    reviews: List<Review>,
    onBackClick: () -> Unit
) {

    // 1. Scaffold
    Scaffold(
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding(),
                shadowElevation = 4.dp
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_profedex),
                        contentDescription = "Logo ProfeDex",
                        modifier = Modifier.size(48.dp).clickable(onClick = onBackClick),
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "PROFEDEX",
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 22.sp,
                                letterSpacing = 1.sp
                            )
                        )
                        Text(
                            text = "Facultad de Ingeniería UNAM",
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 11.sp)
                        )
                    }
                }
            }
        }
    ) { paddingValues -> // Este padding evita que el contenido se meta debajo de la barra roja

        // 3. EL CUERPO DESLIZABLE (Todo aquí adentro se mueve)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Sección de la imagen del profesor y calificaciones
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp), // Un poco más de aire
                    horizontalAlignment = Alignment.CenterHorizontally // Centra todo lo que esté suelto
                ) {
                    // --- FILA SUPERIOR: IMAGEN + RATINGS ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center // Centra la pareja Imagen-Textos
                    ) {
                        // Imagen con borde dinámico (opcional: el borde también puede cambiar de color)
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(150.dp))
                        {
                            AsyncImage(
                                model = professor.photo,
                                contentDescription = null,
                                placeholder = painterResource(R.drawable.profe_placeholder),
                                error = painterResource(R.drawable.profe_placeholder),
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                                    .border(3.dp, Color.LightGray, CircleShape),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            // Nombre del Profesor
                            Text(
                                text = professor.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                lineHeight = 18.sp,
                                modifier = Modifier.fillMaxWidth()
                            )

                            // Opcional: Departamento en pequeño
                            Text(
                                text = professor.department,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                        Spacer(modifier = Modifier.width(24.dp)) // Espacio entre foto y datos
                        // Ratings apilados a la derecha
                        Column(
                            horizontalAlignment = Alignment.Start
                        ) {
                            RatingDisplay(
                                label = "PUNTUACIÓN",
                                value = professor.averageRating.toString(),
                                color = getRatingColor(professor.averageRating)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            RatingDisplay(
                                label = "DIFICULTAD",
                                value = professor.difficulty.toString(),
                                color = getDifficultyColor(professor.difficulty)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // --- SECCIÓN DE TAGS (Centrada) ---
                    Text(
                        text = "TIPOS",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    // Usamos una Row con Flow o simplemente centrada
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        professor.tags.take(2).forEach { tag -> // Mostramos los primeros 3 para que no se amontone
                            SuggestionChip(
                                onClick = { },
                                label = { Text(tag) },
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // --- DESCRIPCIÓN ---
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.1f)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(
                                text = "ACERCA DEL PROFESOR",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Black,
                                color = Color.Gray
                            )
                            Text(
                                text = professor.descripcion,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(top = 8.dp),
                                textAlign = TextAlign.Center // Texto de descripción centrado
                            )
                        }
                    }

                    Divider(Modifier.padding(vertical = 12.dp), thickness = 0.5.dp)
                }
            }

            // Sección para CALIFICAR (Un espacio antes de las reseñas)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("¿Qué tal enseña este ProfeMon?", fontWeight = FontWeight.Bold)
                        Button(onClick = { /* Abrir formulario */ }) {
                            Text("Escribir Reseña")
                        }
                    }
                }
            }

            // Sección de RESEÑAS (Muchos elementos)
            items(reviews) { review ->
                ReviewCard(review) // Tu diseño de cada comentario
            }
        }
    }
}
//Para ver la calificación
@Composable
fun RatingDisplay(label: String, value: String, color: Color) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray
        )
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Black,
            color = color
        )
    }
}

// Rating: Alto = Verde (Bueno), Bajo = Rojo (Malo)
fun getRatingColor(value: Double): Color {
    return when {
        value >= 8.5 -> Color(0xFF4CAF50) // Verde
        value >= 7.0 -> Color(0xFFFFC107) // Amarillo/Naranja
        else -> Color(0xFFF44336)         // Rojo
    }
}

// Dificultad: Alta = Rojo (Peligro), Baja = Verde (Barco)
fun getDifficultyColor(value: Double): Color {
    return when {
        value >= 8.0 -> Color(0xFFF44336) // Rojo
        value >= 5.0 -> Color(0xFFFFC107) // Amarillo
        else -> Color(0xFF4CAF50)         // Verde
    }
}