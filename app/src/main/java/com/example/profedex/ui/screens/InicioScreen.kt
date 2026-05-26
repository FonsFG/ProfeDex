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
    
    // Estado para saber qué lista mostrar (null = ninguna, "recomendados" o "pesados")
    var categoriaSeleccionada by remember { mutableStateOf<String?>(null) }
    
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // ── HEADER ────────────────────────
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
                        color = colorScheme.onError,
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

        // ── TARJETAS DE CATEGORÍA (LOS CUADRITOS) ──────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TarjetaCategoria(
                modifier = Modifier.weight(1f),
                icono = R.drawable.estrella_recomendado,
                titulo = "PROFES MÁS RECOMENDADOS",
                descripcion = "Los mejores evaluados.",
                valoracion = profesoresRecomendados.firstOrNull()?.averageRating ?: 0.0,
                etiqueta = "Promedio",
                colorEtiqueta = colorScheme.tertiary,
                seleccionada = categoriaSeleccionada == "recomendados",
                onClick = { 
                    categoriaSeleccionada = if (categoriaSeleccionada == "recomendados") null else "recomendados"
                }
            )

            TarjetaCategoria(
                modifier = Modifier.weight(1f),
                icono = R.drawable.logo_norecomendado,
                titulo = "PROFES PESADOS",
                descripcion = "Alto nivel de exigencia.",
                valoracion = profesoresPesados.firstOrNull()?.difficulty ?: 0.0,
                etiqueta = "Dificultad",
                colorEtiqueta = colorScheme.error,
                seleccionada = categoriaSeleccionada == "pesados",
                onClick = { 
                    categoriaSeleccionada = if (categoriaSeleccionada == "pesados") null else "pesados"
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ── LISTA DINÁMICA (APARECE AL CLICKEAR UN CUADRITO) ────────
        when (categoriaSeleccionada) {
            "recomendados" -> {
                SectionHeader("LISTADO: RECOMENDADOS", colorScheme.tertiary)
                if (profesoresRecomendados.isEmpty()) {
                    Text("No hay profesores recomendados", modifier = Modifier.padding(16.dp))
                } else {
                    profesoresRecomendados.forEach { profesor ->
                        ProfesorCardInicio(
                            profesor = profesor,
                            colorEtiqueta = colorScheme.tertiary,
                            etiqueta = "Promedio",
                            valor = profesor.averageRating,
                            onClick = { onProfesorClick(profesor) }
                        )
                    }
                }
            }
            "pesados" -> {
                SectionHeader("LISTADO: PESADOS", colorScheme.error)
                if (profesoresPesados.isEmpty()) {
                    Text("No hay profesores pesados", modifier = Modifier.padding(16.dp))
                } else {
                    profesoresPesados.forEach { profesor ->
                        ProfesorCardInicio(
                            profesor = profesor,
                            colorEtiqueta = colorScheme.error,
                            etiqueta = "Dificultad",
                            valor = profesor.difficulty,
                            onClick = { onProfesorClick(profesor) }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun TarjetaCategoria(
    modifier: Modifier = Modifier,
    icono: Int,
    titulo: String,
    descripcion: String,
    valoracion: Double,
    etiqueta: String,
    colorEtiqueta: Color,
    seleccionada: Boolean,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        onClick = onClick,
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (seleccionada) colorEtiqueta.copy(alpha = 0.1f) else colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(if (seleccionada) 8.dp else 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = icono),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                minLines = 2
            )
            Text(
                text = descripcion,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 10.sp, color = Color.Gray),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "%.1f %s".format(valoracion, etiqueta),
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 11.sp, fontWeight = FontWeight.Bold, color = colorEtiqueta)
            )
        }
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

            // ─── AQUÍ QUEDÓ EL CAMBIO DEL PIXEL ART ───
            AsyncImage(
                model = obtenerPokemonPixelUrl(profesor.avatarUrl, profesor.idDoc),
                contentDescription = "Sprite de ${profesor.name}",
                placeholder = painterResource(id = R.drawable.profe_placeholder),
                error = painterResource(id = R.drawable.profe_placeholder),
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .padding(1.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = profesor.name,
                    style = typography.titleLarge.copy(fontSize = 15.sp),
                    maxLines = 1
                )
                Text(
                    // .firstOrNull() toma la primera materia de la lista para no saturar la tarjeta pequeña,
                    // y si la lista está vacía pone "Sin materia"
                    text = profesor.materia.firstOrNull() ?: "Sin materia",
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
/**
 * Genera la URL del sprite en Pixel Art (Frente) basado en el número de Pokémon o ID del profesor.
 */
fun obtenerPokemonPixelUrl(avatarUrl: String, profesorId: String, limitePokemon: Int = 151): String {
    val pokemonId = if (avatarUrl.isNotEmpty()) {
        avatarUrl
    } else {
        (kotlin.math.abs(profesorId.hashCode()) % limitePokemon + 1).toString()
    }
    // Esta URL apunta directo al sprite pixelado tradicional en los servidores de la PokeAPI
    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$pokemonId.png"
}