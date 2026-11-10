package app.kaiju.inventory.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ColoresKaiju = lightColorScheme(
    primary = KaijuMorado,
    onPrimary = Color.White,
    primaryContainer = KaijuMoradoClaro,
    onPrimaryContainer = KaijuMorado,
    secondary = KaijuAmarillo,
    onSecondary = Color.Black,
    tertiary = KaijuNaranjo,
    tertiaryContainer = KaijuNaranjoClaro,
    onTertiaryContainer = KaijuNaranjo,
    error = KaijuRojo,
    background = KaijuFondo
)

@Composable
fun KaijuTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ColoresKaiju,
        content = content
    )
}
