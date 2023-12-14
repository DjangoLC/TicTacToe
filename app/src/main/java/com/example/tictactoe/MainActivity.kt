package com.example.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChangeCircle
import androidx.compose.material.icons.sharp.ChangeCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.tictactoe.ui.theme.TictacToeTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val mainVieModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            TictacToeTheme {
                val boardGame = mainVieModel.board.collectAsState(initial = emptyArray())
                val winner = mainVieModel.winner.collectAsState(initial = null)
                val currentPlayer =
                    mainVieModel.currentPlayer.collectAsState(initial = Player.PLAYER1)
                val draw = mainVieModel.draw.collectAsState(initial = false)
                val error = mainVieModel.error.collectAsState(initial = false)

                val scope = rememberCoroutineScope()
                val snackbarHostState = remember { SnackbarHostState() }
                Scaffold(
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState)
                    }
                ) { _ ->
                    BuildScreen(
                        boardGame = boardGame.value,
                        currentPlayer = currentPlayer.value,
                        winner = winner.value,
                        draw = draw.value,
                        onBoardClick = { x, y ->
                            mainVieModel.updateBoard(x, y)
                        },
                        onDialogClick = {
                            mainVieModel.reset()
                        },
                        onChangeBoardClick = {
                            mainVieModel.changeBoard()
                        })
                }

                if (error.value) {
                    LaunchedEffect(key1 = null) {
                        scope.launch {
                            snackbarHostState.showSnackbar("Invalid position")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BuildScreen(
    boardGame: Array<Array<Player>>,
    currentPlayer: Player,
    winner: Player?,
    draw: Boolean,
    onBoardClick: (Int, Int) -> Unit,
    onDialogClick: () -> Unit,
    onChangeBoardClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Image(
                painter = painterResource(id = R.drawable.player_1),
                contentDescription = null,
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.player_2),
                contentDescription = null,
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(200.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.bg_button),
                contentDescription = null
            )
            Text(
                text = "Your turn player: \n${currentPlayer.name}",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(4.dp)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(16.dp))
            BoardGame(board = boardGame, onClick = { x, y ->
                onBoardClick.invoke(x, y)
            })
            Spacer(modifier = Modifier.height(16.dp))
            ChangeBoardType(onChangeBoardClick)
            Spacer(modifier = Modifier.height(16.dp))
            if (winner != null) {
                Dialog(
                    message = "Winner ${winner.name}",
                    iconId = R.drawable.winner
                ) {
                    onDialogClick.invoke()
                }
            }

            if (draw) {
                Dialog(message = "No winner", iconId = R.drawable.drawn) {
                    onDialogClick.invoke()
                }
            }
        }
    }
}

@Composable
fun Dialog(message: String, iconId: Int, onButtonClick: () -> Unit) {
    Dialog(
        onDismissRequest = {

        }
    ) {
        Card(
            modifier = Modifier
                .height(200.dp)
                .width(200.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = iconId),
                    contentDescription = null,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = message)
                OutlinedButton(onClick = {
                    onButtonClick.invoke()
                }, content = {
                    Text(text = "OK")
                })
            }
        }

    }
}

@Composable
fun BoardGame(board: Array<Array<Player>>, onClick: (Int, Int) -> Unit) {
    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.Center) {
            BackBoard()
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                for (i in board.indices) {
                    GameRow(i, arr = board[i], onClick)
                }
            }
        }
    }
}

@Composable
fun ChangeBoardType(onChangeBoardClick: () -> Unit) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier
        .width(200.dp)
        .clickable {
            onChangeBoardClick.invoke()
        }) {
        Image(painter = painterResource(id = R.drawable.bg_button), contentDescription = null)
        Text(text = "Change board", modifier = Modifier.padding(4.dp))
    }
}

@Composable
fun GameRow(x: Int, arr: Array<Player>, onClick: (Int, Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        for (e in arr.indices) {
            val vector = if (arr[e] == Player.PLAYER1) {
                R.drawable.player2
            } else if (arr[e] == Player.PLAYER2) {
                R.drawable.player1
            } else {
                R.drawable.bg_piece
            }
            Item(
                vectorId = vector, x = x, y = e, onClick = onClick
            )
        }
    }
}

@Composable
fun Item(vectorId: Int?, x: Int, y: Int, onClick: (Int, Int) -> Unit) {
    Box(
        modifier = Modifier
            .clickable {
                onClick(x, y)
                println("($x, $y)")
            }, contentAlignment = Alignment.Center
    ) {
        vectorId?.let {
            Image(
                painter = painterResource(id = vectorId),
                contentDescription = null,
            )
        }
    }
}

@Composable
fun BackBoard() {
    Image(
        painter = painterResource(id = R.drawable.background),
        contentDescription = null,
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview
@Composable
fun GreetingPreview() {
    BuildScreen(
        boardGame = arrayOf(
            arrayOf(Player.PLAYER1, Player.EMPTY, Player.PLAYER2),
            arrayOf(Player.EMPTY, Player.PLAYER2, Player.EMPTY),
            arrayOf(Player.PLAYER2, Player.EMPTY, Player.PLAYER1),
        ),
        currentPlayer = Player.PLAYER1,
        winner = null,
        draw = false,
        onBoardClick = { i: Int, i1: Int -> },
        onDialogClick = {},
        onChangeBoardClick = {})
}

