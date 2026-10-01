package app.kaiju.inventory.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.kaiju.inventory.ui.component.TarjetaProducto
import app.kaiju.inventory.viewmodel.KaijuViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaListaProducto(
    viewModel: KaijuViewModel,
    onIrAAgregar: () -> Unit
) {
    val productos by viewModel.productos.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Inventario de Productos") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onIrAAgregar) {
                Text("+", style = MaterialTheme.typography.titleLarge)
            }
        }
    ) { padding ->
        if (productos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay productos registrados todavía.\nToca + para agregar el primero.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = productos,
                    key = { producto -> producto.sku } // Clave única para optimizar el rendimiento del LazyColumn
                ) { producto ->
                    TarjetaProducto(producto = producto)
                }
            }
        }
    }
}