package com.robert.detail

import app.cash.turbine.test
import com.robert.domain.model.PokemonDetail
import com.robert.domain.model.PokemonStat
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
    private lateinit var viewModel: DetailViewModel

    private val testPokemonDetail = PokemonDetail(
        id = 25,
        name = "pikachu",
        height = 4,
        weight = 60,
        baseExperience = 112,
        imageUrl = "https://example.com/pikachu.png",
        types = persistentListOf("Electric"),
        abilities = persistentListOf("Static", "Lightning Rod"),
        stats = persistentListOf(
            PokemonStat("hp", 35, 0),
            PokemonStat("attack", 55, 0),
            PokemonStat("defense", 40, 0)
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
        viewModel = DetailViewModel(getPokemonDetailUseCase)

        assertEquals(DetailUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `loadPokemonDetail success updates state to Success`() = runTest {
        coEvery { getPokemonDetailUseCase("pikachu") } returns Result.success(testPokemonDetail)

        viewModel = DetailViewModel(getPokemonDetailUseCase)
        viewModel.loadPokemonDetail("pikachu")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is DetailUiState.Success)
        assertEquals(testPokemonDetail, (state as DetailUiState.Success).pokemonDetail)
    }

    @Test
    fun `loadPokemonDetail failure updates state to Error`() = runTest {
        val errorMessage = "Network error"
        coEvery { getPokemonDetailUseCase("unknown") } returns Result.failure(Exception(errorMessage))

        viewModel = DetailViewModel(getPokemonDetailUseCase)
        viewModel.loadPokemonDetail("unknown")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is DetailUiState.Error)
        assertEquals(errorMessage, (state as DetailUiState.Error).message)
    }

    @Test
    fun `loadPokemonDetail with null error message shows Unknown error`() = runTest {
        coEvery { getPokemonDetailUseCase("unknown") } returns Result.failure(Exception())

        viewModel = DetailViewModel(getPokemonDetailUseCase)
        viewModel.loadPokemonDetail("unknown")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is DetailUiState.Error)
        assertEquals("Unknown error", (state as DetailUiState.Error).message)
    }

    @Test
    fun `loadPokemonDetail emits Loading then Success`() = runTest {
        coEvery { getPokemonDetailUseCase("pikachu") } returns Result.success(testPokemonDetail)
        
        viewModel = DetailViewModel(getPokemonDetailUseCase)
        
        viewModel.uiState.test {
            assertEquals(DetailUiState.Loading, awaitItem())
            
            viewModel.loadPokemonDetail("pikachu")
            testDispatcher.scheduler.advanceUntilIdle()
            

            val successState = awaitItem()
            assertTrue(successState is DetailUiState.Success)
            
            cancelAndIgnoreRemainingEvents()
        }
    }
}

