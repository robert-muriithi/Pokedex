package com.robert.detail

import app.cash.turbine.test
import com.robert.domain.model.PokemonDetails
import com.robert.domain.model.PokemonStats
import com.robert.domain.usecase.GetPokemonDetailUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getPokemonDetailUseCase: GetPokemonDetailUseCase
    private lateinit var viewModel: DetailsViewModel

    private val testPokemonDetails = PokemonDetails(
        id = 25,
        name = "pikachu",
        height = 4,
        weight = 60,
        baseExperience = 112,
        imageUrl = "https://example.com/pikachu.png",
        types = persistentListOf("Electric"),
        abilities = persistentListOf("Static", "Lightning Rod"),
        stats = persistentListOf(
            PokemonStats("hp", 35, 0),
            PokemonStats("attack", 55, 0),
            PokemonStats("defense", 40, 0)
        )
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getPokemonDetailUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading`() = runTest {
        viewModel = DetailsViewModel(getPokemonDetailUseCase)

        assertEquals(DetailsUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `loadPokemonDetail success updates state to Success`() = runTest {
        coEvery { getPokemonDetailUseCase("pikachu") } returns Result.success(testPokemonDetails)

        viewModel = DetailsViewModel(getPokemonDetailUseCase)
        viewModel.loadPokemonDetail("pikachu")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is DetailsUiState.Success)
        assertEquals(testPokemonDetails, (state as DetailsUiState.Success).pokemonDetails)
    }

    @Test
    fun `loadPokemonDetail failure updates state to Error`() = runTest {
        val errorMessage = "Network error"
        coEvery { getPokemonDetailUseCase("unknown") } returns Result.failure(Exception(errorMessage))

        viewModel = DetailsViewModel(getPokemonDetailUseCase)
        viewModel.loadPokemonDetail("unknown")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is DetailsUiState.Error)
        assertEquals(errorMessage, (state as DetailsUiState.Error).message)
    }

    @Test
    fun `loadPokemonDetail with null error message shows Unknown error`() = runTest {
        coEvery { getPokemonDetailUseCase("unknown") } returns Result.failure(Exception())

        viewModel = DetailsViewModel(getPokemonDetailUseCase)
        viewModel.loadPokemonDetail("unknown")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is DetailsUiState.Error)
        assertEquals("Unknown error", (state as DetailsUiState.Error).message)
    }

    @Test
    fun `loadPokemonDetail emits Loading then Success`() = runTest {
        coEvery { getPokemonDetailUseCase("pikachu") } returns Result.success(testPokemonDetails)
        
        viewModel = DetailsViewModel(getPokemonDetailUseCase)
        
        viewModel.uiState.test {
            assertEquals(DetailsUiState.Loading, awaitItem())
            
            viewModel.loadPokemonDetail("pikachu")
            testDispatcher.scheduler.advanceUntilIdle()
            

            val successState = awaitItem()
            assertTrue(successState is DetailsUiState.Success)
            
            cancelAndIgnoreRemainingEvents()
        }
    }
}

