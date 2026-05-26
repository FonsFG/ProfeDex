package com.example.profedex.ui.theme

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.profedex.R
import com.example.profedex.ui.screens.BuscarProfesoresScreen
import com.example.profedex.ui.screens.ChatBotScreen
import com.example.profedex.ui.screens.EvaluationScreen
import com.example.profedex.ui.screens.EvaluarProfesorScreen // ← Tu nueva pantalla de evaluación
import com.example.profedex.ui.screens.InicioScreen
import com.example.profedex.ui.screens.LoginScreen
import com.example.profedex.ui.screens.PerfilUsuarioScreen
import com.example.profedex.ui.screens.ProfesorProfileScreen
import com.example.profedex.ui.screens.RegistroScreen
import com.example.profedex.viewmodel.ProfesorViewModelFB
import com.example.profedex.viewmodel.UsuarioViewModel

object Rutas {
    const val LOGIN = "login"
    const val INICIO = "inicio"
    const val PERFIL = "perfil"
    const val PROFESOR = "profesor"
    const val REGISTRAR = "registrar" // Se queda para la pestaña de crear nuevos profes
    const val BUSCADOR = "buscador"
    const val REGISTRO = "registro"
    const val EVALUAR = "evaluar"     // ← Nueva ruta exclusiva para calificar profesores
    const val CHATBOT = "chatbot"     // ← Asistente IA
}

data class ItemNavBar(
    val ruta: String,
    val icono: Int,
    val descripcion: String
)

@Composable
fun InicioApp() {
    val navController = rememberNavController()
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val profesorViewModel: ProfesorViewModelFB = viewModel()
    val usuarioViewModel: UsuarioViewModel = viewModel()

    val itemsNavBar = listOf(
        ItemNavBar(Rutas.INICIO,     R.drawable.home,     "Inicio"),
        ItemNavBar(Rutas.PERFIL,     R.drawable.perfil,   "Perfil"),
        ItemNavBar(Rutas.REGISTRAR,  R.drawable.registro, "Registrar")
    )

    val backStack by navController.currentBackStackEntryAsState()
    val rutaActual = backStack?.destination?.route
    // Ocultamos la barra tanto en pantallas de login/registro como en el buscador y el formulario de evaluar
    val mostrarBottomBar = rutaActual != Rutas.LOGIN &&
            rutaActual != Rutas.BUSCADOR &&
            rutaActual != Rutas.REGISTRO &&
            rutaActual != Rutas.EVALUAR &&
            rutaActual != Rutas.CHATBOT

    Scaffold(
        bottomBar = {
            if (mostrarBottomBar) {
                NavigationBar(
                    containerColor = colorScheme.surface,
                    tonalElevation = NavigationBarDefaults.Elevation
                ) {
                    itemsNavBar.forEach { item ->
                        NavigationBarItem(
                            selected = rutaActual == item.ruta,
                            onClick = {
                                if (rutaActual != item.ruta) {
                                    navController.navigate(item.ruta) {
                                        popUpTo(Rutas.INICIO) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(id = item.icono),
                                    contentDescription = item.descripcion
                                )
                            },
                            label = {
                                Text(
                                    text = item.descripcion,
                                    style = typography.bodyLarge.copy(fontSize = 10.sp)
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = colorScheme.onError,
                                selectedTextColor = colorScheme.onError,
                                unselectedIconColor = colorScheme.onSurfaceVariant,
                                unselectedTextColor = colorScheme.onSurfaceVariant,
                                indicatorColor = colorScheme.secondaryContainer
                            )
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Rutas.LOGIN,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Rutas.LOGIN) {
                LoginScreen(
                    viewModel = usuarioViewModel,
                    onLoginSuccess = { username ->
                        usuarioViewModel.fetchUsuario(username)
                        navController.navigate(Rutas.INICIO) {
                            popUpTo(Rutas.LOGIN) { inclusive = true }
                        }
                    },
                    onRegisterClick = {
                        navController.navigate(Rutas.REGISTRO)
                    }
                )
            }

            composable(Rutas.REGISTRO) {
                RegistroScreen(
                    viewModel = usuarioViewModel,
                    onRegistroExitoso = { username ->
                        usuarioViewModel.fetchUsuario(username)
                        navController.navigate(Rutas.INICIO) {
                            popUpTo(Rutas.LOGIN) { inclusive = true }
                        }
                    },
                    onVolverClick = { navController.popBackStack() }
                )
            }

            composable(Rutas.INICIO) {
                InicioScreen(
                    onProfesorClick = { profesor ->
                        profesorViewModel.seleccionarProfesor(profesor)
                        navController.navigate(Rutas.PROFESOR)
                    },
                    onSearchClick = {
                        navController.navigate(Rutas.BUSCADOR)
                    },
                    onChatClick = {
                        navController.navigate(Rutas.CHATBOT)
                    }
                )
            }

            composable(Rutas.CHATBOT) {
                ChatBotScreen(
                    onBackClick = { navController.popBackStack() },
                    profesorViewModel = profesorViewModel
                )
            }

            composable(Rutas.PERFIL) {
                PerfilUsuarioScreen(viewModel = usuarioViewModel)
            }

            // Mantiene la pantalla original para dar de alta nuevos profesores desde el NavBar
            composable(Rutas.REGISTRAR) {
                EvaluationScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(Rutas.PROFESOR) {
                val profesor = profesorViewModel.state
                ProfesorProfileScreen(
                    professor = profesor,
                    viewModel = profesorViewModel,
                    onBackClick = { navController.popBackStack() },
                    onEvaluarClick = { navController.navigate(Rutas.EVALUAR) } // ← Redirecciona a la nueva ruta
                )
            }

            // NUEVO DESTINO: Abre el formulario de calificación cargando el profesor guardado en el State
            composable(Rutas.EVALUAR) {
                val profesorActual = profesorViewModel.state
                EvaluarProfesorScreen(
                    professor = profesorActual,
                    viewModel = profesorViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(Rutas.BUSCADOR) {
                BuscarProfesoresScreen(
                    onVolverClick = { navController.popBackStack() },
                    onProfesorClick = { profesor ->
                        profesorViewModel.seleccionarProfesor(profesor)
                        navController.navigate(Rutas.PROFESOR)
                    }
                )
            }
        }
    }
}