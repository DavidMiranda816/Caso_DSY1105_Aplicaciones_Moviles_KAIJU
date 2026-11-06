package app.kaiju.inventory.database

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import app.kaiju.inventory.model.Movimiento
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class MovimientoDao_Impl(
  __db: RoomDatabase,
) : MovimientoDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfMovimiento: EntityInsertAdapter<Movimiento>
  init {
    this.__db = __db
    this.__insertAdapterOfMovimiento = object : EntityInsertAdapter<Movimiento>() {
      protected override fun createQuery(): String = "INSERT OR ABORT INTO `movimientos` (`id`,`codigoProducto`,`tipo`,`cantidad`,`motivo`,`observacion`,`fecha`,`usuario`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: Movimiento) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.codigoProducto)
        statement.bindText(3, entity.tipo)
        statement.bindLong(4, entity.cantidad.toLong())
        statement.bindText(5, entity.motivo)
        statement.bindText(6, entity.observacion)
        statement.bindText(7, entity.fecha)
        statement.bindText(8, entity.usuario)
      }
    }
  }

  public override suspend fun insertar(movimiento: Movimiento): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfMovimiento.insert(_connection, movimiento)
  }

  public override fun obtenerTodos(): Flow<List<Movimiento>> {
    val _sql: String = "select * from movimientos order by id desc"
    return createFlow(__db, false, arrayOf("movimientos")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfCodigoProducto: Int = getColumnIndexOrThrow(_stmt, "codigoProducto")
        val _columnIndexOfTipo: Int = getColumnIndexOrThrow(_stmt, "tipo")
        val _columnIndexOfCantidad: Int = getColumnIndexOrThrow(_stmt, "cantidad")
        val _columnIndexOfMotivo: Int = getColumnIndexOrThrow(_stmt, "motivo")
        val _columnIndexOfObservacion: Int = getColumnIndexOrThrow(_stmt, "observacion")
        val _columnIndexOfFecha: Int = getColumnIndexOrThrow(_stmt, "fecha")
        val _columnIndexOfUsuario: Int = getColumnIndexOrThrow(_stmt, "usuario")
        val _result: MutableList<Movimiento> = mutableListOf()
        while (_stmt.step()) {
          val _item: Movimiento
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpCodigoProducto: String
          _tmpCodigoProducto = _stmt.getText(_columnIndexOfCodigoProducto)
          val _tmpTipo: String
          _tmpTipo = _stmt.getText(_columnIndexOfTipo)
          val _tmpCantidad: Int
          _tmpCantidad = _stmt.getLong(_columnIndexOfCantidad).toInt()
          val _tmpMotivo: String
          _tmpMotivo = _stmt.getText(_columnIndexOfMotivo)
          val _tmpObservacion: String
          _tmpObservacion = _stmt.getText(_columnIndexOfObservacion)
          val _tmpFecha: String
          _tmpFecha = _stmt.getText(_columnIndexOfFecha)
          val _tmpUsuario: String
          _tmpUsuario = _stmt.getText(_columnIndexOfUsuario)
          _item = Movimiento(_tmpId,_tmpCodigoProducto,_tmpTipo,_tmpCantidad,_tmpMotivo,_tmpObservacion,_tmpFecha,_tmpUsuario)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun obtenerPorProducto(codigoProducto: String): Flow<List<Movimiento>> {
    val _sql: String = "select * from movimientos where codigoProducto = ? order by id desc"
    return createFlow(__db, false, arrayOf("movimientos")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, codigoProducto)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfCodigoProducto: Int = getColumnIndexOrThrow(_stmt, "codigoProducto")
        val _columnIndexOfTipo: Int = getColumnIndexOrThrow(_stmt, "tipo")
        val _columnIndexOfCantidad: Int = getColumnIndexOrThrow(_stmt, "cantidad")
        val _columnIndexOfMotivo: Int = getColumnIndexOrThrow(_stmt, "motivo")
        val _columnIndexOfObservacion: Int = getColumnIndexOrThrow(_stmt, "observacion")
        val _columnIndexOfFecha: Int = getColumnIndexOrThrow(_stmt, "fecha")
        val _columnIndexOfUsuario: Int = getColumnIndexOrThrow(_stmt, "usuario")
        val _result: MutableList<Movimiento> = mutableListOf()
        while (_stmt.step()) {
          val _item: Movimiento
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpCodigoProducto: String
          _tmpCodigoProducto = _stmt.getText(_columnIndexOfCodigoProducto)
          val _tmpTipo: String
          _tmpTipo = _stmt.getText(_columnIndexOfTipo)
          val _tmpCantidad: Int
          _tmpCantidad = _stmt.getLong(_columnIndexOfCantidad).toInt()
          val _tmpMotivo: String
          _tmpMotivo = _stmt.getText(_columnIndexOfMotivo)
          val _tmpObservacion: String
          _tmpObservacion = _stmt.getText(_columnIndexOfObservacion)
          val _tmpFecha: String
          _tmpFecha = _stmt.getText(_columnIndexOfFecha)
          val _tmpUsuario: String
          _tmpUsuario = _stmt.getText(_columnIndexOfUsuario)
          _item = Movimiento(_tmpId,_tmpCodigoProducto,_tmpTipo,_tmpCantidad,_tmpMotivo,_tmpObservacion,_tmpFecha,_tmpUsuario)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
