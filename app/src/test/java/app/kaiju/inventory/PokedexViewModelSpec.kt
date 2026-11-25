package app.kaiju.inventory

import app.kaiju.inventory.data.model.PokemonDto
import app.kaiju.inventory.data.model.PokemonTypeDto
import app.kaiju.inventory.data.model.SpritesDto
import app.kaiju.inventory.data.model.TypeDto
import app.kaiju.inventory.repository.PokemonRepository
import app.kaiju.inventory.viewmodel.PokedexViewModel
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class PokedexViewModelSpec : FunSpec({

    val testDispatcher = UnconfinedTestDispatcher()

    beforeSpec {
        Dispatchers.setMain(testDispatcher)
    }

    afterSpec {
        Dispatchers.resetMain()
    }

    test("PokedexViewModel debe actualizar el uiState con el Pokémon encontrado") {
        val mockRepo = mockk<PokemonRepository>()
        val pokemon = PokemonDto(
            id = 6,
            name = "charizard",
            height = 17,
            weight = 905,
            sprites = SpritesDto("https://example.com/charizard.png"),
            types = listOf(PokemonTypeDto(1, TypeDto("fire")), PokemonTypeDto(2, TypeDto("flying")))
        )

        coEvery { mockRepo.buscar("charizard") } returns pokemon

        val viewModel = PokedexViewModel(mockRepo)
        viewModel.buscarPokemon("charizard")

        val state = viewModel.uiState.value
        state.cargando.shouldBeFalse()
        val pokemonEncontrado = state.pokemon
        pokemonEncontrado.shouldNotBeNull()
        pokemonEncontrado.id shouldBe 6
        pokemonEncontrado.name shouldBe "charizard"
        state.error shouldBe null
    }
})
