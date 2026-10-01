package app.kaiju.inventory.model

data class Producto (
    val sku: String,
    val nombreProducto: String,
    val descripcion: String,
    val marca:String,
    val precio: Double,
    val categoria: String,
    val stock: Int,
    val estadoProducto: Boolean
)