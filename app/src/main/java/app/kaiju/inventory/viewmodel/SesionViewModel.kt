package app.kaiju.inventory.viewmodel

import androidx.lifecycle.ViewModel
import app.kaiju.inventory.data.model.LoginResponse
import app.kaiju.inventory.model.Rol
import app.kaiju.inventory.model.TipoMovimiento
import app.kaiju.inventory.model.Usuario
import app.kaiju.inventory.model.rolDesdeBackend
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SesionViewModel : ViewModel() {

    private val _usuarioActivo = MutableStateFlow<Usuario?>(null)
    val usuarioActivo: StateFlow<Usuario?> = _usuarioActivo.asStateFlow()

    var token: String? = null
        private set

    fun iniciarSesion(respuesta: LoginResponse) {
        val usuario = respuesta.usuario ?: return
        token = respuesta.token
        _usuarioActivo.value = Usuario(
            correo = usuario.correo,
            contrasena = "",
            nombre = usuario.nombre,
            rol = rolDesdeBackend(usuario.rol)
        )
    }

    fun cerrarSesion() {
        _usuarioActivo.value = null
        token = null
    }

    fun puedeGestionarProductos(usuario: Usuario?): Boolean =
        usuario?.rol == Rol.ADMINISTRADOR

    fun puedeVerAlertas(usuario: Usuario?): Boolean =
        usuario?.rol == Rol.ADMINISTRADOR || usuario?.rol == Rol.ENCARGADO_INVENTARIO

    fun tiposMovimientoPermitidos(usuario: Usuario?): List<String> = when (usuario?.rol) {
        Rol.ADMINISTRADOR -> listOf(TipoMovimiento.ENTRADA, TipoMovimiento.AJUSTE)
        Rol.ENCARGADO_INVENTARIO -> TipoMovimiento.todos
        Rol.VENDEDOR -> listOf(TipoMovimiento.SALIDA_VENTA)
        null -> emptyList()
    }
}
