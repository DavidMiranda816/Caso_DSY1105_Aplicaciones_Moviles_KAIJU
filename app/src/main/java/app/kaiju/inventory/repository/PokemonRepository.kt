package app.kaiju.inventory.repository

import app.kaiju.inventory.data.model.PokemonDto
import app.kaiju.inventory.data.remote.PokeApi

class PokemonRepository(private val api: PokeApi) {
    suspend fun buscar(entrada: String): PokemonDto {
        val clave = entrada.trim().lowercase()
        return api.obtenerPokemon(clave)
    }
}