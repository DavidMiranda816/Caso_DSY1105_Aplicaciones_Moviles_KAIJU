package app.kaiju.inventory

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.kaiju.inventory.database.KaijuDatabase
import app.kaiju.inventory.database.ProductoDao
import app.kaiju.inventory.model.Producto
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ProductoDaoEmulationTest {

    private lateinit var db: KaijuDatabase
    private lateinit var productoDao: ProductoDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, KaijuDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        productoDao = db.productoDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun test01_insertarYBuscarProductoEnRoomEmulado() = runBlocking {
        val producto = Producto(
            sku = "EMU-001",
            nombreProducto = "Chaqueta Kaiju Emulacion",
            descripcion = "Chaqueta de prueba en base de datos emulada",
            marca = "Kaiju Pro",
            precio = 35000.0,
            categoria = "Chaquetas",
            stock = 8,
            estadoProducto = true,
            stockMinimo = 3
        )

        productoDao.guardar(producto)

        val encontrado = productoDao.buscar("EMU-001")
        assertNotNull("El producto insertado debe encontrarse en la base de datos Room emulada", encontrado)
        assertEquals("EMU-001", encontrado?.sku)
        assertEquals("Chaqueta Kaiju Emulacion", encontrado?.nombreProducto)
        assertEquals(35000.0, encontrado?.precio ?: 0.0, 0.01)
        assertEquals(1, productoDao.contar())
    }
}
