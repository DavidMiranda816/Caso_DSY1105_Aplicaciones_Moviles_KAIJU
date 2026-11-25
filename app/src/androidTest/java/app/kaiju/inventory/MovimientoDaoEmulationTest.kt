package app.kaiju.inventory

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.kaiju.inventory.database.KaijuDatabase
import app.kaiju.inventory.database.MovimientoDao
import app.kaiju.inventory.database.ProductoDao
import app.kaiju.inventory.model.Movimiento
import app.kaiju.inventory.model.Producto
import app.kaiju.inventory.model.TipoMovimiento
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class MovimientoDaoEmulationTest {

    private lateinit var db: KaijuDatabase
    private lateinit var productoDao: ProductoDao
    private lateinit var movimientoDao: MovimientoDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, KaijuDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        productoDao = db.productoDao()
        movimientoDao = db.movimientoDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun test02_actualizarStockYRegistrarMovimientoEnRoomEmulado() = runBlocking {
        val producto = Producto(
            sku = "EMU-002",
            nombreProducto = "Polera Manga Larga",
            descripcion = "Descripción de prueba",
            marca = "Kaiju",
            precio = 14990.0,
            categoria = "Poleras",
            stock = 10,
            estadoProducto = true,
            stockMinimo = 4
        )
        productoDao.guardar(producto)

        // Actualizar stock en la base de datos emulada
        productoDao.actualizarStock("EMU-002", 25)
        val productoActualizado = productoDao.buscar("EMU-002")
        assertEquals(25, productoActualizado?.stock)

        // Registrar un movimiento de entrada
        val movimiento = Movimiento(
            codigoProducto = "EMU-002",
            tipo = TipoMovimiento.ENTRADA,
            cantidad = 15,
            motivo = "Reabastecimiento bodega",
            observacion = "Prueba de emulación",
            fecha = "2025-03-30 10:00",
            usuario = "Admin Emulador"
        )
        movimientoDao.insertar(movimiento)

        val listaMovimientos = movimientoDao.obtenerPorProducto("EMU-002").first()
        assertEquals(1, listaMovimientos.size)
        assertEquals(15, listaMovimientos.first().cantidad)
        assertEquals(TipoMovimiento.ENTRADA, listaMovimientos.first().tipo)

        // Dar de baja lógica al producto
        productoDao.darDeBaja("EMU-002")
        val productoInactivo = productoDao.buscar("EMU-002")
        assertFalse("El producto debe tener estadoProducto = false tras la baja lógica", productoInactivo?.estadoProducto ?: true)
    }
}
