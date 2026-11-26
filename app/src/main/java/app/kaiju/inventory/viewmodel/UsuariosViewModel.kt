package app.kaiju.inventory.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.kaiju.inventory.data.model.UsuarioResponse
import app.kaiju.inventory.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

    fun crear(token: String, nombre: String, correo: String, password: String) {
        if (nombre.isBlank() || correo.isBlank() || password.length < 8) {
            mostrarError("Completa todos los campos. La contraseña debe tener al menos 8 caracteres")
            return
        }
        viewModelScope.launch {
            repository.crear(nombre.trim(), correo.trim(), password).fold(
                onSuccess = { cargar(token) },
                onFailure = { mostrarError("No se pudo crear el usuario") }
            )
        }
    }

    fun editar(token: String, id: Long, nombre: String, correo: String) {
        if (nombre.isBlank() || correo.isBlank()) {
            mostrarError("El nombre y el correo son obligatorios")
            return
        }
        viewModelScope.launch {
            repository.editar(token, id, nombre.trim(), correo.trim()).fold(
                onSuccess = { cargar(token) },
                onFailure = { mostrarError("No se pudo editar el usuario") }
            )
        }
    }

    fun eliminar(token: String, id: Long) {
        viewModelScope.launch {
            repository.eliminar(token, id).fold(
                onSuccess = { cargar(token) },
                onFailure = { mostrarError("No se pudo eliminar el usuario") }
            )
        }
    }

    private fun mostrarError(mensaje: String) {
        _uiState.update { it.copy(cargando = false, error = mensaje) }
    }
}
