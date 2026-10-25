package app.kaiju.inventory.model

sealed class TipoMovimiento(val valor: String, val etiqueta: String) {
    object Entrada : TipoMovimiento(ENTRADA, "Entrada")
    object SalidaVenta : TipoMovimiento(SALIDA_VENTA, "Salida por venta")
    object SalidaConsumo : TipoMovimiento(SALIDA_CONSUMO, "Salida por consumo")
    object Ajuste : TipoMovimiento(AJUSTE, "Ajuste")

    companion object {
        const val ENTRADA = "ENTRADA"
        const val SALIDA_VENTA = "SALIDA_VENTA"
        const val SALIDA_CONSUMO = "SALIDA_CONSUMO"
        const val AJUSTE = "AJUSTE"

        val todos = listOf(ENTRADA, SALIDA_VENTA, SALIDA_CONSUMO, AJUSTE)

        fun esSalida(tipo: String): Boolean = tipo == SALIDA_VENTA || tipo == SALIDA_CONSUMO

        fun etiqueta(tipo: String): String = when (tipo) {
            ENTRADA -> Entrada.etiqueta
            SALIDA_VENTA -> SalidaVenta.etiqueta
            SALIDA_CONSUMO -> SalidaConsumo.etiqueta
            AJUSTE -> Ajuste.etiqueta
            else -> tipo
        }
    }
}
