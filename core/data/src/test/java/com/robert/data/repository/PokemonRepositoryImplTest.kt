package com.robert.data.repository

import com.google.common.truth.Truth.assertThat
import com.robert.data.local.dao.PokemonDao
import com.robert.data.local.database.PokemonDatabase
import com.robert.data.local.entity.PokemonEntity
import com.robert.network.api.PokemonApiService
import com.robert.network.dto.AbilityDto
import com.robert.network.dto.AbilitySlotDto
import com.robert.network.dto.OfficialArtworkDto
import com.robert.network.dto.OtherSpritesDto
import com.robert.network.dto.PokemonDetailResponse
import com.robert.network.dto.SpritesDto
import com.robert.network.dto.StatDto
import com.robert.network.dto.StatNameDto
import com.robert.network.dto.TypeDto
import com.robert.network.dto.TypeSlotDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class PokemonRepositoryImplTest {

    private lateinit var apiService: PokemonApiService
    private lateinit var database: PokemonDatabase
    private lateinit var pokemonDao: PokemonDao
    private lateinit var repository: PokemonRepositoryImpl

    private val testPokemonDetailResponse = PokemonDetailResponse(
        id = 25,
        name = "pikachu",
        height = 4,
        weight = 60,
        baseExperience = 112,
        sprites = SpritesDto(
            frontDefault = "https://example.com/pikachu.png",
            other = OtherSpritesDto(
                officialArtwork = OfficialArtworkDto(
                    frontDefault = "https://example.com/pikachu-artwork.png"
                )
            )
        ),
        types = listOf(
            TypeSlotDto(slot = 1, type = TypeDto(name = "electric", url = ""))
        ),
        abilities = listOf(
            AbilitySlotDto(ability = AbilityDto(name = "static", url = ""), isHidden = false)
        ),
        stats = listOf(
            StatDto(baseStat = 35, effort = 0, stat = StatNameDto(name = "hp", url = ""))
        )
    )

    @Before
    fun setup() {
        apiService = mockk()
        database = mockk()
        pokemonDao = mockk()

        every { database.pokemonDao() } returns pokemonDao
        every { database.remoteKeyDao() } returns mockk(relaxed = true)

        repository = PokemonRepositoryImpl(apiService, database)
    }

    @Test
    fun `getPokemonDetail returns success when API call succeeds`() = runTest {
        coEvery { apiService.getPokemonDetail("pikachu") } returns testPokemonDetailResponse

        val result = repository.getPokemonDetail("pikachu")

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()?.name).isEqualTo("pikachu")
        assertThat(result.getOrNull()?.id).isEqualTo(25)
    }

    @Test
    fun `getPokemonDetail returns failure when API call fails`() = runTest {
        coEvery { apiService.getPokemonDetail("unknown") } throws Exception("Not found")

        val result = repository.getPokemonDetail("unknown")

        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `searchPokemon returns local result when found in database`() = runTest {
        val localPokemon = PokemonEntity(
            name = "pikachu",
            url = "https://pokeapi.co/api/v2/pokemon/25/",
            page = 0
        )
        coEvery { pokemonDao.searchPokemon("pikachu") } returns localPokemon

        val result = repository.searchPokemon("pikachu")

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()?.name).isEqualTo("pikachu")

        // Should not call API when found locally
        coVerify(exactly = 0) { apiService.getPokemonDetail(any()) }
    }

    @Test
    fun `searchPokemon calls API when not found in database`() = runTest {
        coEvery { pokemonDao.searchPokemon("pikachu") } returns null
        coEvery { apiService.getPokemonDetail("pikachu") } returns testPokemonDetailResponse

        val result = repository.searchPokemon("pikachu")

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()?.name).isEqualTo("pikachu")

        coVerify(exactly = 1) { apiService.getPokemonDetail("pikachu") }
    }

    @Test
    fun `searchPokemon returns failure when not found locally and API fails`() = runTest {
        coEvery { pokemonDao.searchPokemon("unknown") } returns null
        coEvery { apiService.getPokemonDetail("unknown") } throws Exception("404 Not Found")

        val result = repository.searchPokemon("unknown")

        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `getPokemonDetail maps types correctly`() = runTest {
        coEvery { apiService.getPokemonDetail("pikachu") } returns testPokemonDetailResponse

        val result = repository.getPokemonDetail("pikachu")

        assertThat(result.getOrNull()?.types).contains("Electric")
    }

    @Test
    fun `getPokemonDetail maps abilities correctly`() = runTest {
        coEvery { apiService.getPokemonDetail("pikachu") } returns testPokemonDetailResponse

        val result = repository.getPokemonDetail("pikachu")

        assertThat(result.getOrNull()?.abilities).contains("Static")
    }

    @Test
    fun `getPokemonDetail uses official artwork when available`() = runTest {
        coEvery { apiService.getPokemonDetail("pikachu") } returns testPokemonDetailResponse

        val result = repository.getPokemonDetail("pikachu")

        assertThat(result.getOrNull()?.imageUrl).isEqualTo("https://example.com/pikachu-artwork.png")
    }
}

