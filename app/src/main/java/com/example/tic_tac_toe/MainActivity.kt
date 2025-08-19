package com.example.tic_tac_toe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tic_tac_toe.game.GameRepository
import com.example.tic_tac_toe.game.GameStatus
import com.example.tic_tac_toe.game.GameViewModel
import com.example.tic_tac_toe.game.GameViewModelFactory
import com.example.tic_tac_toe.ui.theme.TictactoeTheme

class MainActivity : ComponentActivity() {
    private val repository by lazy { GameRepository(this) }
    private val viewModel: GameViewModel by viewModels {
        GameViewModelFactory(application, repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TictactoeTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    TicTacToeGame(viewModel)
                }
            }
        }
    }
}

@Composable
fun TicTacToeGame(viewModel: GameViewModel) {
    val gameState by viewModel.gameState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Tic-Tac-Toe", fontSize = 32.sp, modifier = Modifier.padding(bottom = 16.dp))
        Text(text = "Your Wins: ${gameState.userWins}", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))
        Text(text = getStatusMessage(gameState.gameStatus), fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        Board(board = gameState.board, onCellClicked = { row, col -> viewModel.onCellClicked(row, col) })

        Button(onClick = { viewModel.resetGame() }, modifier = Modifier.padding(top = 16.dp)) {
            Text(text = "Reset Game")
        }
    }
}

@Composable
fun Board(board: List<List<Char?>>, onCellClicked: (Int, Int) -> Unit) {
    Column {
        for (i in board.indices) {
            Row {
                for (j in board[i].indices) {
                    Cell(cell = board[i][j], onCellClicked = { onCellClicked(i, j) })
                }
            }
        }
    }
}

@Composable
fun Cell(cell: Char?, onCellClicked: () -> Unit) {
    Button(
        onClick = onCellClicked,
        modifier = Modifier
            .padding(4.dp)
            .size(64.dp),
        enabled = cell == null
    ) {
        Text(text = cell?.toString() ?: "", fontSize = 32.sp)
    }
}

private fun getStatusMessage(status: GameStatus): String {
    return when (status) {
        GameStatus.IN_PROGRESS -> "Your Turn"
        GameStatus.X_WINS -> "You Win!"
        GameStatus.O_WINS -> "AI Wins!"
        GameStatus.DRAW -> "Draw!"
    }
}
