package app.kaiju.inventory.navigation

import android.net.Uri

const val ARG_SKU = "sku"

sealed class Rutas(val ruta: String) {
    object Login : Rutas("login")
    object Inventario : Rutas("inventario")
    object Alertas : Rutas("alertas")
    object Escaner : Rutas("escaner")
    object Movimiento : Rutas("movimiento")

    object Agregar : Rutas("agregar_producto?$ARG_SKU={$ARG_SKU}") {
        fun conSku(sku: String? = null) =
            if (sku == null) "agregar_producto" else "agregar_producto?$ARG_SKU=${Uri.encode(sku)}"
    }
}