package com.example.tictactoe

class Game {

    private var board = Player.newBoard()
    private var winner = Player.EMPTY
    private var boardType = BoardType.X3

    fun changeBoard() {
        boardType = when (boardType) {
            BoardType.X2 -> BoardType.X3
            BoardType.X3 -> BoardType.X4
            BoardType.X4 -> BoardType.X2
        }
        board = Player.newBoard(this.boardType)
    }

    fun makeMove(x: Int, y: Int, player: Player) {
        // Update row and column
        if (isValidPosition(x, y)) {
            updateBoardState(x, y, player)
        }
    }

    private fun updateBoardState(x: Int, y: Int, player: Player) {
        board[x][y] = player
    }

    fun isValidPosition(x: Int, y: Int): Boolean {
        return board[x][y] == Player.EMPTY
    }

    fun hasWinner(): Player {

        // Check all rows and columns
        for (i in board.indices) {

            val rowWinner = checkRowWinner(i)
            if (rowWinner != Player.EMPTY) {
                winner = rowWinner
            }

            val columnWinner = checkColumnWinner(i)
            if (columnWinner != Player.EMPTY) {
                winner = columnWinner
            }
        }

        // Check diagonals
        val firstDiagonalWinner = checkFirstDiagonalWinner()
        if (firstDiagonalWinner != Player.EMPTY) {
            winner = firstDiagonalWinner
        }

        val secondDiagonalWinner = checkSecondDiagonalWinner()
        if (secondDiagonalWinner != Player.EMPTY) {
            winner = secondDiagonalWinner
        }

        return winner
    }

    private fun checkFirstDiagonalWinner(): Player {
        var currentPlayer = Player.EMPTY
        var prevPlayer = Player.EMPTY

        for (i in board.indices) {
            for (j in board[i].indices) {
                if (i == j) {
                    //base case
                    if (i == 0 && j == 0) {
                        prevPlayer = board[i][j]
                    }

                    currentPlayer = board[i][j]

                    if (currentPlayer != prevPlayer) {
                        return Player.EMPTY
                    }
                }
            }
        }
        return currentPlayer
    }

    private fun checkSecondDiagonalWinner(): Player {
        var currentPlayer = Player.EMPTY
        var prevPlayer = Player.EMPTY

        for (i in board.indices) {
            for (j in board[i].indices) {
                if (j == board[i].size - 1 - i) {
                    //base case
                    if (i == 0 && j == board[i].size - 1) {
                        prevPlayer = board[i][j]
                    }

                    currentPlayer = board[i][j]

                    if (currentPlayer != prevPlayer) {
                        return Player.EMPTY
                    }
                }
            }
        }
        return currentPlayer
    }

    //verify is a row has a winner it will iterate on the size of the current row arr
    private fun checkRowWinner(row: Int): Player {
        var currentPlayer = Player.EMPTY
        var prevPlayer = Player.EMPTY
        for (i in board[row].indices) {
            //base case
            if (board[row][i] == Player.EMPTY) {
                return board[row][i]
            }
            //init when index 0
            if (i == 0) {
                prevPlayer = board[row][i]
            }

            currentPlayer = board[row][i]

            // if current player is different from previous
            // it means that the row is draw or incomplete
            if (currentPlayer != prevPlayer) {
                return Player.EMPTY
            }
        }
        return currentPlayer
    }

    private fun checkColumnWinner(column: Int): Player {
        var currentPlayer = Player.EMPTY
        var prevPlayer = Player.EMPTY
        for (i in board[column].indices) {
            //base case
            if (board[i][column] == Player.EMPTY) {
                return board[i][column]
            }
            //init when index 0
            if (i == 0) {
                prevPlayer = board[i][column]
            }

            currentPlayer = board[i][column]

            // if current player is different from previous
            // it means that the row is draw or incomplete
            if (currentPlayer != prevPlayer) {
                return Player.EMPTY
            }
        }
        return currentPlayer
    }

    fun isGameDraw(): Boolean {
        if (board.any { it.any { it == Player.EMPTY } }) return false
        return winner == Player.EMPTY
    }

    fun reset() {
        board = Player.newBoard(this.boardType)
        winner = Player.EMPTY
    }

    fun getBoard() = board

}

enum class BoardType {
    X2,
    X3,
    X4
}

enum class Player {
    PLAYER1,
    PLAYER2,
    EMPTY;

    companion object {
        fun newBoard(boardType: BoardType = BoardType.X3): Array<Array<Player>> {
            when (boardType) {
                BoardType.X2 -> {
                    return arrayOf(
                        arrayOf(EMPTY, EMPTY),
                        arrayOf(EMPTY, EMPTY),
                    )
                }

                BoardType.X3 -> {
                    return arrayOf(
                        arrayOf(EMPTY, EMPTY, EMPTY),
                        arrayOf(EMPTY, EMPTY, EMPTY),
                        arrayOf(EMPTY, EMPTY, EMPTY)
                    )
                }

                BoardType.X4 -> {
                    return arrayOf(
                        arrayOf(EMPTY, EMPTY, EMPTY, EMPTY),
                        arrayOf(EMPTY, EMPTY, EMPTY, EMPTY),
                        arrayOf(EMPTY, EMPTY, EMPTY, EMPTY),
                        arrayOf(EMPTY, EMPTY, EMPTY, EMPTY)
                    )
                }
            }
        }
    }
}