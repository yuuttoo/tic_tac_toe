package com.example.tic_tac_toe.game

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
class GameViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: GameViewModel
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var repository: GameRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        val application = mock(Application::class.java)
        repository = mock(GameRepository::class.java)
        `when`(repository.userWins).thenReturn(flowOf(0))
        viewModel = GameViewModel(application, repository, false)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test user win`() = runTest {
        viewModel.onCellClicked(0, 0) // X
        viewModel.onCellClicked(0, 1) // X
        viewModel.onCellClicked(0, 2) // X
        assertEquals(GameStatus.X_WINS, viewModel.gameState.value.gameStatus)
    }

    @Test
    fun `test AI win`() = runTest {
        val board = listOf(
            listOf('O', 'O', 'O'),
            listOf('X', 'X', null),
            listOf('X', null, null)
        )
        val gameState = GameState(board = board, gameStatus = GameStatus.O_WINS)
        assertEquals(GameStatus.O_WINS, gameState.gameStatus)
    }

    @Test
    fun `test draw`() = runTest {
        val board = listOf(
            listOf('X', 'O', 'X'),
            listOf('X', 'O', 'O'),
            listOf('O', 'X', 'X')
        )
        val gameState = GameState(board = board, gameStatus = GameStatus.DRAW)
        assertEquals(GameStatus.DRAW, gameState.gameStatus)
    }

    @Test
    fun `test reset game`() = runTest {
        viewModel.onCellClicked(0, 0)
        viewModel.resetGame()
        assertEquals(GameState(userWins = viewModel.gameState.value.userWins), viewModel.gameState.value)
    }
}