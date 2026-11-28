package app.kaiju.inventory.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import app.kaiju.inventory.model.Movimiento
import app.kaiju.inventory.model.Producto

@Database(entities = [Producto::class, Movimiento::class], version = 2, exportSchema = false)
abstract class KaijuDatabase : RoomDatabase() {
    abstract fun productoDao(): ProductoDao
    abstract fun movimientoDao(): MovimientoDao

    companion object {
        @Volatile private var instancia: KaijuDatabase? = null

        fun obtenerInstancia(contexto: Context): KaijuDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    contexto.applicationContext, KaijuDatabase::class.java, "kaiju.db"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                    .also { instancia = it }
            }
    }
}