package app.kaiju.inventory.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun CampoTexto(
    valor: String,
    alCambiarValor: (String) -> Unit,
    etiqueta: String,
    modifier: Modifier = Modifier,
    transformacionVisual: VisualTransformation = VisualTransformation.None,
    opcionesTeclado: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = valor,
        onValueChange = alCambiarValor,
        label = { Text(etiqueta) },
        singleLine = true,
        visualTransformation = transformacionVisual,
        keyboardOptions = opcionesTeclado,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    )
}