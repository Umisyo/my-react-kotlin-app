package Game

import Board.board
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.div
import kotlin.arrayOf as arrayOf1
import react.setState

class Game: RComponent<RProps, Game.State>() {
    init {
       state.apply {
           history = arrayOf1(HistoryEntry(Array(9) { null }))
           xIsNext = true
       }
    }

    interface State: RState {
        var history: Array<HistoryEntry>
        var xIsNext: Boolean
    }

    data class HistoryEntry(var squares: Array<String?>)

    fun handleClick(i: Int){
        val history = state.history
        val current= state.history.last()
        val squares = current.squares.copyOf()

        if (calculateWinner(squares) != null || squares[i] != null) return

        squares[i] = if(state.xIsNext) "X" else "O"

        setState{
            this.history = history + HistoryEntry(squares)
            xIsNext = !state.xIsNext
        }
    }

    private fun calculateWinner(squares:Array<String?>): String? {
        val lines: Array<Array<Int>> = arrayOf1(
                arrayOf1(0, 1, 2),
                arrayOf1(3, 4, 5),
                arrayOf1(6, 7, 8),
                arrayOf1(0, 3, 6),
                arrayOf1(1, 4, 7),
                arrayOf1(2, 5, 8),
                arrayOf1(0, 4, 8),
                arrayOf1(2, 4, 6)
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
        val history = state.history
        val current = state.history.last()
        val winner = calculateWinner(current.squares)
        val status: String =
                when {
                    winner != null -> "Winner: $winner"
                    else -> "Next player: ${if (state.xIsNext) "X" else "O"}"
                }

        div(classes = "game") {
            div(classes = "game-board") {
                board(current.squares){
                    handleClick(it)
                }
            }
            div(classes = "game-info") {
                div { + status}
                div{/*TODO*/}
            }
        }
    }
}

fun RBuilder.game() = child(Game::class) {}