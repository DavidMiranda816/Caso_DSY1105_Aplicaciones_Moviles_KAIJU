package app.kaiju.inventory.database

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class KaijuDatabase_Impl : KaijuDatabase() {
  private val _productoDao: Lazy<ProductoDao> = lazy {
    ProductoDao_Impl(this)
  }

  private val _movimientoDao: Lazy<MovimientoDao> = lazy {
    MovimientoDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(2, "3a5bb33db18045d0eec82cbe425bf4e9", "6f0e65d77bfd0174d69dcb22abf6a6a4") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `productos` (`sku` TEXT NOT NULL, `nombreProducto` TEXT NOT NULL, `descripcion` TEXT NOT NULL, `marca` TEXT NOT NULL, `precio` REAL NOT NULL, `categoria` TEXT NOT NULL, `stock` INTEGER NOT NULL, `estadoProducto` INTEGER NOT NULL, `stockMinimo` INTEGER NOT NULL, PRIMARY KEY(`sku`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `movimientos` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `codigoProducto` TEXT NOT NULL, `tipo` TEXT NOT NULL, `cantidad` INTEGER NOT NULL, `motivo` TEXT NOT NULL, `observacion` TEXT NOT NULL, `fecha` TEXT NOT NULL, `usuario` TEXT NOT NULL)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_movimientos_codigoProducto` ON `movimientos` (`codigoProducto`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '3a5bb33db18045d0eec82cbe425bf4e9')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `productos`")
        connection.execSQL("DROP TABLE IF EXISTS `movimientos`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection): RoomOpenDelegate.ValidationResult {
        val _columnsProductos: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsProductos.put("sku", TableInfo.Column("sku", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsProductos.put("nombreProducto", TableInfo.Column("nombreProducto", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsProductos.put("descripcion", TableInfo.Column("descripcion", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsProductos.put("marca", TableInfo.Column("marca", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsProductos.put("precio", TableInfo.Column("precio", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsProductos.put("categoria", TableInfo.Column("categoria", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsProductos.put("stock", TableInfo.Column("stock", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsProductos.put("estadoProducto", TableInfo.Column("estadoProducto", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsProductos.put("stockMinimo", TableInfo.Column("stockMinimo", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysProductos: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesProductos: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoProductos: TableInfo = TableInfo("productos", _columnsProductos, _foreignKeysProductos, _indicesProductos)
        val _existingProductos: TableInfo = read(connection, "productos")
        if (!_infoProductos.equals(_existingProductos)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |productos(app.kaiju.inventory.model.Producto).
              | Expected:
              |""".trimMargin() + _infoProductos + """
              |
              | Found:
              |""".trimMargin() + _existingProductos)
        }
        val _columnsMovimientos: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsMovimientos.put("id", TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMovimientos.put("codigoProducto", TableInfo.Column("codigoProducto", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMovimientos.put("tipo", TableInfo.Column("tipo", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMovimientos.put("cantidad", TableInfo.Column("cantidad", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMovimientos.put("motivo", TableInfo.Column("motivo", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMovimientos.put("observacion", TableInfo.Column("observacion", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMovimientos.put("fecha", TableInfo.Column("fecha", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMovimientos.put("usuario", TableInfo.Column("usuario", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysMovimientos: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesMovimientos: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesMovimientos.add(TableInfo.Index("index_movimientos_codigoProducto", false, listOf("codigoProducto"), listOf("ASC")))
        val _infoMovimientos: TableInfo = TableInfo("movimientos", _columnsMovimientos, _foreignKeysMovimientos, _indicesMovimientos)
        val _existingMovimientos: TableInfo = read(connection, "movimientos")
        if (!_infoMovimientos.equals(_existingMovimientos)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |movimientos(app.kaiju.inventory.model.Movimiento).
              | Expected:
              |""".trimMargin() + _infoMovimientos + """
              |
              | Found:
              |""".trimMargin() + _existingMovimientos)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "productos", "movimientos")
  }

  public override fun clearAllTables() {
    super.performClear(false, "productos", "movimientos")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(ProductoDao::class, ProductoDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(MovimientoDao::class, MovimientoDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>): List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun productoDao(): ProductoDao = _productoDao.value

  public override fun movimientoDao(): MovimientoDao = _movimientoDao.value
}
