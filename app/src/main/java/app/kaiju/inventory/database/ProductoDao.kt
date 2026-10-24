package app.kaiju.inventory.database

import androidx.room.*
import app.kaiju.inventory.model.Producto
import kotlinx.coroutines.flow.Flow
@Dao
interface ProductoDao {
    @Query("SELECT * FROM productos WHERE estadoProducto = 1 ORDER BY nombreProducto ASC")
    fun obtenerActivos(): Flow<List<Producto>>

    @Query("SELECT * FROM productos WHERE sku = :sku")
    suspend fun buscar(sku: String): Producto?

    @Query("SELECT COUNT (*) FROM productos ")
    suspend fun contar(): Int

    @Upsert
    suspend fun guardar(producto: Producto)

    @Upsert
    suspend fun guardarTodos(productos: List<Producto>)

    @Query("Update productos set estadoProducto = 0 where sku = :sku")
    suspend fun darDeBaja(sku: String)

    @Query("Update productos set stock =:stock where sku = :sku")
    suspend fun actualizarStock(sku: String, stock: Int)

}