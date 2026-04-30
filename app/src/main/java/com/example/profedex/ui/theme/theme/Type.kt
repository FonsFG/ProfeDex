package com.example.profedex.ui.theme.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.profedex.R // Asegúrate de importar tu R

// 1. Define tu familia de fuentes
val MyCustomFontFamily = FontFamily(
    Font(R.font.audiowide_regular, FontWeight.Bold),
    Font(R.font.orbitron_variablefont_wght, FontWeight.Normal)
)

// 2. Configura los estilos de tipografía
val Typography = Typography(
    // Estilo para cuerpos de texto (Parrafos)
    bodyLarge = TextStyle(
        fontFamily = MyCustomFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    // Estilo para títulos (Títulos de secciones, encabezados)
    titleLarge = TextStyle(
        fontFamily = MyCustomFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    )
)