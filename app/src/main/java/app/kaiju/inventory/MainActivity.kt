package app.kaiju.inventory

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import app.kaiju.inventory.navegation.AppNavHost
import app.kaiju.inventory.ui.theme.KaijuTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        crearCanalNotificaciones()

        enableEdgeToEdge()
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
        }

        setContent {
            KaijuTheme {
                AppNavHost()
            }
        }
    }

    private fun crearCanalNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nombreCanal = "Alertas de Inventario"
            val descripcionCanal = "Avisos cuando un producto está por agotarse"
            val importancia = NotificationManager.IMPORTANCE_HIGH

            val canal = NotificationChannel("canal_stock_bajo", nombreCanal, importancia).apply {
                description = descripcionCanal
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(canal)
        }
    }
}
