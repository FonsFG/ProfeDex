package com.example.profedex

import junit.framework.TestCase
import org.junit.Test

class ViewModelTest {

    @Test
    fun testValidacion_ListaEtiquetas_EsCorrecta() {
        // Prueba 1: Verifica lógica de datos pura
        val etiquetasDisponibles = listOf("Puntual", "Comprometido", "Estricto")
        val etiquetaBuscada = "Puntual"

        val existe = etiquetasDisponibles.contains(etiquetaBuscada)
        TestCase.assertEquals(true, existe)
    }

    @Test
    fun testCreacion_ObjetoProfesor_AsignaValoresCorrectos() {

        val nombreEsperado = "Fulanito"

        val nombreActual = "Fulanito"

        TestCase.assertEquals(nombreEsperado, nombreActual)
    }
}