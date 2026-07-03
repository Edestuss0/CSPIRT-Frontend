package com.cpirt.app.ui.theme

import androidx.compose.ui.graphics.Color

/* =========================================================================
 * СВЕТЛАЯ ТЕМА — светлые поверхности, тёмный текст, синий акцент
 * ========================================================================= */

// Primary (синий акцент)
val LightPrimary = Color(0xFF1D4ED8)
val LightOnPrimary = Color(0xFFFFFFFF)
val LightPrimaryContainer = Color(0xFFDCE7FF)
val LightOnPrimaryContainer = Color(0xFF0A2A6B)
val LightInversePrimary = Color(0xFFAEC6FF)

// Secondary (приглушённый сине-серый, поддерживает акцент)
val LightSecondary = Color(0xFF3B5A8A)
val LightOnSecondary = Color(0xFFFFFFFF)
val LightSecondaryContainer = Color(0xFFDCE7FF)
val LightOnSecondaryContainer = Color(0xFF16233A)

// Tertiary (тёплый нейтральный акцент для разнообразия — фиолетово-серый)
val LightTertiary = Color(0xFF5B5D72)
val LightOnTertiary = Color(0xFFFFFFFF)
val LightTertiaryContainer = Color(0xFFE1E0F9)
val LightOnTertiaryContainer = Color(0xFF181A2C)

// Error
val LightError = Color(0xFFBA1A1A)
val LightOnError = Color(0xFFFFFFFF)
val LightErrorContainer = Color(0xFFFFDAD6)
val LightOnErrorContainer = Color(0xFF410002)

// Background / Surface — тёмные полупрозрачные поверхности под AppScaffold
val LightBackground = Color(0xFF101419)
val LightOnBackground = Color(0xFFF4F7FB)

val LightSurface = Color(0xFF151A21)
val LightOnSurface = Color(0xFFF4F7FB)
val LightSurfaceVariant = Color(0xFF2B3441)
val LightOnSurfaceVariant = Color(0xFFD3DAE6)

val LightSurfaceDim = Color(0xFF101419)
val LightSurfaceBright = Color(0xFF303946)

val LightSurfaceContainerLowest = Color(0xCC0D1117)
val LightSurfaceContainerLow = Color(0xCC141A22)
val LightSurfaceContainer = Color(0xCC19212B)
val LightSurfaceContainerHigh = Color(0xDD202A36)
val LightSurfaceContainerHighest = Color(0xEE293342)

val LightOutline = Color(0xFF8B97A8)
val LightOutlineVariant = Color(0xFF3A4656)

val LightScrim = Color(0xFF000000)
val LightInverseSurface = Color(0xFFF4F7FB)
val LightInverseOnSurface = Color(0xFF1A1F27)

val LightSurfaceTint = LightPrimary


/* =========================================================================
 * ТЁМНАЯ ТЕМА — тёмные поверхности, светлый текст, синий акцент
 * ========================================================================= */

// Primary
val DarkPrimary = Color(0xFFAEC6FF)
val DarkOnPrimary = Color(0xFF002C71)
val DarkPrimaryContainer = Color(0xFF00419E)
val DarkOnPrimaryContainer = Color(0xFFDCE7FF)
val DarkInversePrimary = Color(0xFF1D4ED8)

// Secondary
val DarkSecondary = Color(0xFFB7C6EC)
val DarkOnSecondary = Color(0xFF23324D)
val DarkSecondaryContainer = Color(0xFF3A4964)
val DarkOnSecondaryContainer = Color(0xFFD4E1FF)

// Tertiary
val DarkTertiary = Color(0xFFC4C3DD)
val DarkOnTertiary = Color(0xFF2C2E42)
val DarkTertiaryContainer = Color(0xFF434459)
val DarkOnTertiaryContainer = Color(0xFFE1E0F9)

// Error
val DarkError = Color(0xFFFFB4AB)
val DarkOnError = Color(0xFF690005)
val DarkErrorContainer = Color(0xFF93000A)
val DarkOnErrorContainer = Color(0xFFFFDAD6)

// Background / Surface — тёмные, нейтрально-графитовые (не чисто чёрные)
val DarkBackground = Color(0xFF121316)
val DarkOnBackground = Color(0xFFE3E2E6)

val DarkSurface = Color(0xFF121316)
val DarkOnSurface = Color(0xFFE3E2E6)
val DarkSurfaceVariant = Color(0xFF44474E)
val DarkOnSurfaceVariant = Color(0xFFC4C6D0)

val DarkSurfaceDim = Color(0xFF121316)
val DarkSurfaceBright = Color(0xFF38393C)

val DarkSurfaceContainerLowest = Color(0xFF0C0E11)
val DarkSurfaceContainerLow = Color(0xFF1A1C1E)
val DarkSurfaceContainer = Color(0xFF1E2022)
val DarkSurfaceContainerHigh = Color(0xFF282A2D)
val DarkSurfaceContainerHighest = Color(0xFF333538)

val DarkOutline = Color(0xFF8E9099)
val DarkOutlineVariant = Color(0xFF44474E)

val DarkScrim = Color(0xFF000000)
val DarkInverseSurface = Color(0xFFE3E2E6)
val DarkInverseOnSurface = Color(0xFF2F3033)

val DarkSurfaceTint = DarkPrimary


/* =========================================================================
 * Кастомные семантические цвета (вне ColorScheme, свои токены проекта)
 * Каждый со своей парой light/dark — используются напрямую по теме.
 * ========================================================================= */

val LightSuccess = Color(0xFF1B8E4E)
val LightOnSuccess = Color(0xFFFFFFFF)
val LightSuccessContainer = Color(0xFFC3F2CF)
val LightOnSuccessContainer = Color(0xFF002110)

val DarkSuccess = Color(0xFF8BD5A0)
val DarkOnSuccess = Color(0xFF00391A)
val DarkSuccessContainer = Color(0xFF00522A)
val DarkOnSuccessContainer = Color(0xFFA6F2B7)

val LightWarning = Color(0xFF8A5300)
val LightOnWarning = Color(0xFFFFFFFF)
val LightWarningContainer = Color(0xFFFFDDB3)
val LightOnWarningContainer = Color(0xFF2B1700)

val DarkWarning = Color(0xFFFFB95C)
val DarkOnWarning = Color(0xFF482900)
val DarkWarningContainer = Color(0xFF673D00)
val DarkOnWarningContainer = Color(0xFFFFDDB3)

val LightInfo = Color(0xFF2563EB)
val LightOnInfo = Color(0xFFFFFFFF)

val DarkInfo = Color(0xFF9DC2FF)
val DarkOnInfo = Color(0xFF00305F)
