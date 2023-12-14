package com.example.tictactoe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val game = Game()

    private val _board = MutableStateFlow(game.getBoard())
    val board: Flow<Array<Array<Player>>> = _board

    private val _winner = MutableStateFlow<Player?>(null)
    val winner: Flow<Player?> = _winner

    private val _draw = MutableStateFlow(false)
    val draw: Flow<Boolean> = _draw

    private val _currentPlayer = MutableStateFlow(Player.PLAYER1)
    val currentPlayer: Flow<Player> = _currentPlayer

    private val _error = MutableStateFlow(false)
    val error: Flow<Boolean> = _error

    fun updateBoard(x: Int, y: Int) {
        if (!game.isValidPosition(x, y)) {
            _error.value = true
            return
        }
        _error.value = false

        game.makeMove(x, y, _currentPlayer.value)
        _board.value = game.getBoard().copyOf()
        updatePlayer()
        updateDraw()
        updateWinner()
    }

    private fun updateDraw() {
        _draw.value = game.isGameDraw()
    }

    private fun updateWinner() {
        game.hasWinner().let { player ->
            if (player != Player.EMPTY) {
                _winner.value = player
            }
        }
    }

    private fun updatePlayer() {
        _currentPlayer.value =
            if (_currentPlayer.value == Player.PLAYER1) Player.PLAYER2 else Player.PLAYER1
    }

    fun changeBoard() {
        game.changeBoard()
        reset()
    }

    fun reset() {
        game.reset()
        _currentPlayer.value = Player.PLAYER1
        _board.value = game.getBoard()
        _winner.value = null
        _draw.value = false
        _error.value = false
    }

}