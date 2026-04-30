package com.example.profedex.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Menu
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
import com.example.profedex.R
import com.example.profedex.viewmodel.InicioViewModel

@Composable
fun InicioScreen(
    onBuscarClick: () -> Unit = {},
    onProfesorClick: (String) -> Unit,
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
        // ── HEADER (Ahora Rojo del tema) ────────────────────────
        Surface(
            color = colorScheme.error,
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
                        color = colorScheme.onPrimary,
                        style = typography.titleLarge.copy(fontSize = 22.sp, letterSpacing = 1.sp)
                    )
                    Text(
                        text = "Facultad de Ingeniería UNAM",
                        color = colorScheme.onPrimary.copy(alpha = 0.8f),
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
                .height(200.dp)
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
                .padding(16.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = colorScheme.surface)
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Buscar profesor o materia...",
                    color = colorScheme.onSurfaceVariant,
                    style = typography.bodyLarge.copy(fontSize = 14.sp),
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Filtros",
                    tint = colorScheme.outline
                )
            }
        }

        // ── TARJETAS PRINCIPALES ─────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TarjetaCategoria(
                modifier = Modifier.weight(1f),
                icono = R.drawable.estrella_recomendado,
                fotoProfesor = R.drawable.profe_recomendado,
                titulo = "PROFES MÁS RECOMENDADOS",
                descripcion = "Los mejores evaluados por la comunidad.",
                valoracion = profesoresRecomendados.firstOrNull()?.averageRating ?: 0.0,
                etiqueta = "Promedio",
                colorEtiqueta = colorScheme.tertiary,
                onClick = { onProfesorClick("1") }
            )

            TarjetaCategoria(
                modifier = Modifier.weight(1f),
                icono = R.drawable.logo_norecomendado,
                fotoProfesor = R.drawable.profe_norecomendado,
                titulo = "PROFES PESADOS",
                descripcion = "Alto nivel de exigencia y dificultad.",
                valoracion = profesoresPesados.firstOrNull()?.difficulty ?: 0.0,
                etiqueta = "Dificultad",
                colorEtiqueta = colorScheme.error,
                onClick = onBuscarClick
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun TarjetaCategoria(
    modifier: Modifier = Modifier,
    icono: Int,
    fotoProfesor: Int,
    titulo: String,
    descripcion: String,
    valoracion: Double,
    etiqueta: String,
    colorEtiqueta: Color,
    onClick: () -> Unit
) {
    val typography = MaterialTheme.typography
    
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Usamos Image sin tinte para mantener el color original del logo
                Image(
                    painter = painterResource(id = icono),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )
                Image(
                    painter = painterResource(id = fotoProfesor),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = titulo,
                style = typography.titleLarge.copy(fontSize = 12.sp),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                minLines = 2
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = descripcion,
                style = typography.bodyLarge.copy(fontSize = 10.sp, lineHeight = 14.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                color = colorEtiqueta.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "%.1f".format(valoracion),
                        style = typography.titleLarge.copy(fontSize = 13.sp),
                        color = colorEtiqueta
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = etiqueta,
                        style = typography.bodyLarge.copy(fontSize = 10.sp),
                        color = colorEtiqueta
                    )
                }
            }
        }
    }
}
