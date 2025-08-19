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
        viewModel.onCellClicked(0, 0) // User 'X'
        viewModel.onCellClicked(1, 0) // AI 'O'
        viewModel.onCellClicked(0, 1) // User 'X'
        viewModel.onCellClicked(1, 1) // AI 'O'
        viewModel.onCellClicked(0, 2) // User 'X'
        assertEquals(GameStatus.X_WINS, viewModel.gameState.value.gameStatus)
    }

    @Test
    fun `test AI win`() = runTest {
        viewModel.onCellClicked(0, 0) // User 'X'
        viewModel.onCellClicked(1, 0) // AI 'O'
        viewModel.onCellClicked(0, 1) // User 'X'
        viewModel.onCellClicked(1, 1) // AI 'O'
        viewModel.onCellClicked(2, 2) // User 'X'
        viewModel.onCellClicked(1, 2) // AI 'O'
        assertEquals(GameStatus.O_WINS, viewModel.gameState.value.gameStatus)
    }

    @Test
    fun `test draw`() = runTest {
        viewModel.onCellClicked(0, 0) // X
        viewModel.onCellClicked(0, 1) // O
        viewModel.onCellClicked(0, 2) // X
        viewModel.onCellClicked(1, 1) // O
        viewModel.onCellClicked(1, 0) // X
        viewModel.onCellClicked(1, 2) // O
        viewModel.onCellClicked(2, 1) // X
        viewModel.onCellClicked(2, 0) // O
        viewModel.onCellClicked(2, 2) // X
        assertEquals(GameStatus.DRAW, viewModel.gameState.value.gameStatus)
    }

    @Test
    fun `test reset game`() = runTest {
        viewModel.onCellClicked(0, 0)
        viewModel.resetGame()
        val expectedBoard = listOf(
            listOf(null, null, null),
            listOf(null, null, null),
            listOf(null, null, null)
        )
        assertEquals(expectedBoard, viewModel.gameState.value.board)
        assertEquals(GameStatus.IN_PROGRESS, viewModel.gameState.value.gameStatus)
    }
}