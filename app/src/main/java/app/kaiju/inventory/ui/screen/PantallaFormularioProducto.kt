package app.kaiju.inventory.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import app.kaiju.inventory.viewmodel.KaijuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFormularioProducto(
    viewModel: KaijuViewModel,
    skuEditar: String? = null,
    onTerminado: () -> Unit
) {
    val estado by viewModel.estadoFormulario.collectAsState()
    val categorias by viewModel.categorias.collectAsState()
    var mostrarCategorias by remember { mutableStateOf(false) }

    LaunchedEffect(skuEditar) {
        if (skuEditar != null) viewModel.cargarParaEditar(skuEditar) else viewModel.limpiarFormulario()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (estado.esEdicion) "Editar Producto" else "Agregar Producto") },
                navigationIcon = {
                    IconButton(onClick = onTerminado) {
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
                leadingIcon = { Icon(Icons.Filled.QrCode, contentDescription = null) },
                enabled = !estado.esEdicion,
                isError = estado.errores.errorSku != null,
                supportingText = { estado.errores.errorSku?.let { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.nombreProducto,
                onValueChange = viewModel::actualizarNombre,
                label = { Text("Nombre del Producto") },
                leadingIcon = { Icon(Icons.Filled.Edit, contentDescription = null) },
                isError = estado.errores.errorNombre != null,
                supportingText = { estado.errores.errorNombre?.let { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.descripcion,
                onValueChange = viewModel::actualizarDescripcion,
                label = { Text("Descripción (opcional)") },
                leadingIcon = { Icon(Icons.Filled.Description, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.marca,
                onValueChange = viewModel::actualizarMarca,
                label = { Text("Marca (opcional)") },
                leadingIcon = { Icon(Icons.Filled.Sell, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.precio,
                onValueChange = viewModel::actualizarPrecio,
                label = { Text("Precio ($)") },
                leadingIcon = { Icon(Icons.Filled.AttachMoney, contentDescription = null) },
                isError = estado.errores.errorPrecio != null,
                supportingText = { estado.errores.errorPrecio?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Box {
                OutlinedTextField(
                    value = estado.categoria,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría") },
                    leadingIcon = { Icon(Icons.Filled.Category, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = { mostrarCategorias = true }) {
                            Icon(Icons.Filled.ArrowDropDown, contentDescription = "Elegir categoría")
                        }
                    },
                    isError = estado.errores.errorCategoria != null,
                    supportingText = { estado.errores.errorCategoria?.let { Text(it) } },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                DropdownMenu(expanded = mostrarCategorias, onDismissRequest = { mostrarCategorias = false }) {
                    categorias.forEach { categoria ->
                        DropdownMenuItem(
                            text = { Text(categoria) },
                            onClick = {
                                viewModel.actualizarCategoria(categoria)
                                mostrarCategorias = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = estado.stock,
                onValueChange = viewModel::actualizarStock,
                label = { Text(if (estado.esEdicion) "Stock (se cambia con movimientos)" else "Stock inicial") },
                leadingIcon = { Icon(Icons.Filled.Inventory, contentDescription = null) },
                enabled = !estado.esEdicion,
                isError = estado.errores.errorStock != null,
                supportingText = { estado.errores.errorStock?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.stockMinimo,
                onValueChange = viewModel::actualizarStockMinimo,
                label = { Text("Stock mínimo") },
                leadingIcon = { Icon(Icons.Filled.Warning, contentDescription = null) },
                isError = estado.errores.errorStockMinimo != null,
                supportingText = { estado.errores.errorStockMinimo?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = { viewModel.guardarProducto(onSuccess = onTerminado) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = if (estado.esEdicion) "Guardar cambios" else "Agregar producto",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}