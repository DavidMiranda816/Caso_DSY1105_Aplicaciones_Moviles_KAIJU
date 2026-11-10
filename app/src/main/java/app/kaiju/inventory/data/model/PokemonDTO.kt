package app.kaiju.inventory.data.model

import com.google.gson.annotations.SerializedName

data class PokemonDto(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val sprites: SpritesDto,
    val types: List<PokemonTypeDto>
)

data class SpritesDto(
    @SerializedName("front_default") val frontDefault: String?
)

data class PokemonTypeDto(val slot: Int, val type: TypeDto)
data class TypeDto(val name: String)