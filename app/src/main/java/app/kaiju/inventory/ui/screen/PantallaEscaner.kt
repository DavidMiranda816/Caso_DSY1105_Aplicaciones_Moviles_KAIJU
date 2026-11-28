package app.kaiju.inventory.ui.screen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.annotation.OptIn
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import app.kaiju.inventory.ui.components.BotonConPermisoCamara
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

@Composable
fun PantallaEscaner(
    onCodigoLeido: (String) -> Unit,
    onIngresarManual: () -> Unit
) {
    val context = LocalContext.current
    var escaneando by rememberSaveable { mutableStateOf(false) }
    var permisoDenegado by remember { mutableStateOf(false) }

    LifecycleResumeEffect(Unit) {
        val concedido = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED
        if (concedido) permisoDenegado = false else escaneando = false
        onPauseOrDispose { }
    }

    if (escaneando) {
        VistaCamaraQr(onCodigoLeido)
    } else {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Escanear producto", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(8.dp))
            Text(
                "Lee el QR de un producto para registrar una entrada o salida de stock.",
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(24.dp))

            BotonConPermisoCamara(
                onPermisoConcedido = { escaneando = true },
                onSinPermiso = { permisoDenegado = true }
            )

            if (permisoDenegado) {
                Spacer(Modifier.height(16.dp))
                Text(
                    "Sin acceso a la cámara. Puedes ingresar el SKU a mano.",
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(8.dp))
                OutlinedButton(onClick = onIngresarManual) { Text("Ingresar SKU a mano") }
                TextButton(onClick = {
                    context.startActivity(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", context.packageName, null)
                        )
                    )
                }) { Text("Abrir Ajustes") }
            }
        }
    }
}

@OptIn(ExperimentalGetImage::class)
@Composable
private fun VistaCamaraQr(onCodigoLeido: (String) -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val callback by rememberUpdatedState(onCodigoLeido)

    val executor = remember { Executors.newSingleThreadExecutor() }
    val yaLeido = remember { AtomicBoolean(false) }
    val escaner = remember {
        BarcodeScanning.getClient(
            BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build()
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            escaner.close()
            executor.shutdown()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                val futuro = ProcessCameraProvider.getInstance(ctx)
                futuro.addListener({
                    val proveedor = futuro.get()

                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
                    val analisis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()

                    analisis.setAnalyzer(executor) { proxy ->
                        val media = proxy.image
                        if (media == null || yaLeido.get()) {
                            proxy.close()
                            return@setAnalyzer
                        }
                        val imagen = InputImage.fromMediaImage(media, proxy.imageInfo.rotationDegrees)
                        escaner.process(imagen)
                            .addOnSuccessListener { codigos ->
                                val valor = codigos.firstOrNull()?.rawValue?.trim()
                                if (!valor.isNullOrBlank() && yaLeido.compareAndSet(false, true)) {
                                    callback(valor)
                                }
                            }
                            .addOnCompleteListener { proxy.close() }
                    }

                    proveedor.unbindAll()
                    proveedor.bindToLifecycle(
                        lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, preview, analisis
                    )
                }, ContextCompat.getMainExecutor(ctx))
                previewView
            }
        )

        Card(modifier = Modifier.align(Alignment.BottomCenter).padding(24.dp)) {
            Text(
                "Apunta al QR del producto para registrar un movimiento",
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}