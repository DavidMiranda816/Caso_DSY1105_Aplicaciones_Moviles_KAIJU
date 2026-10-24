package app.kaiju.inventory.ui.components

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import app.kaiju.inventory.model.Producto

fun mostrarNotificacionStockBajo(context: Context, producto: Producto) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return
        }
    }

    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val builder = NotificationCompat.Builder(context, "canal_stock_bajo")
        .setSmallIcon(android.R.drawable.ic_dialog_alert)
        .setContentTitle("¡Alerta de Stock Bajo!")
        .setContentText("${producto.nombreProducto} se está agotando. Quedan ${producto.stock} unidades.")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)

    notificationManager.notify(producto.nombreProducto.hashCode(), builder.build())
}