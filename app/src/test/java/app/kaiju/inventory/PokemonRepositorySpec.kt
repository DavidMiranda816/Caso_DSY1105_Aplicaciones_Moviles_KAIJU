package app.kaiju.inventory

import app.kaiju.inventory.data.model.PokemonDto
import app.kaiju.inventory.data.model.PokemonTypeDto
import app.kaiju.inventory.data.model.SpritesDto
import app.kaiju.inventory.data.model.TypeDto
import app.kaiju.inventory.data.remote.PokeApi
import app.kaiju.inventory.repository.PokemonRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

class PokemonRepositorySpec : FunSpec({

    test("PokemonRepository debe formatear la entrada en minúsculas y buscar en PokeApi") {
        val mockApi = mockk<PokeApi>()
        val pokemonEsperado = PokemonDto(
            id = 25,
            name = "pikachu",
            height = 4,
            weight = 60,
            sprites = SpritesDto("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png"),
            types = listOf(PokemonTypeDto(1, TypeDto("electric")))
        )

        coEvery { mockApi.obtenerPokemon("pikachu") } returns pokemonEsperado

        val repository = PokemonRepository(mockApi)
        val resultado = repository.buscar("  PIKACHU  ")

        resultado.id shouldBe 25
        resultado.name shouldBe "pikachu"
        resultado.height shouldBe 4
        coVerify(exactly = 1) { mockApi.obtenerPokemon("pikachu") }
    }
})
