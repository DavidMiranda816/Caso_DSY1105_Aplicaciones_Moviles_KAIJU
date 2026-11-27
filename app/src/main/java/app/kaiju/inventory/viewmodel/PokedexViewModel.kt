package app.kaiju.inventory.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.kaiju.inventory.data.model.PokemonDto
import app.kaiju.inventory.repository.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PokedexUiState(
    val cargando: Boolean = false,
    val pokemon: PokemonDto? = null,
    val error: String? = null
)

class PokedexViewModel(private val repository: PokemonRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(PokedexUiState())
    val uiState = _uiState.asStateFlow()

    fun buscarPokemon(nombre: String) {
        if (nombre.isBlank()) return
        _uiState.update { it.copy(cargando = true, error = null, pokemon = null) }

        viewModelScope.launch {
            try {
                val resultado = repository.buscar(nombre)
                _uiState.update { it.copy(cargando = false, pokemon = resultado) }
            } catch (e: Exception) {
                _uiState.update { it.copy(cargando = false, error = "No se encontró el Pokémon. Revisa tu internet.") }
            }
        }
    }
}