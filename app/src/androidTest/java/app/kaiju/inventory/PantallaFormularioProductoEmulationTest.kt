package app.kaiju.inventory

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.room.Room
import app.kaiju.inventory.database.KaijuDatabase
import app.kaiju.inventory.repository.InventarioRepository
import app.kaiju.inventory.ui.screen.PantallaFormularioProducto
import app.kaiju.inventory.ui.theme.KaijuTheme
import app.kaiju.inventory.viewmodel.KaijuViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PantallaFormularioProductoEmulationTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun formularioProductoMuestraErroresSiCamposEstanVacios() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = Room.inMemoryDatabaseBuilder(context, KaijuDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        val repository = InventarioRepository(db.productoDao(), db.movimientoDao())
        val viewModel = KaijuViewModel(repository)

        composeRule.setContent {
            KaijuTheme {
                PantallaFormularioProducto(
                    viewModel = viewModel,
                    skuEditar = null,
                    onTerminado = {}
                )
            }
        }

        composeRule.onNodeWithText("Agregar producto").performClick()

        composeRule.onNodeWithText("El SKU es obligatorio").assertIsDisplayed()
        db.close()
    }

    @Test
    fun formularioProductoGuardaDatoCorrectamenteEnBaseDeDatos() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = Room.inMemoryDatabaseBuilder(context, KaijuDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        val repository = InventarioRepository(db.productoDao(), db.movimientoDao())
        val viewModel = KaijuViewModel(repository)

        var guardadoExitoso = false

        composeRule.setContent {
            KaijuTheme {
                PantallaFormularioProducto(
                    viewModel = viewModel,
                    skuEditar = null,
                    onTerminado = { guardadoExitoso = true }
                )
            }
        }

        // Rellenar campos válidos usando performTextInput
        composeRule.onNodeWithText("SKU").performTextInput("PROD-999")
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Nombre del Producto").performTextInput("Chaqueta Kaiju Test")
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Precio ($)").performTextInput("25000")
        composeRule.waitForIdle()

        // Asignar categoría y stock inicial
        viewModel.actualizarCategoria("Chaquetas")
        composeRule.waitForIdle()

        composeRule.onNodeWithText("Stock inicial").performTextInput("10")
        composeRule.waitForIdle()

        viewModel.actualizarStockMinimo("2")
        composeRule.waitForIdle()

        // Hacer clic en Agregar producto
        composeRule.onNodeWithText("Agregar producto").performClick()
        composeRule.waitForIdle()

        // Verificar que se guardó exitosamente y se llamó a onTerminado
        assertTrue("El objeto producto debió guardarse y llamar a onTerminado", guardadoExitoso)

        // Verificar en la base de datos Room que el objeto producto fue guardado correctamente con todas sus propiedades
        val productoGuardado = db.productoDao().buscar("PROD-999")
        assertNotNull("El objeto producto creado debe existir en la base de datos Room", productoGuardado)

        // Aserciones exhaustivas de cada propiedad del objeto guardado
        assertEquals("PROD-999", productoGuardado?.sku)
        assertEquals("Chaqueta Kaiju Test", productoGuardado?.nombreProducto)
        assertEquals(25000.0, productoGuardado?.precio ?: 0.0, 0.01)
        assertEquals("Chaquetas", productoGuardado?.categoria)
        assertEquals(10, productoGuardado?.stock)
        assertEquals(2, productoGuardado?.stockMinimo)
        assertTrue("El estado del objeto producto creado debe ser activo", productoGuardado?.estadoProducto == true)

        // Verificar que el repositorio expone el objeto creado en la lista de productos activos
        val listaActivos = repository.productos.first()
        assertTrue("La lista de productos activos debe contener el objeto creado", listaActivos.any { it.sku == "PROD-999" })

        db.close()
    }
}
