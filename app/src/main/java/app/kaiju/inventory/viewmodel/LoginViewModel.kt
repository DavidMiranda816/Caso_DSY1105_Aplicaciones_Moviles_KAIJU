package app.kaiju.inventory.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.kaiju.inventory.model.ErrorLogin
import app.kaiju.inventory.model.LoginEstado
import app.kaiju.inventory.model.Rol                                  
import app.kaiju.inventory.model.Usuario                              
import app.kaiju.inventory.repository.InventarioRepository            
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(                                                 
    private val repository: InventarioRepository = InventarioRepository()
) : ViewModel() {

    private val _loginEstado = MutableStateFlow(LoginEstado())
    val loginEstado: StateFlow<LoginEstado> = _loginEstado.asStateFlow()

    private val _errorLogin = MutableStateFlow(ErrorLogin())
    val errorLogin: StateFlow<ErrorLogin> = _errorLogin.asStateFlow()

    private val _estaCargando = MutableStateFlow(false)
    val estaCargando: StateFlow<Boolean> = _estaCargando.asStateFlow()

    private val _usuarioActivo = MutableStateFlow<Usuario?>(null)
    val usuarioActivo: StateFlow<Usuario?> = _usuarioActivo.asStateFlow()

    fun alCambiarCorreo(correo: String) {
        _loginEstado.update { it.copy(correo = correo) }
        _errorLogin.update { it.copy(mensajeError = null) }
    }

    fun alCambiarContrasena(contrasena: String) {
        _loginEstado.update { it.copy(contrasena = contrasena) }
        _errorLogin.update { it.copy(mensajeError = null) }
    }

    fun iniciarSesion() {
        val estadoActual = _loginEstado.value

        if (estadoActual.correo.isBlank() || estadoActual.contrasena.isBlank()) {
            _errorLogin.update { it.copy(mensajeError = "Por favor, completa todos los campos") }
            return
        }

        viewModelScope.launch {
            _estaCargando.value = true
            _errorLogin.update { it.copy(mensajeError = null) }

            try {
                delay(1500)

                val usuario = repository.buscarUsuarioPorCorreo(estadoActual.correo.trim())

                if (usuario != null && usuario.contrasena == estadoActual.contrasena) {
                    _usuarioActivo.value = usuario
                    _loginEstado.update { it.copy(inicioCompletado = true) }
                } else {
                    _errorLogin.update { it.copy(mensajeError = "Correo o contraseña incorrectos") }
                }
            } catch (e: Exception) {
                _errorLogin.update {
                    it.copy(mensajeError = e.localizedMessage ?: "Ocurrió un error inesperado")
                }
            } finally {
                _estaCargando.value = false
            }
        }
    }

    fun puedeCrearProductos(usuario: Usuario?): Boolean =
        usuario?.rol == Rol.ADMINISTRADOR

    fun puedeVerAlertas(usuario: Usuario?): Boolean =
        usuario?.rol == Rol.ADMINISTRADOR || usuario?.rol == Rol.ENCARGADO_INVENTARIO
}