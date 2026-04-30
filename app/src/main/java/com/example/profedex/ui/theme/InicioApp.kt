package com.example.profedex.ui.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.profedex.ui.screens.InicioScreen
import com.example.profedex.ui.screens.ProfesorProfileScreen
import com.example.profedex.viewmodel.ProfesorViewModel
import androidx.navigation.NavGraphBuilder
import com.example.profedex.ui.screens.EvaluarProfesorScreen

object Rutas {
    const val INICIO = "inicio"
    const val PERFIL = "perfil"
    const val EVALUAR = "evaluar"
    //const val FACULTAD = "facultad" //aqui pueden poner otra pestaña
    const val AJUSTES = "ajustes" //O PONER MENU
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

    val itemsNavBar = listOf(
        ItemNavBar(Rutas.INICIO,    R.drawable.inicio, "Inicio"),
        ItemNavBar(Rutas.PERFIL,    R.drawable.perfil,   "Perfil"),
        ItemNavBar(Rutas.EVALUAR,  R.drawable.registro, "Evaluar"),
        //ItemNavBar(Rutas.FACULTAD,  R.drawable.facultad, "Facultad"),
        ItemNavBar(Rutas.AJUSTES,   R.drawable.ajustes,  "Ajustes")
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = colorScheme.surface,
                tonalElevation = NavigationBarDefaults.Elevation
            ) {
                val backStack by navController.currentBackStackEntryAsState()
                val rutaActual = backStack?.destination?.route

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
                            selectedIconColor = colorScheme.primary,
                            selectedTextColor = colorScheme.primary,
                            unselectedIconColor = colorScheme.onSurfaceVariant,
                            unselectedTextColor = colorScheme.onSurfaceVariant,
                            indicatorColor = colorScheme.secondaryContainer
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Rutas.INICIO,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Rutas.INICIO) {
                InicioScreen(
                    onBuscarClick = { /* ... */ },
                    onProfesorClick = { id ->
                        navController.navigate(Rutas.PERFIL)
                    }
                )
            }

            // Usamos una función de extensión para "encapsular" el perfil
            perfilGraph(
                onBackClick = { navController.popBackStack() },
                onEvaluarClick = { navController.navigate(Rutas.EVALUAR) }
            )

            composable(Rutas.EVALUAR) { EvaluarProfesorScreen(
                onBackClick = { navController.popBackStack() }
            ) }
            composable(Rutas.AJUSTES)  { PlaceholderScreen("Ajustes") }
        }
    }
}


// Esta función extiende el NavGraphBuilder para manejar la ruta del perfil
fun NavGraphBuilder.perfilGraph(
    onBackClick: () -> Unit,
    onEvaluarClick: () -> Unit
) {
    composable(Rutas.PERFIL) {
        // El ViewModel se obtiene aquí, manteniendo el NavHost principal despejado
        val profeViewModel: ProfesorViewModel = viewModel()
        val profesor by profeViewModel.profesorState.collectAsState()

        profesor?.let { profe ->
            ProfesorProfileScreen(
                professor = profe,
                reviews = profeViewModel.getReviewsDePrueba(),
                onBackClick = onBackClick,
                onEvaluarClick = onEvaluarClick
            )
        }
    }
}

@Composable
fun PlaceholderScreen(nombre: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text(
            text = "Próximamente: $nombre",
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
            color = MaterialTheme.colorScheme.outline
        )
    }
}
