package com.example.profedex.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.profedex.R
import com.example.profedex.data.model.BuscarUiState
import com.example.profedex.data.model.ProfesorFB
import com.example.profedex.viewmodel.BuscarProfesoresViewModel

// Colores del tema — basados en tu diseño
private val RojoFI = Color(0xFFCC1919)
private val Crema = Color(0xFFF5EFE6)
private val CremaDark = Color(0xFFEDE4D8)
private val TextoPrincipal = Color(0xFF1A1A1A)
private val TextoSecundario = Color(0xFF666666)
private val Blanco = Color(0xFFFFFFFF)
private val EstrellaDor = Color(0xFFFFB800)

@Composable
fun BuscarProfesoresScreen(
    onVolverClick: () -> Unit = {},
    onProfesorClick: (ProfesorFB) -> Unit = {},
    viewModel: BuscarProfesoresViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Crema
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LogoHeader()
            BarraTitulo(onVolverClick = onVolverClick)

            when (val estado = uiState) {
                is BuscarUiState.Cargando -> PantallaCargando()
                is BuscarUiState.Error -> PantallaError(estado.mensaje)
                is BuscarUiState.Exito -> ContenidoBusqueda(
                    estado = estado,
                    onTextoCambia = viewModel::onBusquedaCambia,
                    onFiltroClick = viewModel::onFiltroSeleccionado,
                    onProfesorClick = onProfesorClick
                )
            }
        }
    }
}

@Composable
private fun LogoHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Crema)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_profedex),
            contentDescription = "Logo ProfeDex",
            modifier = Modifier.size(60.dp)
        )
    }
}

@Composable
private fun BarraTitulo(onVolverClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(RojoFI)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onVolverClick,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = Blanco
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Buscar profesores",
            color = Blanco,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ContenidoBusqueda(
    estado: BuscarUiState.Exito,
    onTextoCambia: (String) -> Unit,
    onFiltroClick: (String) -> Unit,
    onProfesorClick: (ProfesorFB) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Text(
                text = "Encuentra profesores por nombre, materia o dificultad",
                color = TextoSecundario,
                fontSize = 13.sp,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }

        item {
            BarraBusqueda(
                texto = estado.textoBusqueda,
                onTextoCambia = onTextoCambia
            )
        }

        item {
            ChipsFiltros(
                filtros = estado.filtros,
                filtroActivo = estado.filtroActivo,
                onFiltroClick = onFiltroClick
            )
        }

        if (estado.profesores.isEmpty()) {
            item { SinResultados() }
        } else {
            items(
                items = estado.profesores,
                key = { it.idDoc }
            ) { profesor ->
                ProfesorCard(
                    profesor = profesor,
                    onClick = { onProfesorClick(profesor) }
                )
            }
        }
    }
}

@Composable
private fun BarraBusqueda(
    texto: String,
    onTextoCambia: (String) -> Unit
) {
    OutlinedTextField(
        value = texto,
        onValueChange = onTextoCambia,
        placeholder = {
            Text(
                "Buscar profesor o materia",
                color = TextoSecundario,
                fontSize = 15.sp
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                tint = RojoFI
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(Blanco, RoundedCornerShape(50.dp)),
        shape = RoundedCornerShape(50.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = RojoFI,
            unfocusedBorderColor = Color(0xFFDDDDDD),
            focusedContainerColor = Blanco,
            unfocusedContainerColor = Blanco
        ),
        singleLine = true
    )
}

@Composable
private fun ChipsFiltros(
    filtros: List<String>,
    filtroActivo: String?,
    onFiltroClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            filtros.take(3).forEach { filtro ->
                ChipFiltro(
                    texto = filtro,
                    activo = filtro == filtroActivo
                ) { onFiltroClick(filtro) }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            filtros.drop(3).forEach { filtro ->
                ChipFiltro(
                    texto = filtro,
                    activo = filtro == filtroActivo
                ) { onFiltroClick(filtro) }
            }
        }
    }
}

@Composable
private fun ChipFiltro(
    texto: String,
    activo: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50.dp),
        color = if (activo) RojoFI else Blanco,
        border = if (!activo) BorderStroke(1.dp, Color(0xFFCCCCCC)) else null,
        modifier = Modifier.height(34.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 14.dp)
        ) {
            Text(
                text = texto,
                color = if (activo) Blanco else TextoPrincipal,
                fontSize = 13.sp,
                fontWeight = if (activo) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}

@Composable
private fun ProfesorCard(
    profesor: ProfesorFB,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Blanco),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            AvatarProfesor(profesor = profesor)

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = profesor.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = TextoPrincipal
                )

                Text(
                    text = profesor.materia.firstOrNull() ?: "Sin materia asignada",
                    fontSize = 13.sp,
                    color = TextoSecundario,
                    modifier = Modifier.padding(top = 2.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Estrellas(
                    calificacion = profesor.averageRating,
                    modifier = Modifier.padding(top = 6.dp)
                )

                if (profesor.tags.isNotEmpty()) {
                    Row(
                        modifier = Modifier.padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        profesor.tags.take(3).forEach { etiqueta ->
                            EtiquetaChip(texto = etiqueta)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Estrellas(
    calificacion: Double,
    modifier: Modifier = Modifier,
    totalEstrellas: Int = 5
) {
    Row(modifier = modifier) {
        repeat(totalEstrellas) { index ->
            val llena = index < calificacion.toInt()
            Icon(
                imageVector = if (llena) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = null,
                tint = if (llena) EstrellaDor else Color(0xFFCCCCCC),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
@Composable
private fun EtiquetaChip(texto: String, esDestacada: Boolean = false) {
    Surface(
        shape = RoundedCornerShape(50.dp),
        color = if (esDestacada) RojoFI else CremaDark,
        modifier = Modifier.height(28.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            Text(
                text = texto,
                fontSize = 11.sp,
                color = if (esDestacada) Blanco else TextoPrincipal,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun AvatarProfesor(profesor: ProfesorFB) {
    // Definimos esquinas casi rectas de 6.dp para el estilo pixel art
    val formaPixelArt = RoundedCornerShape(6.dp)
    val bordeGrisPixel = BorderStroke(1.5.dp, Color(0xFFBBBBBB))

    // Llama directamente a la función que ya compila en tu otra pantalla
    val pokemonUrl = obtenerPokemonPixelUrl(profesor.avatarUrl, profesor.idDoc)

    AsyncImage(
        model = pokemonUrl,
        contentDescription = "Avatar de ${profesor.name}",
        modifier = Modifier
            .size(54.dp)
            .border(bordeGrisPixel, formaPixelArt)
            .clip(formaPixelArt)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun PantallaCargando() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = RojoFI)
    }
}

@Composable
private fun PantallaError(mensaje: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Error: $mensaje",
            color = RojoFI,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun SinResultados() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🔍", fontSize = 48.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Sin resultados",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextoPrincipal
            )
            Text(
                text = "Intenta con otro nombre o filtro",
                fontSize = 14.sp,
                color = TextoSecundario,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}