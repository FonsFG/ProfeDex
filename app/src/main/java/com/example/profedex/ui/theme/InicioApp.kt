package com.example.profedex.ui.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.profedex.R
import com.example.profedex.data.model.Usuario
import com.example.profedex.ui.screens.InicioScreen
import com.example.profedex.ui.screens.PerfilUsuarioScreen
import com.example.profedex.ui.screens.BuscarProfesoresScreen
import com.example.profedex.ui.screens.LoginScreen

object Rutas {
    const val LOGIN = "login"
    const val INICIO = "inicio"
    const val BUSCADOR = "buscador"
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

    // Estado para guardar el usuario una vez que inicie sesión
    var usuarioActual by remember { mutableStateOf<Usuario?>(null) }

    val itemsNavBar = listOf(
        ItemNavBar(Rutas.INICIO,    R.drawable.inicio, "Inicio"),
        ItemNavBar(Rutas.PERFIL,    R.drawable.perfil,   "Perfil"),
        ItemNavBar(Rutas.EVALUAR,  R.drawable.registro, "Evaluar"),
        //ItemNavBar(Rutas.FACULTAD,  R.drawable.facultad, "Facultad"),
        ItemNavBar(Rutas.AJUSTES,   R.drawable.ajustes,  "Ajustes")
    )

    val backStack by navController.currentBackStackEntryAsState()
    val rutaActual = backStack?.destination?.route

    // Aqui quite la barra de abajo para el login
    val mostrarNavBar = rutaActual != Rutas.LOGIN

    Scaffold(
        bottomBar = {
            if (mostrarNavBar) {
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
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Rutas.LOGIN,
            modifier = Modifier.padding(if (mostrarNavBar) paddingValues else PaddingValues(0.dp))
        ) {
            composable(Rutas.LOGIN) {
                LoginScreen(
                    onLoginSuccess = {
                        // usuario para probar la sesión luego jej
                        usuarioActual = Usuario(
                            username = "PepePolicia",
                            email = "hola@gmail.com",
                            carrera = "Ingeniería mecatrónica"
                        )
                        navController.navigate(Rutas.INICIO) {
                            popUpTo(Rutas.LOGIN) { inclusive = true }
                        }
                    },
                    onRegisterClick = {
                        // Aqui va lo del registro, pero eso luego jeje
                    }
                )
            }
            composable(Rutas.INICIO) {
                InicioScreen(
                    onBuscarClick = { navController.navigate(Rutas.BUSCADOR) }
                )
            }
            composable(Rutas.BUSCADOR) {
                BuscarProfesoresScreen(
                    onVolverClick = { navController.popBackStack() }
                )
            }
            composable(Rutas.PERFIL)    { PerfilUsuarioScreen() }
            composable(Rutas.PERFIL)    { PlaceholderScreen("Perfil de ${usuarioActual?.username ?: "Usuario"}") }
            composable(Rutas.EVALUAR)  { PlaceholderScreen("Evaluar") }
            //composable(Rutas.FACULTAD)  { PlaceholderScreen("Facultad") }
            composable(Rutas.AJUSTES)   { PlaceholderScreen("Ajustes") }
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
