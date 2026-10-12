package app.kaiju.inventory.model

enum class TipoMovimiento {
    ENTRADA,
    SALIDA_VENTA,
    SALIDA_CONSUMO,
    AJUSTE
}

data class Movimiento(
    val id: Int = 0,                               
    val skuProducto: String,
    val tipo: TipoMovimiento,
    val cantidad: Int,
    val motivo: String = "",                       
    val fecha: Long = System.currentTimeMillis(),
    val usuario: String                            
)