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

    private fun handleClick(i: Int) {
        val updateSquares = state.squares.copyOf()

        if (state.xIsNext) updateSquares[i] = "X"
        else updateSquares[i] = "O"

        setState {
            squares = updateSquares
            xIsNext = !state.xIsNext
        }

    }

    override fun RBuilder.render() {
        val status = "Next player :" + (if (state.xIsNext) "X" else "O")

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