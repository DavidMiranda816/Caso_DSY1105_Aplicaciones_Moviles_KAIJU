package app.kaiju.inventory.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.kaiju.inventory.ui.components.TarjetaAlerta
import app.kaiju.inventory.viewmodel.KaijuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAlertas(viewModel: KaijuViewModel) {
    val productos by viewModel.productos.collectAsState()

    val productosEnAlerta = productos.filter { viewModel.estaEnAlerta(it) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Alertas de stock") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            if (productosEnAlerta.isEmpty()) {
                Text(
                    text = "No hay productos con stock bajo. Todo en orden.",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp)
                ) {
                    items(items = productosEnAlerta, key = { producto -> producto.sku }) { producto ->
                        TarjetaAlerta(producto = producto)
                    }
                }
            }
        }
    }
}