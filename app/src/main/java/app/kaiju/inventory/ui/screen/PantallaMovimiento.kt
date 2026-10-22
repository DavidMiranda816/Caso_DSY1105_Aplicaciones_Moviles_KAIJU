package app.kaiju.inventory.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import app.kaiju.inventory.model.TipoMovimiento
import app.kaiju.inventory.viewmodel.MovimientoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMovimiento(
    viewModel: MovimientoViewModel,
    usuarioActivo: String,
    tiposPermitidos: List<String>,
    onEscanear: () -> Unit,
    onVolver: () -> Unit
) {
    val estado by viewModel.estado.collectAsState()
    val productos by viewModel.productos.collectAsState()
    val snackbar = remember { SnackbarHostState() }
    val haptic = LocalHapticFeedback.current
    var mostrarMenu by remember { mutableStateOf(false) }
    var skuManual by rememberSaveable { mutableStateOf("") }

    val producto = productos.find { it.sku == estado.codigoProducto }
    val cantidadInt = estado.cantidad.toIntOrNull() ?: 0
    val stockResultante = producto?.let {
        when (estado.tipo) {
            TipoMovimiento.ENTRADA -> it.stock + cantidadInt
            TipoMovimiento.AJUSTE -> cantidadInt
            else -> it.stock - cantidadInt
        }
    }

    LaunchedEffect(tiposPermitidos) {
        viewModel.ajustarTipoPermitido(tiposPermitidos)
    }

    LaunchedEffect(estado.registrado) {
        if (estado.registrado) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            snackbar.showSnackbar("✓ Movimiento registrado correctamente")
            viewModel.mensajeMostrado()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        topBar = {
            TopAppBar(
                title = { Text("Nuevo movimiento") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = producto?.sku?.take(2) ?: "?",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.size(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(producto?.sku ?: "Sin seleccionar", style = MaterialTheme.typography.bodySmall)
                        Text(producto?.nombreProducto ?: "Elige o escanea", fontWeight = FontWeight.Bold)
                        Text(
                            producto?.let { "Stock actual: ${it.stock} u." } ?: "—",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    IconButton(onClick = onEscanear) {
                        Icon(Icons.Filled.QrCodeScanner, contentDescription = "Escanear QR")
                    }
                    Button(onClick = { mostrarMenu = true }) { Text("Elegir") }
                    DropdownMenu(expanded = mostrarMenu, onDismissRequest = { mostrarMenu = false }) {
                        productos.forEach { p ->
                            DropdownMenuItem(
                                text = { Text("${p.nombreProducto} (${p.stock} u.)") },
                                onClick = {
                                    viewModel.seleccionarProducto(p.sku)
                                    mostrarMenu = false
                                }
                            )
                        }
                    }
                }
            }
            estado.errorProducto?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            if (estado.ingresoManual) {
                Spacer(Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = skuManual,
                        onValueChange = { skuManual = it },
                        label = { Text("SKU del producto") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.size(8.dp))
                    Button(onClick = { viewModel.codigoLeido(skuManual) }) { Text("Buscar") }
                }
            }

            Spacer(Modifier.height(16.dp))

            Text("Tipo de movimiento", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tiposPermitidos.forEach { t ->
                    FilterChip(
                        selected = estado.tipo == t,
                        onClick = { viewModel.actualizarTipo(t) },
                        label = { Text(TipoMovimiento.etiqueta(t)) }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                if (estado.tipo == TipoMovimiento.AJUSTE) "Nuevo stock *" else "Cantidad *",
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    viewModel.actualizarCantidad((cantidadInt - 1).coerceAtLeast(0).toString())
                }) { Icon(Icons.Default.Remove, contentDescription = "Restar") }
                OutlinedTextField(
                    value = estado.cantidad,
                    onValueChange = viewModel::actualizarCantidad,
                    placeholder = { Text("0") },
                    isError = estado.errorCantidad != null,
                    supportingText = { estado.errorCantidad?.let { Text(it) } },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { viewModel.actualizarCantidad((cantidadInt + 1).toString()) }) {
                    Icon(Icons.Default.Add, contentDescription = "Sumar")
                }
            }

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = estado.motivo,
                onValueChange = viewModel::actualizarMotivo,
                label = { Text(if (estado.tipo == TipoMovimiento.AJUSTE) "Motivo *" else "Motivo") },
                placeholder = { Text("Recepción de proveedor") },
                isError = estado.errorMotivo != null,
                supportingText = { estado.errorMotivo?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = estado.observacion,
                onValueChange = viewModel::actualizarObservacion,
                label = { Text("Observación") },
                placeholder = { Text("Agregar detalle…") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            if (producto != null && stockResultante != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Stock resultante", fontWeight = FontWeight.Bold)
                        Text(
                            "${producto.stock} → $stockResultante",
                            fontWeight = FontWeight.Bold,
                            color = if (stockResultante < 0) MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            Button(
                onClick = { viewModel.confirmarMovimiento(usuarioActivo, tiposPermitidos) },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(Modifier.size(8.dp))
                Text("Confirmar movimiento")
            }

            Spacer(Modifier.height(8.dp))
            Text(
                "Usuario activo: $usuarioActivo. El movimiento no se podrá editar ni borrar.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}