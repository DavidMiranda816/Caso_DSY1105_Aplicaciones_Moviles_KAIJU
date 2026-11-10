package app.kaiju.inventory.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


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
