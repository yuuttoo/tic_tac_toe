package com.example.tic_tac_toe.game

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(application: Application, private val repository: GameRepository, private val aiEnabled: Boolean = true) : AndroidViewModel(application) {

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    init {
        repository.userWins.onEach { wins ->
            _gameState.update { it.copy(userWins = wins) }
        }.launchIn(viewModelScope)
    }

    fun onCellClicked(row: Int, col: Int) {
        if (_gameState.value.board[row][col] == null && _gameState.value.gameStatus == GameStatus.IN_PROGRESS) {
            val newBoard = _gameState.value.board.map { it.toMutableList() }.toMutableList()
            newBoard[row][col] = _gameState.value.currentPlayer
            val nextPlayer = if (_gameState.value.currentPlayer == 'X') 'O' else 'X'
            _gameState.update { it.copy(board = newBoard, currentPlayer = nextPlayer) }
            checkForWin()
            if (_gameState.value.gameStatus == GameStatus.IN_PROGRESS && aiEnabled) {
                aiMove()
            }
        }
    }

    private fun aiMove() {
        if (_gameState.value.gameStatus != GameStatus.IN_PROGRESS) return
        val emptyCells = mutableListOf<Pair<Int, Int>>()
        for (i in 0..2) {
            for (j in 0..2) {
                if (_gameState.value.board[i][j] == null) {
                    emptyCells.add(Pair(i, j))
                }
            }
        }

        if (emptyCells.isNotEmpty()) {
            val (row, col) = emptyCells.random()
            val newBoard = _gameState.value.board.map { it.toMutableList() }.toMutableList()
            newBoard[row][col] = 'O'
            _gameState.update { it.copy(board = newBoard, currentPlayer = 'X') }
            checkForWin()
        }
    }

    private fun checkForWin() {
        val board = _gameState.value.board

        // Check rows
        for (i in 0..2) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != null) {
                endGame(board[i][0])
                return
            }
        }

        // Check columns
        for (i in 0..2) {
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] != null) {
                endGame(board[0][i])
                return
            }
        }

        // Check diagonals
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != null) {
            endGame(board[0][0])
            return
        }
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != null) {
            endGame(board[0][2])
            return
        }

        if (board.flatten().all { it != null }) {
            _gameState.update { it.copy(gameStatus = GameStatus.DRAW) }
        }
    }

    private fun endGame(winner: Char?) {
        when (winner) {
            'X' -> {
                _gameState.update { it.copy(gameStatus = GameStatus.X_WINS) }
                viewModelScope.launch {
                    repository.incrementUserWins()
                }
            }
            'O' -> _gameState.update { it.copy(gameStatus = GameStatus.O_WINS) }
            else -> _gameState.update { it.copy(gameStatus = GameStatus.DRAW) }
        }
    }

    fun resetGame() {
        _gameState.update { GameState(userWins = it.userWins) }
    }
}