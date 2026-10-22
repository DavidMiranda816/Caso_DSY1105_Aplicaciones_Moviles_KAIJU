package app.kaiju.inventory.database

import androidx.room.*
import app.kaiju.inventory.model.Movimiento
import kotlinx.coroutines.flow.Flow

@Dao
interface MovimientoDao {
    @Insert
    suspend fun insertar(movimiento: Movimiento)

    @Query("select * from movimientos order by id desc")
    fun obtenerTodos(): Flow<List<Movimiento>>

    @Query("select * from movimientos where codigoProducto = :codigoProducto order by id desc")
    fun obtenerPorProducto(codigoProducto: String): Flow<List<Movimiento>>
}