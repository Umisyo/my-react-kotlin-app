package Board

import Square.square
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.div
import react.setState

class Board: RComponent<RProps, Board.State>() {
    init {
        state.squares = Array(9) { null }
        state.xIsNext = true
    }

    private fun RBuilder.renderSquare(i: Int) {
        square(value = state.squares[i],
                onClickFunction = { handleClick(i) })
    }

    private fun handleClick(i: Int): Unit? {
        val updateSquares = state.squares.copyOf()

        if (calculateWinner(updateSquares) != null || updateSquares[i] != null) return null

        if (state.xIsNext) updateSquares[i] = "X"
        else updateSquares[i] = "O"

        return setState {
            squares = updateSquares
            xIsNext = !state.xIsNext
        }
    }

    private fun calculateWinner(squares:Array<String?>): String? {
        val lines: Array<Array<Int>> = arrayOf(
                arrayOf(0, 1, 2),
                arrayOf(3, 4, 5),
                arrayOf(6, 7, 8),
                arrayOf(0, 3, 6),
                arrayOf(1, 4, 7),
                arrayOf(2, 5, 8),
                arrayOf(0, 4, 8),
                arrayOf(2, 4, 6)
        )

        for (line in lines) {
            val (a, b, c) = line
            if (squares[a] != null && squares[a] == squares[b] && squares[a] == squares[c]) {
                return squares[a]
            }
        }

        return null

    }

    override fun RBuilder.render() {
        val winner: String? = calculateWinner(state.squares)
        val status = if(winner != null) "Winner: $winner" else ("Next player :" + (if (state.xIsNext) "X" else "O"))

        div {
            div(classes = "status") { + status }
            div(classes = "board-row") {
                renderSquare(0)
                renderSquare(1)
                renderSquare(2)
            }
            div(classes = "board-row") {
                renderSquare(3)
                renderSquare(4)
                renderSquare(5)
            }
            div(classes = "board-row") {
                renderSquare(6)
                renderSquare(7)
                renderSquare(8)
            }
        }
    }


    interface State: RState {
        var squares: Array<String?>
        var xIsNext: Boolean
    }
}

fun RBuilder.board() = child(Board::class) {}