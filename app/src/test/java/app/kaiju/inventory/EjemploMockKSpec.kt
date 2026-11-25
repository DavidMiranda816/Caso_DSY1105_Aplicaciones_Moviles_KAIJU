package app.kaiju.inventory

import app.kaiju.inventory.database.MovimientoDao
import app.kaiju.inventory.database.ProductoDao
import app.kaiju.inventory.model.Producto
import app.kaiju.inventory.model.TipoMovimiento
import app.kaiju.inventory.repository.InventarioRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

class EjemploMockKSpec : FunSpec({

    test("InventarioRepository usando MockK debe rechazar movimiento por stock insuficiente") {
        val mockProductoDao = mockk<ProductoDao>(relaxed = true)
        val mockMovimientoDao = mockk<MovimientoDao>(relaxed = true)

        val productoExistente = Producto(
            sku = "PMC-002",
            nombreProducto = "Polera Gato Samurái",
            descripcion = "Polera estampada",
            marca = "Marca propia",
            precio = 13000.0,
            categoria = "Polera",
            stock = 3,
            estadoProducto = true,
            stockMinimo = 6
        )

        coEvery { mockProductoDao.buscar("PMC-002") } returns productoExistente

        val repository = InventarioRepository(mockProductoDao, mockMovimientoDao)
        val error = repository.registrarMovimiento(
            sku = "PMC-002",
            tipo = TipoMovimiento.SALIDA_VENTA,
            cantidad = 5,
            motivo = "Venta cliente",
            observacion = "",
            usuario = "Vendedor Test"
        )

        error shouldBe "Stock insuficiente (disponible: 3)"
        coVerify(exactly = 0) { mockMovimientoDao.insertar(any()) }
        coVerify(exactly = 0) { mockProductoDao.actualizarStock(any(), any()) }
    }
})
