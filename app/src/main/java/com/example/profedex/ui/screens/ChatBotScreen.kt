package com.example.profedex.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.profedex.viewmodel.IAViewModel
import com.example.profedex.viewmodel.ProfesorViewModelFB

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBotScreen(
    onBackClick: () -> Unit = {},
    profesorViewModel: ProfesorViewModelFB,
    iaViewModel: IAViewModel = viewModel()
) {
    val profesores by profesorViewModel.dataProfeDex.collectAsState()
    val respuesta by iaViewModel.respuestaChat.collectAsState()
    val cargando by iaViewModel.chatCargando.collectAsState()

    var pregunta by remember { mutableStateOf("") }
    var ultimaPregunta by remember { mutableStateOf<String?>(null) }

    DisposableEffect(Unit) { onDispose { iaViewModel.limpiarChat() } }

    val typography = MaterialTheme.typography
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        containerColor = colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.AutoAwesome,
                            contentDescription = null,
                            tint = colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Asistente ProfeDex",
                            style = typography.titleLarge.copy(fontSize = 18.sp, color = colorScheme.primary)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás",
                            tint = colorScheme.primary
                        )
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
                .padding(16.dp)
        ) {
            // ── ÁREA DE CONVERSACIÓN ────────────────────────────
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "¡Hola! 👋 Pregúntame qué profe te conviene según materia, dificultad o rating. " +
                                "Por ejemplo: \"¿Quién es el mejor para Cálculo?\"",
                        style = typography.bodyLarge.copy(fontSize = 13.sp),
                        color = colorScheme.onSurface,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                ultimaPregunta?.let { texto ->
                    BurbujaUsuario(texto = texto)
                }

                if (cargando) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(
                            color = colorScheme.onError,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Pensando…",
                            style = typography.bodyLarge.copy(fontSize = 13.sp, color = colorScheme.onSurfaceVariant)
                        )
                    }
                }

                respuesta?.let { texto ->
                    BurbujaIA(texto = texto)
                }
            }

            // ── BARRA DE ENTRADA ────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = pregunta,
                    onValueChange = { pregunta = it },
                    placeholder = {
                        Text(
                            "Escribe tu pregunta…",
                            style = typography.bodyLarge.copy(fontSize = 13.sp)
                        )
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    singleLine = false,
                    maxLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorScheme.onError,
                        unfocusedBorderColor = colorScheme.outline.copy(alpha = 0.5f),
                        cursorColor = colorScheme.onError,
                        focusedTextColor = colorScheme.onSurface,
                        unfocusedTextColor = colorScheme.onSurface
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                FilledIconButton(
                    onClick = {
                        if (pregunta.isNotBlank() && !cargando) {
                            ultimaPregunta = pregunta
                            iaViewModel.chatRecomendacion(pregunta, profesores)
                            pregunta = ""
                        }
                    },
                    enabled = pregunta.isNotBlank() && !cargando,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = colorScheme.onError,
                        contentColor = colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Enviar"
                    )
                }
            }
        }
    }
}

@Composable
private fun BurbujaUsuario(texto: String) {
    val colorScheme = MaterialTheme.colorScheme
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = colorScheme.onError),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 4.dp),
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Text(
                text = texto,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 13.sp),
                color = colorScheme.primary,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

@Composable
private fun BurbujaIA(texto: String) {
    val colorScheme = MaterialTheme.colorScheme
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = colorScheme.secondaryContainer.copy(alpha = 0.6f)
            ),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 4.dp, bottomEnd = 16.dp),
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Column(Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.AutoAwesome,
                        contentDescription = null,
                        tint = colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "ProfeDex IA",
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                        color = colorScheme.onSecondaryContainer
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = texto,
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 13.sp),
                    color = colorScheme.onSecondaryContainer
                )
            }
        }
    }
}
