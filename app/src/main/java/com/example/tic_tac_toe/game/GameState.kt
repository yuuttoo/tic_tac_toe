package com.example.tic_tac_toe.game

data class GameState(
    val board: List<List<Char?>> = List(3) { List(3) { null } },
    val currentPlayer: Char = 'X',
    val gameStatus: GameStatus = GameStatus.IN_PROGRESS,
    val userWins: Int = 0
)
