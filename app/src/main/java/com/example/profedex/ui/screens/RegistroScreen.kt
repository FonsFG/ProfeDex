package com.example.profedex.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.profedex.R
import com.example.profedex.data.model.Usuario
import com.example.profedex.viewmodel.AuthResult
import com.example.profedex.viewmodel.UsuarioViewModel

@Composable
fun RegistroScreen(
    onRegistroExitoso: (String) -> Unit = {},
    onVolverClick: () -> Unit = {},
    viewModel: UsuarioViewModel = viewModel()
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var carrera by remember { mutableStateOf("") }
    var semestre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorLocal by remember { mutableStateOf<String?>(null) }

    val registroResult by viewModel.registroResult.collectAsState()
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    LaunchedEffect(registroResult) {
        val resultado = registroResult
        if (resultado is AuthResult.Success) {
            onRegistroExitoso(resultado.username)
            viewModel.resetRegistroResult()
        }
    }

    val mensajeError = errorLocal ?: (registroResult as? AuthResult.Error)?.mensaje
    val cargando = registroResult is AuthResult.Loading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.banner_fi),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.45f))
            )

            IconButton(
                onClick = onVolverClick,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White
                )
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_profedex),
                    contentDescription = "Logo ProfeDex",
                    modifier = Modifier.size(80.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Crea tu cuenta",
                    style = typography.titleLarge.copy(
                        fontSize = 26.sp,
                        letterSpacing = 1.sp,
                        color = Color.White
                    )
                )
                Text(
                    text = "Únete a la comunidad FI",
                    style = typography.bodyLarge.copy(fontSize = 12.sp, color = Color.White.copy(alpha = 0.9f))
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CampoRegistro(
                value = username,
                onValueChange = {
                    username = it
                    errorLocal = null
                    if (registroResult is AuthResult.Error) viewModel.resetRegistroResult()
                },
                label = "Usuario",
                icono = Icons.Default.Person
            )

            Spacer(modifier = Modifier.height(12.dp))

            CampoRegistro(
                value = password,
                onValueChange = {
                    password = it
                    errorLocal = null
                    if (registroResult is AuthResult.Error) viewModel.resetRegistroResult()
                },
                label = "Contraseña",
                icono = Icons.Default.Lock,
                esPassword = true,
                passwordVisible = passwordVisible,
                onTogglePassword = { passwordVisible = !passwordVisible }
            )

            Spacer(modifier = Modifier.height(12.dp))

            CampoRegistro(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    errorLocal = null
                },
                label = "Confirmar contraseña",
                icono = Icons.Default.Lock,
                esPassword = true,
                passwordVisible = passwordVisible,
                onTogglePassword = { passwordVisible = !passwordVisible }
            )

            Spacer(modifier = Modifier.height(12.dp))

            CampoRegistro(
                value = nombre,
                onValueChange = { nombre = it },
                label = "Nombre completo",
                icono = Icons.Default.Badge
            )

            Spacer(modifier = Modifier.height(12.dp))

            CampoRegistro(
                value = carrera,
                onValueChange = { carrera = it },
                label = "Carrera",
                icono = Icons.Default.School
            )

            Spacer(modifier = Modifier.height(12.dp))

            CampoRegistro(
                value = semestre,
                onValueChange = { semestre = it.filter { c -> c.isDigit() }.take(2) },
                label = "Semestre",
                icono = Icons.Default.School,
                keyboardType = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(12.dp))

            CampoRegistro(
                value = correo,
                onValueChange = { correo = it },
                label = "Correo institucional",
                icono = Icons.Default.Email,
                keyboardType = KeyboardType.Email
            )

            if (mensajeError != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = mensajeError,
                    color = colorScheme.error,
                    style = typography.bodyLarge.copy(fontSize = 13.sp),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val userTrim = username.trim()
                    val correoTrim = correo.trim()
                    when {
                        userTrim.isBlank() || password.isBlank() ->
                            errorLocal = "Usuario y contraseña son obligatorios"
                        password != confirmPassword ->
                            errorLocal = "Las contraseñas no coinciden"
                        nombre.isBlank() ->
                            errorLocal = "Ingresa tu nombre completo"
                        else -> {
                            errorLocal = null
                            viewModel.registrar(
                                Usuario(
                                    username = userTrim,
                                    password = password,
                                    nombre = nombre.trim(),
                                    carrera = carrera.trim(),
                                    semestre = semestre.trim(),
                                    correo = correoTrim,
                                    email = correoTrim,
                                    avatar = 0
                                )
                            )
                        }
                    }
                },
                enabled = !cargando,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.onError,
                    contentColor = colorScheme.primary
                )
            ) {
                if (cargando) {
                    CircularProgressIndicator(
                        color = colorScheme.primary,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(22.dp)
                    )
                } else {
                    Text(
                        text = "REGISTRARME",
                        style = typography.titleLarge.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿Ya tienes cuenta?",
                    style = typography.bodyLarge.copy(fontSize = 14.sp),
                    color = colorScheme.onSurfaceVariant
                )
                TextButton(onClick = onVolverClick) {
                    Text(
                        text = "Inicia sesión",
                        style = typography.bodyLarge.copy(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                        color = colorScheme.onError
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun CampoRegistro(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icono: ImageVector,
    esPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onTogglePassword: () -> Unit = {},
    keyboardType: KeyboardType = KeyboardType.Text
) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, style = typography.bodyLarge) },
        leadingIcon = { Icon(icono, contentDescription = null, tint = colorScheme.onError) },
        trailingIcon = {
            if (esPassword) {
                val icon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = onTogglePassword) {
                    Icon(
                        imageVector = icon,
                        contentDescription = if (passwordVisible) "Ocultar" else "Mostrar",
                        tint = colorScheme.onError
                    )
                }
            }
        },
        visualTransformation = if (esPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colorScheme.onError,
            unfocusedBorderColor = colorScheme.outline.copy(alpha = 0.5f),
            focusedLabelColor = colorScheme.onError,
            cursorColor = colorScheme.onError
        )
    )
}
