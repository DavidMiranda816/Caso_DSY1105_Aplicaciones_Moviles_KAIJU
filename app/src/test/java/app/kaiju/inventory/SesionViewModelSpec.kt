package app.kaiju.inventory

import app.kaiju.inventory.data.model.LoginResponse
import app.kaiju.inventory.data.model.UsuarioResponse
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
        val admin = Usuario(1, "admin@kaiju.cl", "Admin", Rol.ADMINISTRADOR)
        val vendedor = Usuario(2, "vendedor@kaiju.cl", "Vendedor", Rol.VENDEDOR)
        val encargado = Usuario(3, "encargado@kaiju.cl", "Encargado", Rol.ENCARGADO_INVENTARIO)

        sesionVM.puedeGestionarProductos(admin).shouldBeTrue()
        sesionVM.puedeVerAlertas(admin).shouldBeTrue()

        sesionVM.puedeGestionarProductos(vendedor).shouldBeFalse()
        sesionVM.puedeVerAlertas(vendedor).shouldBeFalse()
        sesionVM.tiposMovimientoPermitidos(vendedor) shouldBe listOf(TipoMovimiento.SALIDA_VENTA)

        sesionVM.puedeGestionarProductos(encargado).shouldBeFalse()
        sesionVM.puedeVerAlertas(encargado).shouldBeTrue()
        sesionVM.tiposMovimientoPermitidos(encargado) shouldBe TipoMovimiento.todos
    }

    test("La sesión usa el perfil explícito que devuelve el backend") {
        val sesion = SesionViewModel()
        mapOf(
            "ADMIN" to Rol.ADMINISTRADOR,
            "VENDEDOR" to Rol.VENDEDOR,
            "ENCARGADO" to Rol.ENCARGADO_INVENTARIO
        ).forEach { (codigo, rol) ->
            sesion.iniciarSesion(respuesta(codigo)).shouldBeTrue()
            sesion.usuarioActivo.value?.rol shouldBe rol
            sesion.token shouldBe "jwt-prueba"
        }
    }

    test("Una cuenta de Veterinaria o un perfil desconocido no abre una sesión en Kaiju") {
        val sesion = SesionViewModel()
        listOf("CLIENTE", "OTRO").forEach { codigo ->
            sesion.iniciarSesion(respuesta("ADMIN")).shouldBeTrue()
            sesion.iniciarSesion(respuesta(codigo)).shouldBeFalse()
            sesion.usuarioActivo.value shouldBe null
            sesion.token shouldBe null
        }
    }
})

private fun respuesta(rol: String) = LoginResponse(
    token = "jwt-prueba",
    tipo = "Bearer",
    usuario = UsuarioResponse(1, "Usuario", "usuario@example.com", rol, true, null)
)
