package app.kaiju.inventory.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import app.kaiju.inventory.data.model.UsuarioResponse
import app.kaiju.inventory.model.rolDesdeBackend
import app.kaiju.inventory.ui.components.CampoTexto
import app.kaiju.inventory.viewmodel.UsuariosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaUsuarios(
    viewModel: UsuariosViewModel,
    token: String,
    correoActual: String
) {
    val estado by viewModel.uiState.collectAsState()
    var creando by remember { mutableStateOf(false) }
    var usuarioAEditar by remember { mutableStateOf<UsuarioResponse?>(null) }
    var usuarioAEliminar by remember { mutableStateOf<UsuarioResponse?>(null) }

    LaunchedEffect(Unit) {
        viewModel.cargar(token)
    }

    if (creando || usuarioAEditar != null) {
        DialogoUsuario(
            usuario = usuarioAEditar,
            onGuardar = { nombre, correo, password ->
                val usuario = usuarioAEditar
                if (usuario == null) viewModel.crear(token, nombre, correo, password)
                else viewModel.editar(token, usuario.id, nombre, correo)
                creando = false
                usuarioAEditar = null
            },
            onCancelar = {
                creando = false
                usuarioAEditar = null
            }
        )
    }

    usuarioAEliminar?.let { usuario ->
        AlertDialog(
            onDismissRequest = { usuarioAEliminar = null },
            title = { Text("Eliminar usuario") },
            text = { Text("La cuenta de ${usuario.nombre} quedará desactivada y no podrá iniciar sesión.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.eliminar(token, usuario.id)
                    usuarioAEliminar = null
                }) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { usuarioAEliminar = null }) { Text("Cancelar") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Usuarios") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { creando = true }) {
                Text("+", style = MaterialTheme.typography.titleLarge)
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            estado.error?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp))
            }
            if (estado.cargando) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp)
            ) {
                items(items = estado.usuarios, key = { usuario -> usuario.id }) { usuario ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(usuario.nombre, style = MaterialTheme.typography.titleMedium)
                            Text(usuario.correo, style = MaterialTheme.typography.bodyMedium)
                            Text(
                                text = "${rolDesdeBackend(usuario.rol).etiqueta} · ${if (usuario.activo) "Activo" else "Inactivo"}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Row {
                                TextButton(onClick = { usuarioAEditar = usuario }) { Text("Editar") }
                                if (usuario.activo && usuario.correo != correoActual) {
                                    TextButton(onClick = { usuarioAEliminar = usuario }) { Text("Eliminar") }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DialogoUsuario(
    usuario: UsuarioResponse?,
    onGuardar: (String, String, String) -> Unit,
    onCancelar: () -> Unit
) {
    var nombre by remember { mutableStateOf(usuario?.nombre ?: "") }
    var correo by remember { mutableStateOf(usuario?.correo ?: "") }
    var password by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text(if (usuario == null) "Nuevo vendedor" else "Editar usuario") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CampoTexto(valor = nombre, alCambiarValor = { nombre = it }, etiqueta = "Nombre", icono = Icons.Filled.Person)
                CampoTexto(valor = correo, alCambiarValor = { correo = it }, etiqueta = "Correo electrónico", icono = Icons.Filled.Email)
                if (usuario == null) {
                    CampoTexto(
                        valor = password,
                        alCambiarValor = { password = it },
                        etiqueta = "Contraseña",
                        icono = Icons.Filled.Lock,
                        transformacionVisual = PasswordVisualTransformation()
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onGuardar(nombre, correo, password) }) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) { Text("Cancelar") }
        }
    )
}
