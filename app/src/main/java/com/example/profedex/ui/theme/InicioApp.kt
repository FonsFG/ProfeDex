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
import com.example.profedex.ui.screens.EvaluationScreen
import com.example.profedex.ui.screens.InicioScreen
import com.example.profedex.ui.screens.LoginScreen
import com.example.profedex.ui.screens.PerfilUsuarioScreen
import com.example.profedex.ui.screens.ProfesorProfileScreen
import com.example.profedex.viewmodel.ProfesorViewModel

object Rutas {
    const val LOGIN = "login"
    const val INICIO = "inicio"
    const val PERFIL = "perfil"
    const val PROFESOR = "profesor"
    const val EVALUACION = "evaluacion"
    const val BUSCADOR = "buscador"
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
    
    val profesorViewModel: ProfesorViewModel = viewModel()

    // Definición de los botones de la barra inferior
    val itemsNavBar = listOf(
        ItemNavBar(Rutas.INICIO,     R.drawable.home,     "Inicio"),
        ItemNavBar(Rutas.PERFIL,     R.drawable.perfil,   "Perfil"),
        ItemNavBar(Rutas.EVALUACION, R.drawable.registro, "Evaluación"),
        ItemNavBar(Rutas.PROFESOR,   R.drawable.perfil,   "Profesor")
    )

    val backStack by navController.currentBackStackEntryAsState()
    val rutaActual = backStack?.destination?.route
    val mostrarBottomBar = rutaActual != Rutas.LOGIN && rutaActual != Rutas.BUSCADOR

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
                                selectedIconColor = colorScheme.onError, // Cambio de primary a onError
                                selectedTextColor = colorScheme.onError, // Cambio de primary a onError
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
            // 1. Pantalla de Login
            composable(Rutas.LOGIN) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Rutas.INICIO) {
                            popUpTo(Rutas.LOGIN) { inclusive = true }
                        }
                    }
                )
            }

            // 2. Pantalla de Inicio (Pestaña)
            composable(Rutas.INICIO) {
                InicioScreen(
                    onProfesorClick = {
                        navController.navigate(Rutas.PROFESOR)
                    },
                    onSearchClick = {
                        navController.navigate(Rutas.BUSCADOR)
                    }
                )
            }
            
            // 3. Perfil de Usuario (Pestaña)
            composable(Rutas.PERFIL) {
                PerfilUsuarioScreen()
            }

            // 4. Evaluación (Pestaña)
            composable(Rutas.EVALUACION) {
                EvaluationScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
            
            // 5. Perfil de Profesor (Pestaña / Detalle)
            composable(Rutas.PROFESOR) {
                val profesorState by profesorViewModel.profesorState.collectAsState()
                profesorState?.let { profesor ->
                    ProfesorProfileScreen(
                        professor = profesor,
                        reviews = profesorViewModel.getReviewsDePrueba(),
                        onBackClick = { navController.popBackStack() },
                        onEvaluarClick = { navController.navigate(Rutas.EVALUACION) }
                    )
                }
            }

            // 6. Buscador
            composable(Rutas.BUSCADOR) {
                BuscarProfesoresScreen(
                    onVolverClick = { navController.popBackStack() }
                )
            }
        }
    }
}
