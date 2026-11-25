package app.kaiju.inventory.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.kaiju.inventory.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class RegistroUiState {
    object Idle : RegistroUiState()
    object Loading : RegistroUiState()
    object Success : RegistroUiState()
    data class Error(val mensaje: String) : RegistroUiState()
}

class RegistroViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<RegistroUiState>(RegistroUiState.Idle)
    val uiState: StateFlow<RegistroUiState> = _uiState.asStateFlow()

    fun registrar(nombre: String, correo: String, pass: String, passConfirm: String) {
        if (nombre.isBlank() || correo.isBlank() || pass.isBlank()) {
            _uiState.value = RegistroUiState.Error("Todos los campos son obligatorios")
            return
        }
        if (pass != passConfirm) {
            _uiState.value = RegistroUiState.Error("Las contraseñas no coinciden")
            return
        }
        if (pass.length < 8) {
            _uiState.value = RegistroUiState.Error("La contraseña debe tener al menos 8 caracteres")
            return
        }

        viewModelScope.launch {
            _uiState.value = RegistroUiState.Loading
            val resultado = authRepository.registrar(nombre, correo, pass)
            resultado.fold(
                onSuccess = {
                    _uiState.value = RegistroUiState.Success
                },
                onFailure = { error ->
                    _uiState.value = RegistroUiState.Error(error.localizedMessage ?: "Error al registrar usuario")
                }
            )
        }
    }

    fun resetState() {
        _uiState.value = RegistroUiState.Idle
    }
}
