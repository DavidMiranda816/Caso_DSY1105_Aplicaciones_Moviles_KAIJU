package app.kaiju.inventory.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.kaiju.inventory.model.Producto
import app.kaiju.inventory.model.Usuario
import app.kaiju.inventory.ui.components.TarjetaProducto
import app.kaiju.inventory.viewmodel.KaijuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCatalogo(
    viewModel: KaijuViewModel,
    usuario: Usuario?,
    puedeGestionar: Boolean,
    puedeRegistrarMovimientos: Boolean,
    onIrAAgregar: () -> Unit,
    onIrAEditar: (String) -> Unit,
    onIrAMovimiento: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val productos by viewModel.productos.collectAsState()
    var productoAEliminar by remember { mutableStateOf<Producto?>(null) }

    productoAEliminar?.let { producto ->
        AlertDialog(
            onDismissRequest = { productoAEliminar = null },
            title = { Text("Eliminar producto") },
            text = { Text("¿Eliminar \"${producto.nombreProducto}\"? Su historial de movimientos se conserva.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.eliminarProducto(producto.sku)
                    productoAEliminar = null
                }) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { productoAEliminar = null }) { Text("Cancelar") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Inventario de Productos")
                        usuario?.let {
                            Text("${it.nombre} · ${it.rol.etiqueta}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                },
                actions = {
                    if (puedeRegistrarMovimientos) {
                        IconButton(onClick = onIrAMovimiento) {
                            Icon(Icons.Filled.SwapHoriz, contentDescription = "Registrar movimiento")
                        }
                    }
                    IconButton(onClick = onCerrarSesion) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar sesión")
                    }
                }
            )
        },
        floatingActionButton = {
            if (puedeGestionar) {
                FloatingActionButton(onClick = onIrAAgregar) {
                    Text("+", style = MaterialTheme.typography.titleLarge)
                }
            }
        }
    ) { padding ->
        if (productos.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay productos registrados todavía.\nToca + para agregar el primero.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = productos, key = { producto -> producto.sku }) { producto ->
                    TarjetaProducto(
                        producto = producto,
                        enAlerta = viewModel.estaEnAlerta(producto),
                        mostrarAcciones = puedeGestionar,
                        onEditar = { onIrAEditar(producto.sku) },
                        onEliminar = { productoAEliminar = producto }
                    )
                }
            }
        }
    }
}