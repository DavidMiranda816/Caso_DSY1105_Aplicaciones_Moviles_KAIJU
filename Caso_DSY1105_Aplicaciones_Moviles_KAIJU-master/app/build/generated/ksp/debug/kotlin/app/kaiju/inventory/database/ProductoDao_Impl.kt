package app.kaiju.inventory.database

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.EntityUpsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import app.kaiju.inventory.model.Producto
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Double
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
public class ProductoDao_Impl(
  __db: RoomDatabase,
) : ProductoDao {
  private val __db: RoomDatabase

  private val __upsertAdapterOfProducto: EntityUpsertAdapter<Producto>
  init {
    this.__db = __db
    this.__upsertAdapterOfProducto = EntityUpsertAdapter<Producto>(object : EntityInsertAdapter<Producto>() {
      protected override fun createQuery(): String = "INSERT INTO `productos` (`sku`,`nombreProducto`,`descripcion`,`marca`,`precio`,`categoria`,`stock`,`estadoProducto`,`stockMinimo`) VALUES (?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: Producto) {
        statement.bindText(1, entity.sku)
        statement.bindText(2, entity.nombreProducto)
        statement.bindText(3, entity.descripcion)
        statement.bindText(4, entity.marca)
        statement.bindDouble(5, entity.precio)
        statement.bindText(6, entity.categoria)
        statement.bindLong(7, entity.stock.toLong())
        val _tmp: Int = if (entity.estadoProducto) 1 else 0
        statement.bindLong(8, _tmp.toLong())
        statement.bindLong(9, entity.stockMinimo.toLong())
      }
    }, object : EntityDeleteOrUpdateAdapter<Producto>() {
      protected override fun createQuery(): String = "UPDATE `productos` SET `sku` = ?,`nombreProducto` = ?,`descripcion` = ?,`marca` = ?,`precio` = ?,`categoria` = ?,`stock` = ?,`estadoProducto` = ?,`stockMinimo` = ? WHERE `sku` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: Producto) {
        statement.bindText(1, entity.sku)
        statement.bindText(2, entity.nombreProducto)
        statement.bindText(3, entity.descripcion)
        statement.bindText(4, entity.marca)
        statement.bindDouble(5, entity.precio)
        statement.bindText(6, entity.categoria)
        statement.bindLong(7, entity.stock.toLong())
        val _tmp: Int = if (entity.estadoProducto) 1 else 0
        statement.bindLong(8, _tmp.toLong())
        statement.bindLong(9, entity.stockMinimo.toLong())
        statement.bindText(10, entity.sku)
      }
    })
  }

  public override suspend fun guardar(producto: Producto): Unit = performSuspending(__db, false, true) { _connection ->
    __upsertAdapterOfProducto.upsert(_connection, producto)
  }

  public override suspend fun guardarTodos(productos: List<Producto>): Unit = performSuspending(__db, false, true) { _connection ->
    __upsertAdapterOfProducto.upsert(_connection, productos)
  }

  public override fun obtenerActivos(): Flow<List<Producto>> {
    val _sql: String = "SELECT * FROM productos WHERE estadoProducto = 1 ORDER BY nombreProducto ASC"
    return createFlow(__db, false, arrayOf("productos")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfSku: Int = getColumnIndexOrThrow(_stmt, "sku")
        val _columnIndexOfNombreProducto: Int = getColumnIndexOrThrow(_stmt, "nombreProducto")
        val _columnIndexOfDescripcion: Int = getColumnIndexOrThrow(_stmt, "descripcion")
        val _columnIndexOfMarca: Int = getColumnIndexOrThrow(_stmt, "marca")
        val _columnIndexOfPrecio: Int = getColumnIndexOrThrow(_stmt, "precio")
        val _columnIndexOfCategoria: Int = getColumnIndexOrThrow(_stmt, "categoria")
        val _columnIndexOfStock: Int = getColumnIndexOrThrow(_stmt, "stock")
        val _columnIndexOfEstadoProducto: Int = getColumnIndexOrThrow(_stmt, "estadoProducto")
        val _columnIndexOfStockMinimo: Int = getColumnIndexOrThrow(_stmt, "stockMinimo")
        val _result: MutableList<Producto> = mutableListOf()
        while (_stmt.step()) {
          val _item: Producto
          val _tmpSku: String
          _tmpSku = _stmt.getText(_columnIndexOfSku)
          val _tmpNombreProducto: String
          _tmpNombreProducto = _stmt.getText(_columnIndexOfNombreProducto)
          val _tmpDescripcion: String
          _tmpDescripcion = _stmt.getText(_columnIndexOfDescripcion)
          val _tmpMarca: String
          _tmpMarca = _stmt.getText(_columnIndexOfMarca)
          val _tmpPrecio: Double
          _tmpPrecio = _stmt.getDouble(_columnIndexOfPrecio)
          val _tmpCategoria: String
          _tmpCategoria = _stmt.getText(_columnIndexOfCategoria)
          val _tmpStock: Int
          _tmpStock = _stmt.getLong(_columnIndexOfStock).toInt()
          val _tmpEstadoProducto: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfEstadoProducto).toInt()
          _tmpEstadoProducto = _tmp != 0
          val _tmpStockMinimo: Int
          _tmpStockMinimo = _stmt.getLong(_columnIndexOfStockMinimo).toInt()
          _item = Producto(_tmpSku,_tmpNombreProducto,_tmpDescripcion,_tmpMarca,_tmpPrecio,_tmpCategoria,_tmpStock,_tmpEstadoProducto,_tmpStockMinimo)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun buscar(sku: String): Producto? {
    val _sql: String = "SELECT * FROM productos WHERE sku = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, sku)
        val _columnIndexOfSku: Int = getColumnIndexOrThrow(_stmt, "sku")
        val _columnIndexOfNombreProducto: Int = getColumnIndexOrThrow(_stmt, "nombreProducto")
        val _columnIndexOfDescripcion: Int = getColumnIndexOrThrow(_stmt, "descripcion")
        val _columnIndexOfMarca: Int = getColumnIndexOrThrow(_stmt, "marca")
        val _columnIndexOfPrecio: Int = getColumnIndexOrThrow(_stmt, "precio")
        val _columnIndexOfCategoria: Int = getColumnIndexOrThrow(_stmt, "categoria")
        val _columnIndexOfStock: Int = getColumnIndexOrThrow(_stmt, "stock")
        val _columnIndexOfEstadoProducto: Int = getColumnIndexOrThrow(_stmt, "estadoProducto")
        val _columnIndexOfStockMinimo: Int = getColumnIndexOrThrow(_stmt, "stockMinimo")
        val _result: Producto?
        if (_stmt.step()) {
          val _tmpSku: String
          _tmpSku = _stmt.getText(_columnIndexOfSku)
          val _tmpNombreProducto: String
          _tmpNombreProducto = _stmt.getText(_columnIndexOfNombreProducto)
          val _tmpDescripcion: String
          _tmpDescripcion = _stmt.getText(_columnIndexOfDescripcion)
          val _tmpMarca: String
          _tmpMarca = _stmt.getText(_columnIndexOfMarca)
          val _tmpPrecio: Double
          _tmpPrecio = _stmt.getDouble(_columnIndexOfPrecio)
          val _tmpCategoria: String
          _tmpCategoria = _stmt.getText(_columnIndexOfCategoria)
          val _tmpStock: Int
          _tmpStock = _stmt.getLong(_columnIndexOfStock).toInt()
          val _tmpEstadoProducto: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfEstadoProducto).toInt()
          _tmpEstadoProducto = _tmp != 0
          val _tmpStockMinimo: Int
          _tmpStockMinimo = _stmt.getLong(_columnIndexOfStockMinimo).toInt()
          _result = Producto(_tmpSku,_tmpNombreProducto,_tmpDescripcion,_tmpMarca,_tmpPrecio,_tmpCategoria,_tmpStock,_tmpEstadoProducto,_tmpStockMinimo)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun contar(): Int {
    val _sql: String = "SELECT COUNT (*) FROM productos "
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: Int
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp
        } else {
          _result = 0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun darDeBaja(sku: String) {
    val _sql: String = "Update productos set estadoProducto = 0 where sku = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, sku)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun actualizarStock(sku: String, stock: Int) {
    val _sql: String = "Update productos set stock =? where sku = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, stock.toLong())
        _argIndex = 2
        _stmt.bindText(_argIndex, sku)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
