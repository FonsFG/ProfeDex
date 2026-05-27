package com.example.profedex.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.profedex.R

@Composable
fun ProfeDexHeader(
    title: String = "PROFEDEX",
    subtitle: String = "Facultad de Ingeniería UNAM",
    onBackClick: (() -> Unit)? = null
) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Surface(
        color = colorScheme.error,
        shadowElevation = 4.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            if (onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = colorScheme.background
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
            }

            Image(
                painter = painterResource(id = R.drawable.logo_profedex),
                contentDescription = "Logo ProfeDex",
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    color = colorScheme.background,
                    style = typography.titleLarge.copy(
                        fontSize = 22.sp, 
                        letterSpacing = 1.sp, 
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = subtitle,
                    color = colorScheme.background.copy(alpha = 0.8f),
                    style = typography.bodyLarge.copy(fontSize = 11.sp)
                )
            }
        }
    }
}
