package app.kaiju.inventory.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.kaiju.inventory.model.Movimiento
import app.kaiju.inventory.model.TipoMovimiento
import app.kaiju.inventory.viewmodel.KaijuViewModel
import java.text.NumberFormat
import java.util.Locale

private val formatoPrecio = NumberFormat.getInstance(Locale("es", "CL"))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalleProducto(
    sku: String,
    viewModel: KaijuViewModel,
    puedeEditar: Boolean,
    puedeRegistrarMovimiento: Boolean,
    onVolver: () -> Unit,
    onIrAEditar: (String) -> Unit,
    onIrAMovimiento: (String) -> Unit
) {
    val productos by viewModel.productos.collectAsState()
    val producto = productos.find { it.sku.equals(sku, ignoreCase = true) }
    val movimientos by viewModel.movimientosDeProducto(sku).collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(producto?.nombreProducto ?: "Detalle de Producto") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    if (producto != null && puedeEditar) {
                        IconButton(onClick = { onIrAEditar(producto.sku) }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Editar producto")
                        }
                    }
                    if (producto != null && puedeRegistrarMovimiento) {
                        IconButton(onClick = { onIrAMovimiento(producto.sku) }) {
                            Icon(Icons.Filled.SwapHoriz, contentDescription = "Registrar movimiento")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        if (producto == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "El producto no fue encontrado o fue eliminado.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = producto.nombreProducto,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = "$${formatoPrecio.format(producto.precio.toInt())}",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            FilaDetalle("SKU", producto.sku)
                            FilaDetalle("Categoría", producto.categoria.ifBlank { "General" })
                            FilaDetalle("Marca", producto.marca.ifBlank { "N/A" })
                            FilaDetalle("Stock Actual", "${producto.stock} unidades")
                            FilaDetalle("Stock Mínimo", "${producto.stockMinimo} unidades")
                            FilaDetalle("Estado", if (producto.estadoProducto) "Activo" else "Inactivo")

                            if (producto.descripcion.isNotBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Descripción:",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = producto.descripcion,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Historial de Movimientos",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        if (puedeRegistrarMovimiento) {
                            Button(
                                onClick = { onIrAMovimiento(producto.sku) },
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Icon(Icons.Filled.SwapHoriz, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("Movimiento", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }

                if (movimientos.isEmpty()) {
                    item {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No hay movimientos registrados para este producto.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                } else {
                    items(items = movimientos, key = { it.id }) { movimiento ->
                        TarjetaMovimiento(movimiento = movimiento)
                    }
                }
            }
        }
    }
}

@Composable
private fun FilaDetalle(etiqueta: String, valor: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun TarjetaMovimiento(movimiento: Movimiento) {
    val esEntrada = movimiento.tipo == TipoMovimiento.ENTRADA
    val esAjuste = movimiento.tipo == TipoMovimiento.AJUSTE

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = when {
                        esEntrada -> MaterialTheme.colorScheme.primaryContainer
                        esAjuste -> MaterialTheme.colorScheme.tertiaryContainer
                        else -> MaterialTheme.colorScheme.errorContainer
                    }
                ) {
                    Text(
                        text = TipoMovimiento.etiqueta(movimiento.tipo),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = when {
                            esEntrada -> MaterialTheme.colorScheme.onPrimaryContainer
                            esAjuste -> MaterialTheme.colorScheme.onTertiaryContainer
                            else -> MaterialTheme.colorScheme.onErrorContainer
                        }
                    )
                }

                Text(
                    text = when {
                        esEntrada -> "+${movimiento.cantidad}"
                        esAjuste -> "Ajuste: ${movimiento.cantidad}"
                        else -> "-${movimiento.cantidad}"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = when {
                        esEntrada -> MaterialTheme.colorScheme.primary
                        esAjuste -> MaterialTheme.colorScheme.tertiary
                        else -> MaterialTheme.colorScheme.error
                    }
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Motivo: ${movimiento.motivo}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )

            if (movimiento.observacion.isNotBlank()) {
                Text(
                    text = "Obs: ${movimiento.observacion}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Usuario: ${movimiento.usuario}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = movimiento.fecha,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
