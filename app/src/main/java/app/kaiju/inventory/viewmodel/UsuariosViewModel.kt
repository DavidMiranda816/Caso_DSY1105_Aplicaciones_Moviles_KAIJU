package app.kaiju.inventory.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.kaiju.inventory.data.model.UsuarioResponse
import app.kaiju.inventory.model.Rol
import app.kaiju.inventory.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

data class UsuariosUiState(
    val usuarios: List<UsuarioResponse> = emptyList(),
    val cargando: Boolean = false,
    val error: String? = null
)

class UsuariosViewModel(
    private val repository: UsuarioRepository = UsuarioRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(UsuariosUiState())
    val uiState: StateFlow<UsuariosUiState> = _uiState.asStateFlow()

    fun cargar(token: String) {
        _uiState.update { it.copy(cargando = true, error = null) }
        viewModelScope.launch {
            repository.listar(token).fold(
                onSuccess = { lista -> _uiState.update { it.copy(cargando = false, usuarios = lista) } },
                onFailure = { mostrarError("No se pudo cargar la lista de usuarios") }
            )
        }
    }

    fun crear(
        token: String,
        nombre: String,
        correo: String,
        password: String,
        rol: Rol,
        alGuardar: () -> Unit
    ) {
        if (nombre.isBlank() || correo.isBlank() || password.isBlank() || password.length < 8) {
            mostrarError("Completa todos los campos. La contraseña debe tener al menos 8 caracteres")
            return
        }
        modificar(token, alGuardar) {
            repository.crear(token, nombre.trim(), correo.trim(), password, rol)
        }
    }

    fun editar(token: String, id: Long, nombre: String, correo: String, alGuardar: () -> Unit) {
        if (nombre.isBlank() || correo.isBlank()) {
            mostrarError("El nombre y el correo son obligatorios")
            return
        }
        modificar(token, alGuardar) {
            repository.editar(token, id, nombre.trim(), correo.trim())
        }
    }

    fun eliminar(token: String, id: Long) {
        modificar(token, {}) { repository.eliminar(token, id) }
    }

    fun limpiarError() {
        _uiState.update { it.copy(error = null) }
    }

    private fun modificar(
        token: String,
        alTerminar: () -> Unit,
        operacion: suspend () -> Result<*>
    ) {
        if (_uiState.value.cargando) return
        _uiState.update { it.copy(cargando = true, error = null) }
        viewModelScope.launch {
            operacion().fold(
                onSuccess = {
                    alTerminar()
                    cargar(token)
                },
                onFailure = { error ->
                    val mensaje = when (error) {
                        is HttpException -> when (error.code()) {
                            400 -> "Revisa el nombre, el correo y la contraseña"
                            401 -> "La sesión venció. Vuelve a iniciar sesión"
                            403 -> "Tu cuenta no tiene permiso para gestionar usuarios"
                            409 -> "El correo ya está registrado"
                            else -> "No se pudo completar la operación"
                        }
                        is IOException -> "No se pudo conectar con el backend"
                        else -> "No se pudo completar la operación"
                    }
                    mostrarError(mensaje)
                }
            )
        }
    }

    private fun mostrarError(mensaje: String) {
        _uiState.update { it.copy(cargando = false, error = mensaje) }
    }
}
