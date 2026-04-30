package profedex.ui.screens // Paquete ajustado a tu directorio real

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check // Cambiado a Check para evitar librerías pesadas
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel // <-- Observa la 'v' minúscula al final
import profedex.Viewmodel.EvaluarProfesorViewModel
import profedex.ui.components.RatingBar
import profedex.ui.components.TagChip
import profedex.viewmodel.EvaluarProfesorViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EvaluarProfesorScreen(
    onBackClick: () -> Unit = {},
    viewModel: EvaluarProfesorViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // ── LOGO SUPERIOR (Comentado temporalmente por falta de recurso) ──
        /*
        Box(
            modifier = Modifier.fillMaxWidth().padding(top = 32.dp, bottom = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_profedex),
                contentDescription = "Logo ProfeDex",
                modifier = Modifier.size(64.dp)
            )
        }
        */

        Spacer(modifier = Modifier.height(32.dp))

        // ── FRANJA TÍTULO Y BACK ────────────────────────────
        Surface(
            color = colorScheme.error,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = colorScheme.onError
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Evaluar Profesor",
                    color = colorScheme.onError,
                    style = typography.titleLarge.copy(fontSize = 20.sp)
                )
            }
        }

        // ── FORMULARIO PRINCIPAL ───────────────────────────────────
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = "Profesor: ${uiState.profesorNombre}",
                style = typography.titleLarge.copy(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                color = colorScheme.onBackground
            )
            Text(
                text = "Materia: ${uiState.materia}",
                style = typography.titleLarge.copy(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                color = colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("Dominio del Tema:", style = typography.bodyLarge)
            RatingBar(rating = uiState.dominio, onRatingChanged = viewModel::onDominioChanged)

            Spacer(modifier = Modifier.height(16.dp))

            Text("Claridad:", style = typography.bodyLarge)
            RatingBar(rating = uiState.claridad, onRatingChanged = viewModel::onClaridadChanged)

            Spacer(modifier = Modifier.height(16.dp))

            Text("Nivel de dificultad:", style = typography.bodyLarge)
            RatingBar(rating = uiState.dificultad, onRatingChanged = viewModel::onDificultadChanged)

            Spacer(modifier = Modifier.height(24.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                uiState.tagsDisponibles.forEach { tag ->
                    TagChip(
                        text = tag,
                        isSelected = uiState.tagsSeleccionados.contains(tag),
                        onClick = { viewModel.onTagToggled(tag) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = uiState.comentario,
                onValueChange = viewModel::onComentarioChanged,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Comentarios de tu experiencia...") },
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.enviarEvaluacion() },
                modifier = Modifier.align(Alignment.End),
                colors = ButtonDefaults.buttonColors(containerColor = colorScheme.error),
                shape = RoundedCornerShape(24.dp),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Check, // Ícono genérico compatible
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("SUBIR CALIFICACIÓN", style = typography.bodyLarge.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp))
            }
        }
    }
}