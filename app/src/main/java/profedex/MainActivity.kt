package profedex // Ajustado a tu estructura real según tu captura

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import profedex.ui.screens.EvaluarProfesorScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Usamos el tema genérico para no depender de la rama de tu compañero aún
            MaterialTheme {
                EvaluarProfesorScreen()
            }
        }
    }
}