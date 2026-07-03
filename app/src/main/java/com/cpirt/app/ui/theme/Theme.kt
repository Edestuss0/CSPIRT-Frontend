package com.cpirt.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

/* =========================================================================
 * Material 3 ColorScheme — используем ВСЕ токены схемы, ничего не пропущено
 * ========================================================================= */

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    primaryContainer = LightPrimaryContainer,
    onPrimaryContainer = LightOnPrimaryContainer,
    inversePrimary = LightInversePrimary,

    secondary = LightSecondary,
    onSecondary = LightOnSecondary,
    secondaryContainer = LightSecondaryContainer,
    onSecondaryContainer = LightOnSecondaryContainer,

    tertiary = LightTertiary,
    onTertiary = LightOnTertiary,
    tertiaryContainer = LightTertiaryContainer,
    onTertiaryContainer = LightOnTertiaryContainer,

    error = LightError,
    onError = LightOnError,
    errorContainer = LightErrorContainer,
    onErrorContainer = LightOnErrorContainer,

    background = LightBackground,
    onBackground = LightOnBackground,

    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,

    surfaceDim = LightSurfaceDim,
    surfaceBright = LightSurfaceBright,
    surfaceContainerLowest = LightSurfaceContainerLowest,
    surfaceContainerLow = LightSurfaceContainerLow,
    surfaceContainer = LightSurfaceContainer,
    surfaceContainerHigh = LightSurfaceContainerHigh,
    surfaceContainerHighest = LightSurfaceContainerHighest,

    outline = LightOutline,
    outlineVariant = LightOutlineVariant,

    scrim = LightScrim,
    inverseSurface = LightInverseSurface,
    inverseOnSurface = LightInverseOnSurface,

    surfaceTint = LightSurfaceTint,
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnPrimaryContainer,
    inversePrimary = DarkInversePrimary,

    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,
    secondaryContainer = DarkSecondaryContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,

    tertiary = DarkTertiary,
    onTertiary = DarkOnTertiary,
    tertiaryContainer = DarkTertiaryContainer,
    onTertiaryContainer = DarkOnTertiaryContainer,

    error = DarkError,
    onError = DarkOnError,
    errorContainer = DarkErrorContainer,
    onErrorContainer = DarkOnErrorContainer,

    background = DarkBackground,
    onBackground = DarkOnBackground,

    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,

    surfaceDim = DarkSurfaceDim,
    surfaceBright = DarkSurfaceBright,
    surfaceContainerLowest = DarkSurfaceContainerLowest,
    surfaceContainerLow = DarkSurfaceContainerLow,
    surfaceContainer = DarkSurfaceContainer,
    surfaceContainerHigh = DarkSurfaceContainerHigh,
    surfaceContainerHighest = DarkSurfaceContainerHighest,

    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant,

    scrim = DarkScrim,
    inverseSurface = DarkInverseSurface,
    inverseOnSurface = DarkInverseOnSurface,

    surfaceTint = DarkSurfaceTint,
)

/* =========================================================================
 * Кастомные цвета вне ColorScheme (success/warning/info), доступные
 * через CompositionLocal — так же удобно, как MaterialTheme.colorScheme
 * ========================================================================= */

data class CpirtExtendedColors(
    val success: androidx.compose.ui.graphics.Color,
    val onSuccess: androidx.compose.ui.graphics.Color,
    val successContainer: androidx.compose.ui.graphics.Color,
    val onSuccessContainer: androidx.compose.ui.graphics.Color,

    val warning: androidx.compose.ui.graphics.Color,
    val onWarning: androidx.compose.ui.graphics.Color,
    val warningContainer: androidx.compose.ui.graphics.Color,
    val onWarningContainer: androidx.compose.ui.graphics.Color,

    val info: androidx.compose.ui.graphics.Color,
    val onInfo: androidx.compose.ui.graphics.Color,
)

private val LightExtendedColors = CpirtExtendedColors(
    success = LightSuccess,
    onSuccess = LightOnSuccess,
    successContainer = LightSuccessContainer,
    onSuccessContainer = LightOnSuccessContainer,
    warning = LightWarning,
    onWarning = LightOnWarning,
    warningContainer = LightWarningContainer,
    onWarningContainer = LightOnWarningContainer,
    info = LightInfo,
    onInfo = LightOnInfo,
)

private val DarkExtendedColors = CpirtExtendedColors(
    success = DarkSuccess,
    onSuccess = DarkOnSuccess,
    successContainer = DarkSuccessContainer,
    onSuccessContainer = DarkOnSuccessContainer,
    warning = DarkWarning,
    onWarning = DarkOnWarning,
    warningContainer = DarkWarningContainer,
    onWarningContainer = DarkOnWarningContainer,
    info = DarkInfo,
    onInfo = DarkOnInfo,
)

val LocalCpirtExtendedColors = staticCompositionLocalOf { LightExtendedColors }

/**
 * Доступ к кастомным цветам: CpirtTheme.extendedColors.success
 */
object CpirtTheme {
    val extendedColors: CpirtExtendedColors
        @Composable
        get() = LocalCpirtExtendedColors.current
}

val CpirtShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

@Composable
fun CpirtTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val extendedColors = if (darkTheme) DarkExtendedColors else LightExtendedColors

    androidx.compose.runtime.CompositionLocalProvider(
        LocalCpirtExtendedColors provides extendedColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = CpirtShapes,
            content = content
        )
    }
}