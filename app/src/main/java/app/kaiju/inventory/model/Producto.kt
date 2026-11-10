package app.kaiju.inventory.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class Producto(
    @PrimaryKey val sku: String,
    val nombreProducto: String,
    val descripcion: String,
    val marca: String,
    val precio: Double,
    val categoria: String,
    val stock: Int,
    val estadoProducto: Boolean = true,
    val stockMinimo: Int = 5
)