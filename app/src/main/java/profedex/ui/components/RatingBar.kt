package profedex.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RatingBar(
    rating: Int,
    onRatingChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        for (i in 1..5) {
            val isSelected = i <= rating
            Icon(
                // Usamos el mismo ícono sólido para evitar librerías pesadas
                imageVector = Icons.Filled.Star,
                contentDescription = "Estrella $i",
                // La diferenciación visual recae 100% en el tint (Color)
                tint = if (isSelected) Color(0xFFFFC107) else Color(0xFFE0E0E0), // Amarillo vs Gris claro
                modifier = Modifier
                    .size(36.dp)
                    .padding(4.dp)
                    .clickable { onRatingChanged(i) }
            )
        }
    }
}