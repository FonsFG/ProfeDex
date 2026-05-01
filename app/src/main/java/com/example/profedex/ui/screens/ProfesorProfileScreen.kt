package com.example.profedex.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.profedex.data.model.Review
import com.example.profedex.ui.components.ReviewCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfesorProfileScreen(
    professor: Profesor, 
    reviews: List<Review>,
    onBackClick: () -> Unit,
    onEvaluarClick: () -> Unit
) {
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
                        Column(
                            horizontalAlignment = Alignment.Start
                        ) {
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

                    Text(
                        text = "MATERIA: ${professor.materia}",
                        style = typography.titleLarge.copy(fontSize = 14.sp),
                        color = colorScheme.primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        professor.tags.forEach { tag ->
                            SuggestionChip(
                                onClick = { },
                                label = { Text(tag, style = typography.bodyLarge.copy(fontSize = 10.sp)) },
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = colorScheme.surfaceVariant.copy(alpha = 0.3f)),
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

            items(reviews) { review ->
                ReviewCard(review)
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
        value >= 8.5 -> Color(0xFF4CAF50)
        value >= 7.0 -> Color(0xFFFFC107)
        else -> Color(0xFFF44336)
    }
}

fun getDifficultyColor(value: Double): Color {
    return when {
        value >= 8.0 -> Color(0xFFF44336)
        value >= 5.0 -> Color(0xFFFFC107)
        else -> Color(0xFF4CAF50)
    }
}
