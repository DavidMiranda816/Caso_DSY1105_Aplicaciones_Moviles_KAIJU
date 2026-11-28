package app.kaiju.inventory.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.kaiju.inventory.data.model.LoginResponse
import app.kaiju.inventory.model.LoginEstado
import app.kaiju.inventory.model.ErrorLogin
import app.kaiju.inventory.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class LoginViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _loginEstado = MutableStateFlow(LoginEstado())
    val loginEstado: StateFlow<LoginEstado> = _loginEstado.asStateFlow()

    private val _errorLogin = MutableStateFlow(ErrorLogin())
    val errorLogin: StateFlow<ErrorLogin> = _errorLogin.asStateFlow()

    private val _estaCargando = MutableStateFlow(false)
    val estaCargando: StateFlow<Boolean> = _estaCargando.asStateFlow()

    fun alCambiarCorreo(correo: String) {
        _loginEstado.update { it.copy(correo = correo) }
        _errorLogin.update { it.copy(mensajeError = null) }
    }

    fun alCambiarContrasena(contrasena: String) {
        _loginEstado.update { it.copy(contrasena = contrasena) }
        _errorLogin.update { it.copy(mensajeError = null) }
    }

    fun iniciarSesion(alAutenticar: (LoginResponse) -> Boolean) {
        if (_estaCargando.value) return
        val estadoActual = _loginEstado.value
        if (estadoActual.correo.isBlank() || estadoActual.contrasena.isBlank()) {
            _errorLogin.update { it.copy(mensajeError = "Por favor, completa todos los campos") }
            return
        }
        viewModelScope.launch {
            _estaCargando.value = true
            _errorLogin.update { it.copy(mensajeError = null) }
            val resultado = authRepository.login(estadoActual.correo.trim(), estadoActual.contrasena)
            val loginResponse = resultado.getOrNull()
            if (loginResponse != null && alAutenticar(loginResponse)) {
                _loginEstado.update { it.copy(contrasena = "", inicioCompletado = true) }
            } else {
                val mensaje = when (val error = resultado.exceptionOrNull()) {
                    null -> "Esta cuenta no tiene un perfil activo para Kaiju"
                    is HttpException -> if (error.code() == 401) {
                        "Correo o contraseña incorrectos"
                    } else {
                        "El servidor no pudo iniciar la sesión"
                    }
                    is IOException -> "No se pudo conectar con el backend"
                    else -> "No se pudo iniciar sesión"
                }
                _errorLogin.update { it.copy(mensajeError = mensaje) }
            }
            _estaCargando.value = false
        }
    }
}
