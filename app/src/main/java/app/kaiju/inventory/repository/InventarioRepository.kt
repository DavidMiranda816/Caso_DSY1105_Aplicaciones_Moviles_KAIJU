package app.kaiju.inventory.repository

import app.kaiju.inventory.model.Movimiento
import app.kaiju.inventory.model.Producto

class InventarioRepository {

    private val productos = DatosDePrueba.productos.toMutableList()
    private val movimientos = mutableListOf<Movimiento>()
    private var siguienteIdMovimiento = 1

    fun obtenerProductos(): List<Producto> = productos.toList()

    fun buscarProducto(sku: String): Producto? =
        productos.find { it.sku == sku }

    fun agregarProducto(producto: Producto) {
        productos.add(producto)
    }

    fun actualizarProducto(producto: Producto) {
        val posicion = productos.indexOfFirst { it.sku == producto.sku }
        if (posicion != -1) productos[posicion] = producto
    }

    fun agregarMovimiento(movimiento: Movimiento) {
        movimientos.add(movimiento.copy(id = siguienteIdMovimiento))
        siguienteIdMovimiento = siguienteIdMovimiento + 1
    }

    fun obtenerMovimientos(sku: String): List<Movimiento> =
        movimientos.filter { it.skuProducto == sku }
}