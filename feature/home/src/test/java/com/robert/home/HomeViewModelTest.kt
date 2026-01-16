package com.robert.home

import androidx.paging.PagingData
import app.cash.turbine.test
import com.robert.domain.model.Pokemon
import com.robert.domain.usecase.GetPokemonListUseCase
import com.robert.domain.usecase.SearchPokemonUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getPokemonListUseCase: GetPokemonListUseCase
    private lateinit var searchPokemonUseCase: SearchPokemonUseCase
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getPokemonListUseCase = mockk()
        searchPokemonUseCase = mockk()

        coEvery { getPokemonListUseCase() } returns flowOf(PagingData.empty())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(): HomeViewModel {
        return HomeViewModel(getPokemonListUseCase, searchPokemonUseCase)
    }

    @Test
    fun `initial search state is Idle`() = runTest {
        viewModel = createViewModel()

        assertEquals(SearchUiState.Idle, viewModel.searchState.value)
    }

    @Test
    fun `initial search query is empty`() = runTest {
        viewModel = createViewModel()

        assertEquals("", viewModel.searchQuery.value)
    }

    @Test
    fun `clearSearch resets query and state to Idle`() = runTest {
        viewModel = createViewModel()

        viewModel.onSearchQueryChange("pikachu")
        viewModel.clearSearch()

        assertEquals("", viewModel.searchQuery.value)
        assertEquals(SearchUiState.Idle, viewModel.searchState.value)
    }

    @Test
    fun `empty query resets state to Idle`() = runTest {
        val viewModel = createViewModel()
        viewModel.onSearchQueryChange("pikachu")
        viewModel.onSearchQueryChange("")

        assertEquals(SearchUiState.Idle, viewModel.searchState.value)
    }

    @Test
    fun `search success returns Success state with pokemon`() = runTest {
        val pokemon = Pokemon(
            id = 25,
            name = "pikachu",
            url = "https://pokeapi.co/api/v2/pokemon/25/",
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/25.png"
        )
        coEvery { searchPokemonUseCase("pikachu") } returns Result.success(pokemon)

        viewModel = createViewModel()
        viewModel.onSearchQueryChange("pikachu")

        // Advance past debounce (500ms)
        testDispatcher.scheduler.advanceTimeBy(600)
        testDispatcher.scheduler.runCurrent()

        val state = viewModel.searchState.value
        assertTrue(state is SearchUiState.Success)
        assertEquals(pokemon, (state as SearchUiState.Success).pokemon)
    }

    @Test
    fun `search failure with 404 returns descriptive error message`() = runTest {
        coEvery { searchPokemonUseCase("unknownpokemon") } returns Result.failure(
            Exception("HTTP 404 Not Found")
        )

        viewModel = createViewModel()
        viewModel.onSearchQueryChange("unknownpokemon")

        testDispatcher.scheduler.advanceTimeBy(600)
        testDispatcher.scheduler.runCurrent()

        val state = viewModel.searchState.value
        assertTrue(state is SearchUiState.Error)
        val message = (state as SearchUiState.Error).message
        assertTrue(message is UiText.StringResource)
        assertEquals(R.string.no_pok_mon_found_matching, (message as UiText.StringResource).resId)
    }

    @Test
    fun `search failure with network error returns appropriate message`() = runTest {
        coEvery { searchPokemonUseCase("pikachu") } returns Result.failure(
            Exception("Unable to resolve host")
        )

        viewModel = createViewModel()
        viewModel.onSearchQueryChange("pikachu")

        testDispatcher.scheduler.advanceTimeBy(600)
        testDispatcher.scheduler.runCurrent()

        val state = viewModel.searchState.value
        assertTrue(state is SearchUiState.Error)
        val message = (state as SearchUiState.Error).message
        assertTrue(message is UiText.StringResource)
        assertEquals(R.string.error_no_internet, (message as UiText.StringResource).resId)
    }


    @Test
    fun `search shows Loading state while fetching`() = runTest {
        val pokemon = Pokemon(
            id = 25,
            name = "pikachu",
            url = "https://pokeapi.co/api/v2/pokemon/25/",
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/25.png"
        )
        coEvery { searchPokemonUseCase("pikachu") } returns Result.success(pokemon)

        viewModel = createViewModel()

        viewModel.searchState.test {
            assertEquals(SearchUiState.Idle, awaitItem())

            viewModel.onSearchQueryChange("pikachu")

            // Advance past debounce
            testDispatcher.scheduler.advanceTimeBy(600)

            assertEquals(SearchUiState.Loading, awaitItem())

            testDispatcher.scheduler.runCurrent()

            val successState = awaitItem()
            assertTrue(successState is SearchUiState.Success)

            cancelAndIgnoreRemainingEvents()
        }
    }
}

