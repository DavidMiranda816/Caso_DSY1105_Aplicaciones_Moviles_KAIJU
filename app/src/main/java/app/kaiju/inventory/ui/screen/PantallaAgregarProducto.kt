package app.kaiju.inventory.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import app.kaiju.inventory.viewmodel.KaijuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAgregarProducto(viewModel: KaijuViewModel, onProductoAgregado: () -> Unit) {
    val estado by viewModel.estadoFormulario.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Agregar Producto") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = estado.sku,
                onValueChange = viewModel::actualizarSku,
                label = { Text("SKU") },
                isError = estado.errores.errorSku != null,
                supportingText = { estado.errores.errorSku?.let { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.nombreProducto,
                onValueChange = viewModel::actualizarNombre,
                label = { Text("Nombre del Producto") },
                isError = estado.errores.errorNombre != null,
                supportingText = { estado.errores.errorNombre?.let { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.descripcion,
                onValueChange = viewModel::actualizarDescripcion,
                label = { Text("Descripción (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.marca,
                onValueChange = viewModel::actualizarMarca,
                label = { Text("Marca (opcional)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.precio,
                onValueChange = viewModel::actualizarPrecio,
                label = { Text("Precio ($)") },
                isError = estado.errores.errorPrecio != null,
                supportingText = { estado.errores.errorPrecio?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.categoria,
                onValueChange = viewModel::actualizarCategoria,
                label = { Text("Categoría (opcional)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.stock,
                onValueChange = viewModel::actualizarStock,
                label = { Text("Stock inicial") },
                isError = estado.errores.errorStock != null,
                supportingText = { estado.errores.errorStock?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    viewModel.guardarProducto(onSuccess = onProductoAgregado)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Producto")
            }
        }
    }
}