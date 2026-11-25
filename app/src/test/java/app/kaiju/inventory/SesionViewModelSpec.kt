package app.kaiju.inventory

import app.kaiju.inventory.model.Rol
import app.kaiju.inventory.model.TipoMovimiento
import app.kaiju.inventory.model.Usuario
import app.kaiju.inventory.viewmodel.SesionViewModel
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe

class SesionViewModelSpec : FunSpec({

    test("SesionViewModel debe verificar permisos y tipos de movimientos permitidos por rol") {
        val sesionVM = SesionViewModel()
        val admin = Usuario("admin@kaiju.cl", "1234", "Admin", Rol.ADMINISTRADOR)
        val vendedor = Usuario("vendedor@kaiju.cl", "1234", "Vendedor", Rol.VENDEDOR)
        val encargado = Usuario("encargado@kaiju.cl", "1234", "Encargado", Rol.ENCARGADO_INVENTARIO)

        sesionVM.puedeGestionarProductos(admin).shouldBeTrue()
        sesionVM.puedeVerAlertas(admin).shouldBeTrue()

        sesionVM.puedeGestionarProductos(vendedor).shouldBeFalse()
        sesionVM.puedeVerAlertas(vendedor).shouldBeFalse()
        sesionVM.tiposMovimientoPermitidos(vendedor) shouldBe listOf(TipoMovimiento.SALIDA_VENTA)

        sesionVM.puedeGestionarProductos(encargado).shouldBeFalse()
        sesionVM.puedeVerAlertas(encargado).shouldBeTrue()
        sesionVM.tiposMovimientoPermitidos(encargado) shouldBe TipoMovimiento.todos
    }
})
