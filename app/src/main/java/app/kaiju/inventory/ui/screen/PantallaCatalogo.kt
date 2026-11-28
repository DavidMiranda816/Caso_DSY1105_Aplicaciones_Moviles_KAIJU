package app.kaiju.inventory.ui.screen

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
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
    onIrAAgregar: () -> Unit,
    onIrAEditar: (String) -> Unit,
    onIrADetalle: (String) -> Unit,
    onCerrarSesion: () -> Unit
) {
    val productos by viewModel.productos.collectAsState()
    val categorias by viewModel.categorias.collectAsState()
    var categoriaSeleccionada by rememberSaveable { mutableStateOf<String?>(null) }
    var productoAEliminar by remember { mutableStateOf<Producto?>(null) }
    var textoBusqueda by rememberSaveable { mutableStateOf("") }

    val productosFiltrados = remember(productos, textoBusqueda, categoriaSeleccionada) {
        productos.filter { p ->
            (categoriaSeleccionada == null || p.categoria == categoriaSeleccionada) &&
            (textoBusqueda.isBlank() ||
                p.nombreProducto.contains(textoBusqueda, ignoreCase = true) ||
                p.sku.contains(textoBusqueda, ignoreCase = true) ||
                p.marca.contains(textoBusqueda, ignoreCase = true))
        }
    }

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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { textoBusqueda = it },
                placeholder = { Text("Buscar por nombre, SKU, marca...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                trailingIcon = {
                    if (textoBusqueda.isNotEmpty()) {
                        IconButton(onClick = { textoBusqueda = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Limpiar búsqueda")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = categoriaSeleccionada == null,
                    onClick = { categoriaSeleccionada = null },
                    label = { Text("Todas") }
                )
                categorias.forEach { categoria ->
                    FilterChip(
                        selected = categoriaSeleccionada == categoria,
                        onClick = { categoriaSeleccionada = categoria },
                        label = { Text(categoria) }
                    )
                }
            }

            if (productosFiltrados.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (productos.isEmpty()) {
                            "No hay productos registrados todavía.\nToca + para agregar el primero."
                        } else {
                            "No se encontraron productos con ese filtro."
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items = productosFiltrados, key = { producto -> producto.sku }) { producto ->
                        TarjetaProducto(
                            producto = producto,
                            enAlerta = viewModel.estaEnAlerta(producto),
                            mostrarAcciones = puedeGestionar,
                            onEditar = { onIrAEditar(producto.sku) },
                            onEliminar = { productoAEliminar = producto },
                            onClick = { onIrADetalle(producto.sku) }
                        )
                    }
                }
            }
        }
    }
}