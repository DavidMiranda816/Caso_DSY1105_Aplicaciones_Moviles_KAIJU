package app.kaiju.inventory.ui.components

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@Composable
fun BotonConPermisoCamara(
    onPermisoConcedido: () -> Unit,
    onSinPermiso: () -> Unit
) {
    val context = LocalContext.current
    val activity = LocalActivity.current
    var mostrarExplicacion by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) onPermisoConcedido() else onSinPermiso()
    }

    Button(onClick = {
        val yaConcedido = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        when {
            yaConcedido -> onPermisoConcedido()
            activity != null && ActivityCompat.shouldShowRequestPermissionRationale(
                activity, Manifest.permission.CAMERA
            ) -> mostrarExplicacion = true
            else -> launcher.launch(Manifest.permission.CAMERA)
        }
    }) { Text("Escanear código") }

    if (mostrarExplicacion) {
        AlertDialog(
            onDismissRequest = { mostrarExplicacion = false },
            title = { Text("Usaremos la cámara") },
            text = { Text("La cámara sirve solo para leer el código QR de un producto. No se guardan fotos ni video.") },
            confirmButton = {
                TextButton(onClick = {
                    mostrarExplicacion = false
                    launcher.launch(Manifest.permission.CAMERA)
                }) { Text("Continuar") }
            },
            dismissButton = {
                TextButton(onClick = {
                    mostrarExplicacion = false
                    onSinPermiso()
                }) { Text("Ahora no") }
            }
        )
    }
}
