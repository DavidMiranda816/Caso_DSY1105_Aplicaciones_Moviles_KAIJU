package app.kaiju.inventory.data.remote

import app.kaiju.inventory.data.model.PokemonDto
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApi {
    @GET("pokemon/{nombre}")
    suspend fun obtenerPokemon(@Path("nombre") nombre: String): PokemonDto
}