package com.example.calendarapp.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val LightColors = lightColors(
    primary = androidx.compose.ui.graphics.Color(0xFF1E88E5),
    primaryVariant = androidx.compose.ui.graphics.Color(0xFF005CB2),
    secondary = androidx.compose.ui.graphics.Color(0xFFFFC107)
)

@Composable
fun CalendarTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = LightColors,
        typography = androidx.compose.material.Typography(),
        shapes = androidx.compose.material.Shapes(),
        content = content
    )
}
