package app.kaiju.inventory.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.kaiju.inventory.model.Producto
import java.text.NumberFormat
import java.util.Locale

private val formatoPrecio = NumberFormat.getInstance(Locale("es", "CL"))

@Composable
fun TarjetaProducto(
    producto: Producto,
    enAlerta: Boolean = false,
    mostrarAcciones: Boolean = true,
    onEditar: () -> Unit = {},
    onEliminar: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = producto.nombreProducto,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "$${formatoPrecio.format(producto.precio.toInt())}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Marca: ${producto.marca.ifBlank { "N/A" }}  ·  Cat: ${producto.categoria.ifBlank { "General" }}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "SKU: ${producto.sku}  ·  Stock: ${producto.stock}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (enAlerta) {
            }

            if (producto.descripcion.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = producto.descripcion,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (mostrarAcciones) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    IconButton(onClick = onEditar) { Icon(Icons.Filled.Edit, contentDescription = "Editar") }
                    IconButton(onClick = onEliminar) { Icon(Icons.Filled.Delete, contentDescription = "Eliminar") }
                }
            }
        }
    }
}
