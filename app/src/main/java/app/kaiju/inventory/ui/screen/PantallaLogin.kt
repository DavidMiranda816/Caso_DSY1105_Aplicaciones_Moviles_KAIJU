package app.kaiju.inventory.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import app.kaiju.inventory.ui.component.CampoTexto
import app.kaiju.inventory.viewmodel.LoginViewModel

@Composable
fun PantallaLogin(
    modeloVista: LoginViewModel,
    alIngresarExitosamente: () -> Unit
) {
    val loginEstado by modeloVista.loginEstado.collectAsState()
    val errorLogin by modeloVista.errorLogin.collectAsState()
    val estaCargando by modeloVista.estaCargando.collectAsState()

    LaunchedEffect(loginEstado.inicioCompletado) {
        if (loginEstado.inicioCompletado) {
            alIngresarExitosamente()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Control de Inventario",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            CampoTexto(
                valor = loginEstado.correo,
                alCambiarValor = { modeloVista.alCambiarCorreo(it) },
                etiqueta = "Correo electrónico"
            )

            Spacer(modifier = Modifier.height(16.dp))

            CampoTexto(
                valor = loginEstado.contrasena,
                alCambiarValor = { modeloVista.alCambiarContrasena(it) },
                etiqueta = "Contraseña",
                transformacionVisual = PasswordVisualTransformation()
            )

            if (errorLogin.mensajeError != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorLogin.mensajeError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { modeloVista.iniciarSesion() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !estaCargando
            ) {
                if (estaCargando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Iniciar Sesión")
                }
            }
        }
    }
}