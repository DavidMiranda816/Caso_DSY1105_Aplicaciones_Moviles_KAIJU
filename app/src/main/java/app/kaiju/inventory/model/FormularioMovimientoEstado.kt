package app.kaiju.inventory.model

data class FormularioMovimientoEstado(
    val codigoProducto: String = "",
    val tipo: String = TipoMovimiento.ENTRADA,
    val cantidad: String = "",
    val motivo: String = "",
    val observacion: String = "",
    val ingresoManual: Boolean = false,
    val registrado: Boolean = false,
    val errorProducto: String? = null,
    val errorCantidad: String? = null,
    val errorMotivo: String? = null
)