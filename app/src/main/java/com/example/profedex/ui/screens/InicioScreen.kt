package com.example.profedex.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.profedex.R
import com.example.profedex.data.model.ProfesorFB
import com.example.profedex.viewmodel.InicioViewModel

@Composable
fun InicioScreen(
    onProfesorClick: (ProfesorFB) -> Unit = {},
    onSearchClick: () -> Unit = {},
    viewModel: InicioViewModel = viewModel()
) {
    val profesoresRecomendados by viewModel.profesoresRecomendados.collectAsStateWithLifecycle()
    val profesoresPesados by viewModel.profesoresPesados.collectAsStateWithLifecycle()
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // ── HEADER (Rojo con letras Blancas) ────────────────────────
        Surface(
            color = colorScheme.error, // Fondo Rojo
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
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "PROFEDEX",
                        color = colorScheme.onError, // Letras Blancas
                        style = typography.titleLarge.copy(fontSize = 22.sp, letterSpacing = 1.sp)
                    )
                    Text(
                        text = "Facultad de Ingeniería UNAM",
                        color = colorScheme.onError.copy(alpha = 0.8f),
                        style = typography.bodyLarge.copy(fontSize = 11.sp)
                    )
                }
            }
        }

        // ── IMAGEN BANNER ────────────────────────────────────────
        Image(
            painter = painterResource(id = R.drawable.banner_fi),
            contentDescription = "Banner FI",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        )

        // ── BIENVENIDA ───────────────────────────────────────────
        Surface(
            color = colorScheme.secondaryContainer,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "¡BIENVENIDO A PROFEDEX!",
                color = colorScheme.onSecondaryContainer,
                style = typography.titleLarge.copy(fontSize = 16.sp),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 12.dp)
            )
        }

        // ── BARRA DE BÚSQUEDA ────────────────────────────────────
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { onSearchClick() },
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = colorScheme.surface)
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.busqueda),
                    contentDescription = "Buscar",
                    tint = colorScheme.error,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Buscar profesor o materia...",
                    color = colorScheme.onSurfaceVariant,
                    style = typography.bodyLarge.copy(fontSize = 14.sp),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // ── SECCIONES ──────────────────────
        SectionHeader("PROFES MÁS RECOMENDADOS", colorScheme.tertiary)
        profesoresRecomendados.forEach { profesor ->
            ProfesorCardInicio(
                profesor = profesor,
                colorEtiqueta = colorScheme.tertiary,
                etiqueta = "Promedio",
                valor = profesor.averageRating,
                onClick = { onProfesorClick(profesor) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        SectionHeader("PROFES PESADOS", colorScheme.error)
        profesoresPesados.forEach { profesor ->
            ProfesorCardInicio(
                profesor = profesor,
                colorEtiqueta = colorScheme.error,
                etiqueta = "Dificultad",
                valor = profesor.difficulty,
                onClick = { onProfesorClick(profesor) }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun SectionHeader(titulo: String, color: Color) {
    Text(
        text = titulo,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        style = MaterialTheme.typography.titleLarge.copy(
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    )
}

@Composable
fun ProfesorCardInicio(
    profesor: ProfesorFB,
    colorEtiqueta: Color,
    etiqueta: String,
    valor: Double,
    onClick: () -> Unit
) {
    val typography = MaterialTheme.typography
    val colorScheme = MaterialTheme.colorScheme

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick,
        colors = CardDefaults.elevatedCardColors(containerColor = colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (profesor.photo.isNotEmpty()) {
                AsyncImage(
                    model = profesor.photo,
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(colorEtiqueta.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = profesor.name.firstOrNull()?.toString() ?: "?",
                        style = typography.titleLarge.copy(color = colorEtiqueta)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = profesor.name,
                    style = typography.titleLarge.copy(fontSize = 15.sp),
                    maxLines = 1
                )
                Text(
                    text = profesor.materia,
                    style = typography.bodyLarge.copy(fontSize = 12.sp, color = Color.Gray),
                    maxLines = 1
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "%.1f".format(valor),
                    style = typography.titleLarge.copy(fontSize = 18.sp, color = colorEtiqueta)
                )
                Text(
                    text = etiqueta,
                    style = typography.bodyLarge.copy(fontSize = 10.sp, color = Color.Gray)
                )
            }
        }
    }
}
