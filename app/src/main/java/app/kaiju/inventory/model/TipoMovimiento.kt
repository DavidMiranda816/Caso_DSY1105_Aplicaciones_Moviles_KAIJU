package app.kaiju.inventory.model

sealed class TipoMovimiento(val valor: String, val etiqueta: String) {
    object Entrada : TipoMovimiento(ENTRADA, "Entrada")
    object Salida : TipoMovimiento(SALIDA, "Salida")
    object Ajuste : TipoMovimiento(AJUSTE, "Ajuste")

    companion object {
        const val ENTRADA = "ENTRADA"
        const val SALIDA = "SALIDA"
        const val AJUSTE = "AJUSTE"

        val todos = listOf(ENTRADA, SALIDA, AJUSTE)

        fun etiqueta(tipo: String): String = when (tipo) {
            ENTRADA -> Entrada.etiqueta
            SALIDA -> Salida.etiqueta
            AJUSTE -> Ajuste.etiqueta
            else -> tipo
        }
    }
}