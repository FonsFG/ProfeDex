package com.example.profedex.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import coil.compose.AsyncImage
import com.example.profedex.R
import com.example.profedex.data.model.Profesor
import com.example.profedex.data.model.Review
import com.example.profedex.ui.components.ReviewCard

@Composable
fun ProfesorProfileScreen(professor: Profesor, reviews: List<Review>,onBackClick: () -> Unit) {
    // 1. El Chasis
    Scaffold(
        topBar = {
            // 2. LA BARRA SUPERIOR FIJA
            Surface(
                color = Color.Red, // Tu color rojo
                modifier = Modifier.fillMaxWidth().height(60.dp),
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_profedex),
                        contentDescription = "Logo",
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text("ProfeDex", color = Color.White, fontWeight = FontWeight.Bold)
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
            // Sección de la imagen del profesor
            item {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = professor.photo,
                        contentDescription = null,
                        placeholder = painterResource(id = R.drawable.profe_placeholder), // Mientras carga
                        error = painterResource(id = R.drawable.profe_placeholder),       // Si falla el link
                        fallback = painterResource(id = R.drawable.profe_placeholder),     // Si la URL es nula
                        modifier = Modifier.size(150.dp).clip(CircleShape)
                    )
                    Text(text = professor.name, style = MaterialTheme.typography.headlineMedium)
                    Text(text = professor.department)
                    Text(text = professor.descripcion, modifier = Modifier.padding(16.dp))
                }
            }

            // Sección para CALIFICAR (Un espacio antes de las reseñas)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("¿Qué tal enseña este Papu-profe?", fontWeight = FontWeight.Bold)
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