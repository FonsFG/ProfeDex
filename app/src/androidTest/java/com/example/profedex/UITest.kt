package com.example.profedex

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.profedex.ui.screens.LoginScreen
import com.example.profedex.ui.theme.theme.ProfeDexTheme
import org.junit.Rule
import org.junit.Test

class UITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testVerificarCampo_Correo_Existe() {
        composeTestRule.setContent {
            ProfeDexTheme {
                LoginScreen()
            }
        }


        composeTestRule.onNodeWithText("Usuario", ignoreCase = true)
            .assertExists("No se encontró la etiqueta de Usuario")
    }
}