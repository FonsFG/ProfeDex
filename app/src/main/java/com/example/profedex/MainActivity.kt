package com.example.profedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.profedex.ui.theme.InicioApp
import com.example.profedex.ui.theme.theme.ProfeDexTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProfeDexTheme {
                // Esto lanza la app completa con el diseño y navegación que arreglamos
                InicioApp()
            }
        }
    }
}
