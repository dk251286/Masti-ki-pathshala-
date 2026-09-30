package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val MastiLightColorScheme = lightColorScheme(
    primary = MangoOrange,
    onPrimary = Color.White,
    primaryContainer = SunshineYellowLight,
    onPrimaryContainer = DeepInk,
    secondary = SkyBlueDeep,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE1F5FE),
    onSecondaryContainer = DeepInk,
    tertiary = BerryPurple,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFEDE7F6),
    onTertiaryContainer = DeepInk,
    background = WarmCreamBg,
    onBackground = DeepInk,
    surface = SoftCloudSurface,
    onSurface = DeepInk,
    surfaceVariant = Color(0xFFFFF3E0),
    onSurfaceVariant = MutedSlate
)

val MastiShapes = Shapes(
    small = RoundedCornerShape(14.dp),
    medium = RoundedCornerShape(22.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(36.dp)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    // Children's educational app uses a bright, cheerful, high-contrast light palette
    MaterialTheme(
        colorScheme = MastiLightColorScheme,
        typography = Typography,
        shapes = MastiShapes,
        content = content
    )
}
