package com.robert.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.robert.database.dao.PokemonDao
import com.robert.database.entity.PokemonEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class PokemonDaoTest {

    private lateinit var database: PokemonDatabase
    private lateinit var pokemonDao: PokemonDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            PokemonDatabase::class.java
        ).allowMainThreadQueries().build()

        pokemonDao = database.pokemonDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndRetrievePokemon() = runTest {
        val pokemon = listOf(
            PokemonEntity(name = "bulbasaur", url = "https://pokeapi.co/api/v2/pokemon/1/", page = 0),
            PokemonEntity(name = "ivysaur", url = "https://pokeapi.co/api/v2/pokemon/2/", page = 0),
            PokemonEntity(name = "venusaur", url = "https://pokeapi.co/api/v2/pokemon/3/", page = 0)
        )

        pokemonDao.insertAll(pokemon)

        val result = pokemonDao.searchPokemonList("saur")

        assertThat(result).hasSize(3)
        assertThat(result.map { it.name }).containsExactly("bulbasaur", "ivysaur", "venusaur")
    }

    @Test
    fun searchPokemonByName() = runTest {
        val pokemon = listOf(
            PokemonEntity(name = "pikachu", url = "https://pokeapi.co/api/v2/pokemon/25/", page = 0),
            PokemonEntity(name = "raichu", url = "https://pokeapi.co/api/v2/pokemon/26/", page = 0),
            PokemonEntity(name = "pichu", url = "https://pokeapi.co/api/v2/pokemon/172/", page = 0)
        )

        pokemonDao.insertAll(pokemon)

        val result = pokemonDao.searchPokemon("pikachu")

        assertThat(result).isNotNull()
        assertThat(result?.name).isEqualTo("pikachu")
    }

    @Test
    fun searchPokemonPartialMatch() = runTest {
        val pokemon = listOf(
            PokemonEntity(name = "charmander", url = "https://pokeapi.co/api/v2/pokemon/4/", page = 0),
            PokemonEntity(name = "charmeleon", url = "https://pokeapi.co/api/v2/pokemon/5/", page = 0),
            PokemonEntity(name = "charizard", url = "https://pokeapi.co/api/v2/pokemon/6/", page = 0)
        )

        pokemonDao.insertAll(pokemon)

        val result = pokemonDao.searchPokemonList("char")

        assertThat(result).hasSize(3)
    }

    @Test
    fun searchPokemonNotFound() = runTest {
        val pokemon = listOf(
            PokemonEntity(name = "bulbasaur", url = "https://pokeapi.co/api/v2/pokemon/1/", page = 0)
        )

        pokemonDao.insertAll(pokemon)

        val result = pokemonDao.searchPokemon("pikachu")

        assertThat(result).isNull()
    }

    @Test
    fun clearAllRemovesAllPokemon() = runTest {
        val pokemon = listOf(
            PokemonEntity(name = "bulbasaur", url = "https://pokeapi.co/api/v2/pokemon/1/", page = 0),
            PokemonEntity(name = "charmander", url = "https://pokeapi.co/api/v2/pokemon/4/", page = 0),
            PokemonEntity(name = "squirtle", url = "https://pokeapi.co/api/v2/pokemon/7/", page = 0)
        )

        pokemonDao.insertAll(pokemon)
        pokemonDao.clearAll()

        val result = pokemonDao.searchPokemonList("")

        assertThat(result).isEmpty()
    }

    @Test
    fun insertDuplicateReplacesExisting() = runTest {
        val pokemon1 = PokemonEntity(name = "pikachu", url = "https://pokeapi.co/api/v2/pokemon/25/", page = 0)
        val pokemon2 = PokemonEntity(name = "pikachu", url = "https://pokeapi.co/api/v2/pokemon/25/", page = 1)

        pokemonDao.insertAll(listOf(pokemon1))
        pokemonDao.insertAll(listOf(pokemon2))

        val result = pokemonDao.searchPokemon("pikachu")

        assertThat(result).isNotNull()
        assertThat(result?.page).isEqualTo(1)
    }
}
