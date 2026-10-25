package app.kaiju.inventory.viewmodel

import androidx.lifecycle.ViewModel
import app.kaiju.inventory.model.Rol
import app.kaiju.inventory.model.TipoMovimiento
import app.kaiju.inventory.model.Usuario
import app.kaiju.inventory.repository.DatosDePrueba
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SesionViewModel : ViewModel() {

    private val _usuarioActivo = MutableStateFlow<Usuario?>(null)
    val usuarioActivo: StateFlow<Usuario?> = _usuarioActivo.asStateFlow()

    fun iniciarSesion(correo: String, contrasena: String): Boolean {
        val usuario = DatosDePrueba.usuarios.find {
            it.correo == correo && it.contrasena == contrasena
        }
        return if (usuario != null) {
            _usuarioActivo.value = usuario
            true
        } else {
            false
        }
    }

    fun cerrarSesion() {
        _usuarioActivo.value = null
    }

    fun puedeGestionarProductos(usuario: Usuario?): Boolean =
        usuario?.rol == Rol.ADMINISTRADOR

    fun puedeVerAlertas(usuario: Usuario?): Boolean =
        usuario?.rol == Rol.ADMINISTRADOR || usuario?.rol == Rol.ENCARGADO_INVENTARIO

    fun tiposMovimientoPermitidos(usuario: Usuario?): List<String> = when (usuario?.rol) {
        Rol.ADMINISTRADOR -> listOf(TipoMovimiento.ENTRADA, TipoMovimiento.AJUSTE)
        Rol.ENCARGADO_INVENTARIO -> TipoMovimiento.todos
        Rol.VENDEDOR -> listOf(TipoMovimiento.SALIDA)
        null -> emptyList()
    }
}
