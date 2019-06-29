package Game

import Board.board
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.button
import react.dom.div
import react.dom.li
import react.dom.ol
import kotlin.arrayOf
import react.setState

class Game: RComponent<RProps, Game.State>() {
    init {
       state.apply {
           history = arrayOf(HistoryEntry(Array(9) { null }))
           xIsNext = true
           stepNumber = 0
       }
    }

    interface State: RState {
        var history: Array<HistoryEntry>
        var xIsNext: Boolean
        var stepNumber: Int
    }

    data class HistoryEntry(var squares: Array<String?>)

    private fun handleClick(i: Int){
        val history = state.history.sliceArray(0..state.stepNumber)
        val current= history.last()
        val squares = current.squares.copyOf()

        if (calculateWinner(squares) != null || squares[i] != null) return

        squares[i] = if(state.xIsNext) "X" else "O"

        setState{
            this.history = history + HistoryEntry(squares)
            xIsNext = !state.xIsNext
            stepNumber = history.size
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

    private fun jumpTo (step: Int) {
        setState{
            stepNumber = step
            xIsNext = (step % 2) == 0
        }
    }

    private fun RBuilder.moves () =
        state.history.mapIndexed{ step, _ ->
            val desc = when {
                step != 0 -> "Go to move #$step"
                else -> "Go to game start"
            }

            li {
                button {
                    + desc
                    attrs.onClickFunction = { jumpTo(step) }
                }
            }
        }


    override fun RBuilder.render() {
        val history = state.history
        val current = history[state.stepNumber]
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
                ol{ moves ()}
            }
        }
    }
}

fun RBuilder.game() = child(Game::class) {}