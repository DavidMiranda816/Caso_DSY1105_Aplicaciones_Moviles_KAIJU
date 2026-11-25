package app.kaiju.inventory.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.kaiju.inventory.ui.components.CampoTexto
import app.kaiju.inventory.viewmodel.RegistroUiState
import app.kaiju.inventory.viewmodel.RegistroViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRegistro(
    viewModel: RegistroViewModel = viewModel(),
    onRegistroExitoso: () -> Unit,
    onVolverLogin: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    LaunchedEffect(uiState) {
        if (uiState is RegistroUiState.Success) {
            onRegistroExitoso()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro de Usuario") },
                navigationIcon = {
                    TextButton(onClick = onVolverLogin) {
                        Text("← Volver")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(700)) + slideInVertically(initialOffsetY = { 50 })
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PersonAdd,
                                contentDescription = "Registro",
                                modifier = Modifier.size(36.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Crear Cuenta",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Regístrate para conectar con el backend",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        CampoTexto(
                            valor = nombre,
                            alCambiarValor = { nombre = it },
                            etiqueta = "Nombre completo",
                            icono = Icons.Filled.Person
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        CampoTexto(
                            valor = correo,
                            alCambiarValor = { correo = it },
                            etiqueta = "Correo electrónico",
                            icono = Icons.Filled.Email
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        CampoTexto(
                            valor = password,
                            alCambiarValor = { password = it },
                            etiqueta = "Contraseña (min. 8 caract.)",
                            icono = Icons.Filled.Lock,
                            transformacionVisual = PasswordVisualTransformation()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        CampoTexto(
                            valor = confirmPassword,
                            alCambiarValor = { confirmPassword = it },
                            etiqueta = "Confirmar contraseña",
                            icono = Icons.Filled.Lock,
                            transformacionVisual = PasswordVisualTransformation()
                        )

                        if (uiState is RegistroUiState.Error) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = (uiState as RegistroUiState.Error).mensaje,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                viewModel.registrar(nombre, correo, password, confirmPassword)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(14.dp),
                            enabled = uiState !is RegistroUiState.Loading
                        ) {
                            if (uiState is RegistroUiState.Loading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text("Registrarse", style = MaterialTheme.typography.titleMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}
