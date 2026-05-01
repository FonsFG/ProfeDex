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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.profedex.R
import com.example.profedex.viewmodel.UsuarioViewModel

@Composable
fun PerfilUsuarioScreen(
    viewModel: UsuarioViewModel = viewModel()
) {
    val usuario by viewModel.usuario.collectAsState()
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    var modoEdicion by remember { mutableStateOf(false) }
    var mensajeGuardado by remember { mutableStateOf(false) }

    val avatars = listOf(
        R.drawable.avatar_1,
        R.drawable.avatar_2,
        R.drawable.avatar_3,
        R.drawable.avatar_4,
        R.drawable.avatar_5
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorScheme.onError) // Cambio de primary a onError
                .padding(vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "MI PERFIL",
                color = colorScheme.primary, // Ajuste para visibilidad sobre blanco
                style = typography.titleLarge.copy(fontSize = 24.sp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = avatars[usuario.avatar]),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(colorScheme.secondaryContainer),
                    contentScale = ContentScale.Crop
                )

                if (modoEdicion) {
                    Text(
                        text = "Selecciona un avatar", 
                        style = typography.bodyLarge.copy(fontSize = 12.sp),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Row(
                        modifier = Modifier.padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        avatars.forEachIndexed { index, avatar ->
                            Image(
                                painter = painterResource(id = avatar),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(45.dp)
                                    .clip(CircleShape)
                                    .clickable { viewModel.actualizarAvatar(index) }
                                    .background(if (usuario.avatar == index) colorScheme.onError else Color.Transparent), // Cambio de primary a onError
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Campos Editables
                PerfilField("Nombre Completo", usuario.nombre, modoEdicion) { viewModel.actualizarNombre(it) }
                PerfilField("Carrera", usuario.carrera, modoEdicion) { viewModel.actualizarCarrera(it) }
                PerfilField("Semestre", usuario.semestre, modoEdicion) { viewModel.actualizarSemestre(it) }
                PerfilField("Correo Institucional", usuario.correo, modoEdicion) { viewModel.actualizarCorreo(it) }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (modoEdicion) mensajeGuardado = true
                        else mensajeGuardado = false
                        modoEdicion = !modoEdicion
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (modoEdicion) colorScheme.tertiary else colorScheme.onError, // Cambio de primary a onError
                        contentColor = if (modoEdicion) colorScheme.onTertiary else colorScheme.primary // Ajuste contraste
                    )
                ) {
                    Text(
                        if (modoEdicion) "GUARDAR CAMBIOS" else "EDITAR PERFIL",
                        style = typography.titleLarge.copy(fontSize = 14.sp)
                    )
                }

                if (mensajeGuardado && !modoEdicion) {
                    Text(
                        "¡Cambios guardados con éxito! ✅",
                        color = colorScheme.onError, // Cambio de primary a onError (podría ser invisible sobre blanco)
                        style = typography.bodyLarge.copy(fontSize = 12.sp),
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun PerfilField(label: String, value: String, enabled: Boolean, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(
            text = label, 
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 12.sp, color = MaterialTheme.colorScheme.onError) // Cambio de primary a onError
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                focusedBorderColor = MaterialTheme.colorScheme.onError // Cambio de primary a onError
            )
        )
    }
}
