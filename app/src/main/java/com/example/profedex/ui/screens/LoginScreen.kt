package com.example.profedex.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.profedex.R
import com.example.profedex.viewmodel.AuthResult
import com.example.profedex.viewmodel.UsuarioViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit = {},
    onRegisterClick: () -> Unit = {},
    viewModel: UsuarioViewModel = viewModel()
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val loginResult by viewModel.loginResult.collectAsState()
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    LaunchedEffect(loginResult) {
        val resultado = loginResult
        if (resultado is AuthResult.Success) {
            onLoginSuccess(resultado.username)
            viewModel.resetLoginResult()
        }
    }

    val mensajeError = (loginResult as? AuthResult.Error)?.mensaje
    val cargando = loginResult is AuthResult.Loading

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
                .height(260.dp)
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
                    .background(Color.Black.copy(alpha = 0.4f))
            )

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_profedex),
                    contentDescription = "Logo ProfeDex",
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "PROFEDEX",
                    style = typography.titleLarge.copy(
                        fontSize = 36.sp,
                        letterSpacing = 2.sp,
                        color = Color.White
                    )
                )
                Text(
                    text = "Facultad de Ingeniería UNAM",
                    style = typography.bodyLarge.copy(fontSize = 12.sp, color = Color.White.copy(alpha = 0.9f))
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // FORMS
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Iniciar Sesión",
                style = typography.titleLarge.copy(fontSize = 24.sp),
                color = colorScheme.onError,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    if (loginResult is AuthResult.Error) viewModel.resetLoginResult()
                },
                label = { Text("Usuario", style = typography.bodyLarge) },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = colorScheme.onError) },
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

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    if (loginResult is AuthResult.Error) viewModel.resetLoginResult()
                },
                label = { Text("Contraseña", style = typography.bodyLarge) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = colorScheme.onError) },
                trailingIcon = {
                    val icon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = icon,
                            contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                            tint = colorScheme.onError
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.login(username.trim(), password) },
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
                        text = "ENTRAR",
                        style = typography.titleLarge.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿Eres nuevo?",
                    style = typography.bodyLarge.copy(fontSize = 14.sp),
                    color = colorScheme.onSurfaceVariant
                )
                TextButton(onClick = onRegisterClick) {
                    Text(
                        text = "Crea una cuenta",
                        style = typography.bodyLarge.copy(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                        color = colorScheme.onError
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Hecho por y para la comunidad FI",
                style = typography.bodyLarge.copy(fontSize = 10.sp),
                color = colorScheme.onError.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
