package app.kaiju.inventory.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.kaiju.inventory.model.LoginEstado
import app.kaiju.inventory.model.ErrorLogin
import app.kaiju.inventory.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _loginEstado = MutableStateFlow(LoginEstado())
    val loginEstado: StateFlow<LoginEstado> = _loginEstado.asStateFlow()

    private val _errorLogin = MutableStateFlow(ErrorLogin())
    val errorLogin: StateFlow<ErrorLogin> = _errorLogin.asStateFlow()

    private val _estaCargando = MutableStateFlow(false)
    val estaCargando: StateFlow<Boolean> = _estaCargando.asStateFlow()

    var jwtToken: String? = null
        private set

    fun alCambiarCorreo(correo: String) {
        _loginEstado.update { it.copy(correo = correo) }
        _errorLogin.update { it.copy(mensajeError = null) }
    }

    fun alCambiarContrasena(contrasena: String) {
        _loginEstado.update { it.copy(contrasena = contrasena) }
        _errorLogin.update { it.copy(mensajeError = null) }
    }

    fun iniciarSesion(validarLocal: (String, String) -> Boolean = { _, _ -> false }) {
        val estadoActual = _loginEstado.value
        if (estadoActual.correo.isBlank() || estadoActual.contrasena.isBlank()) {
            _errorLogin.update { it.copy(mensajeError = "Por favor, completa todos los campos") }
            return
        }
        viewModelScope.launch {
            _estaCargando.value = true
            _errorLogin.update { it.copy(mensajeError = null) }
            try {
                val resultado = authRepository.login(estadoActual.correo.trim(), estadoActual.contrasena)
                if (resultado.isSuccess) {
                    val loginResponse = resultado.getOrNull()
                    jwtToken = loginResponse?.token
                    _loginEstado.update { it.copy(inicioCompletado = true) }
                } else {
                    if (validarLocal(estadoActual.correo.trim(), estadoActual.contrasena)) {
                        _loginEstado.update { it.copy(inicioCompletado = true) }
                    } else {
                        val errMessage = resultado.exceptionOrNull()?.localizedMessage ?: "Credenciales inválidas"
                        _errorLogin.update { it.copy(mensajeError = "Error de autenticación: $errMessage") }
                    }
                }
            } catch (e: Exception) {
                if (validarLocal(estadoActual.correo.trim(), estadoActual.contrasena)) {
                    _loginEstado.update { it.copy(inicioCompletado = true) }
                } else {
                    _errorLogin.update { it.copy(mensajeError = "No se pudo conectar al servidor: ${e.localizedMessage}") }
                }
            } finally {
                _estaCargando.value = false
            }
        }
    }
}
