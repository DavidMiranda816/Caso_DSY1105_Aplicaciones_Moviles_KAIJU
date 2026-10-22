package app.kaiju.inventory.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

object TipoMovimiento {
    const val ENTRADA = "ENTRADA"
    const val SALIDA_VENTA = "SALIDA_VENTA"
    const val SALIDA_CONSUMO = "SALIDA_CONSUMO"
    const val AJUSTE = "AJUSTE"

    val todos = listOf(ENTRADA, SALIDA_VENTA, SALIDA_CONSUMO, AJUSTE)

    fun etiqueta(tipo: String) = when (tipo) {
        ENTRADA -> "Entrada"
        SALIDA_VENTA -> "Venta"
        SALIDA_CONSUMO -> "Consumo"
        AJUSTE -> "Ajuste"
        else -> tipo
    }
}

@Entity(tableName = "movimientos", indices = [Index("codigoProducto")])
data class Movimiento(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val codigoProducto: String,
    val tipo: String,
    val cantidad: Int,
    val motivo: String,
    val observacion: String = "",
    val fecha: String,
    val usuario: String
)