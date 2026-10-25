package app.kaiju.inventory.repository

import app.kaiju.inventory.database.MovimientoDao
import app.kaiju.inventory.database.ProductoDao
import app.kaiju.inventory.model.Movimiento
import app.kaiju.inventory.model.Producto
import app.kaiju.inventory.model.TipoMovimiento
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class InventarioRepository(
    private val productoDao: ProductoDao,
    private val movimientoDao: MovimientoDao,
    private val transaccion: suspend (suspend () -> String?) -> String? = { bloque -> bloque() }
) {
    val productos: Flow<List<Producto>> = productoDao.obtenerActivos()
    val movimientos: Flow<List<Movimiento>> = movimientoDao.obtenerTodos()

    fun movimientosDe(sku: String): Flow<List<Movimiento>> = movimientoDao.obtenerPorProducto(sku)

    suspend fun sembrarSiVacio() {
        if (productoDao.contar() == 0) productoDao.guardarTodos(DatosDePrueba.productos)
    }

    suspend fun buscar(sku: String): Producto? = productoDao.buscar(sku)

    suspend fun crear(producto: Producto): String? {
        if (productoDao.buscar(producto.sku)?.estadoProducto == true) return "Ya existe un producto con ese SKU"
        productoDao.guardar(producto.copy(estadoProducto = true))
        return null
    }

    suspend fun actualizar(producto: Producto) = productoDao.guardar(producto)
    suspend fun eliminar(sku: String) = productoDao.darDeBaja(sku) // baja lógica

    suspend fun registrarMovimiento(
        sku: String, tipo: String, cantidad: Int,
        motivo: String, observacion: String, usuario: String
    ): String? = transaccion { aplicarMovimiento(sku, tipo, cantidad, motivo, observacion, usuario) }

    private suspend fun aplicarMovimiento(
        sku: String, tipo: String, cantidad: Int,
        motivo: String, observacion: String, usuario: String
    ): String? {

        if (tipo !in TipoMovimiento.todos) return "Tipo de movimiento no válido"

        if (cantidad < 0 || (cantidad == 0 && tipo != TipoMovimiento.AJUSTE)) {
            return "La cantidad debe ser mayor a 0"
        }
        if (tipo == TipoMovimiento.AJUSTE && motivo.isBlank()) {
            return "El ajuste requiere un motivo"
        }

        val producto = productoDao.buscar(sku)?.takeIf { it.estadoProducto }
            ?: return "Producto no encontrado o inactivo"

        val esSalida = TipoMovimiento.esSalida(tipo)
        if (esSalida && cantidad > producto.stock) {
            return "Stock insuficiente (disponible: ${producto.stock})"
        }

        val nuevoStock = when {
            tipo == TipoMovimiento.ENTRADA -> producto.stock + cantidad
            esSalida -> producto.stock - cantidad
            else -> cantidad
        }

        movimientoDao.insertar(
            Movimiento(
                codigoProducto = sku, tipo = tipo, cantidad = cantidad,
                motivo = motivo, observacion = observacion, usuario = usuario,
                fecha = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
            )
        )
        productoDao.actualizarStock(sku, nuevoStock)

        return null
    }
}
