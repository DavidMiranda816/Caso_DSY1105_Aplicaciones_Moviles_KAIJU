package app.kaiju.inventory.fakes

import app.kaiju.inventory.database.MovimientoDao
import app.kaiju.inventory.database.ProductoDao
import app.kaiju.inventory.model.Movimiento
import app.kaiju.inventory.model.Producto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeProductoDao(
    initialProductos: List<Producto> = emptyList()
) : ProductoDao {
    private val lista = MutableStateFlow(initialProductos.toMutableList())

    override fun obtenerActivos(): Flow<List<Producto>> =
        lista.map { productos -> productos.filter { it.estadoProducto } }

    override suspend fun buscar(sku: String): Producto? =
        lista.value.find { it.sku.equals(sku, ignoreCase = true) }

    override suspend fun contar(): Int = lista.value.size

    override suspend fun guardar(producto: Producto) {
        val current = lista.value.toMutableList()
        val index = current.indexOfFirst { it.sku.equals(producto.sku, ignoreCase = true) }
        if (index >= 0) {
            current[index] = producto
        } else {
            current.add(producto)
        }
        lista.value = current
    }

    override suspend fun guardarTodos(productos: List<Producto>) {
        val current = lista.value.toMutableList()
        current.addAll(productos)
        lista.value = current
    }

    override suspend fun darDeBaja(sku: String) {
        val current = lista.value.toMutableList()
        val index = current.indexOfFirst { it.sku.equals(sku, ignoreCase = true) }
        if (index >= 0) {
            current[index] = current[index].copy(estadoProducto = false)
            lista.value = current
        }
    }

    override suspend fun actualizarStock(sku: String, stock: Int) {
        val current = lista.value.toMutableList()
        val index = current.indexOfFirst { it.sku.equals(sku, ignoreCase = true) }
        if (index >= 0) {
            current[index] = current[index].copy(stock = stock)
            lista.value = current
        }
    }
}

class FakeMovimientoDao : MovimientoDao {
    private val lista = MutableStateFlow<List<Movimiento>>(emptyList())

    override suspend fun insertar(movimiento: Movimiento) {
        lista.value = listOf(movimiento) + lista.value
    }

    override fun obtenerTodos(): Flow<List<Movimiento>> = lista

    override fun obtenerPorProducto(codigoProducto: String): Flow<List<Movimiento>> =
        lista.map { movs -> movs.filter { it.codigoProducto == codigoProducto } }
}
