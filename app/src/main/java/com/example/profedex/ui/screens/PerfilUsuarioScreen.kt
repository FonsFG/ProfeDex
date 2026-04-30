package com.example.profedex.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.profedex.R
import com.example.profedex.viewmodel.UsuarioViewModel

@Composable
fun PerfilUsuarioScreen(
    viewModel: UsuarioViewModel = viewModel()
) {
    val usuario by viewModel.usuario.collectAsState()
    val colorScheme = MaterialTheme.colorScheme

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
                .background(colorScheme.error)
                .padding(vertical = 18.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "MI PERFIL",
                color = colorScheme.onError,
                style = MaterialTheme.typography.titleLarge
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = MaterialTheme.shapes.large,
            elevation = CardDefaults.cardElevation(4.dp)
        ) {

            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Image(
                    painter = painterResource(id = avatars[usuario.avatar]),
                    contentDescription = "Avatar seleccionado",
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Elige tu avatar")

                Spacer(modifier = Modifier.height(8.dp))


                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    avatars.forEachIndexed { index, avatar ->
                        Image(
                            painter = painterResource(id = avatar),
                            contentDescription = "Avatar $index",
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .clickable(enabled = modoEdicion) {
                                    viewModel.actualizarAvatar(index)
                                },
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Nombre
                OutlinedTextField(
                    value = usuario.nombre,
                    onValueChange = { viewModel.actualizarNombre(it) },
                    label = { Text("Nombre") },
                    enabled = modoEdicion,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Carrera
                OutlinedTextField(
                    value = usuario.carrera,
                    onValueChange = { viewModel.actualizarCarrera(it) },
                    label = { Text("Carrera") },
                    enabled = modoEdicion,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Semestre
                OutlinedTextField(
                    value = usuario.semestre,
                    onValueChange = { viewModel.actualizarSemestre(it) },
                    label = { Text("Semestre") },
                    enabled = modoEdicion,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Correo
                OutlinedTextField(
                    value = usuario.correo,
                    onValueChange = { viewModel.actualizarCorreo(it) },
                    label = { Text("Correo") },
                    enabled = modoEdicion,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))


                Button(
                    onClick = {
                        if (modoEdicion) {
                            mensajeGuardado = true
                        }
                        modoEdicion = !modoEdicion
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorScheme.error,
                        contentColor = colorScheme.onError
                    )
                ) {
                    Text(if (modoEdicion) "Guardar cambios" else "Editar")
                }


                if (mensajeGuardado) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Cambios guardados ✅",
                        color = colorScheme.primary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}