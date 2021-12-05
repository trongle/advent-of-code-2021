fun main(args: Array<String>) {
    val bingoData = readEntireFile("src/main/resources/day_4.txt").split("""\n\n""".toRegex())
    val drawNumbers = bingoData[0].split(",")
    val rawBoards = bingoData.slice(1 until bingoData.size)

    fun solvePuzzle1(drawNumbers: List<Int>, boards: List<Board>) {
        drawNumbers.forEach { number ->
            boards.forEachIndexed { index, board ->
                if (board.check(number)) {
                    println("Puzzle 1: Board ${index + 1} won!")
                    println(board.unmarkedNumbers.sum() * number)
                    return
                }
            }
        }
    }

    fun solvePuzzle2(drawNumbers: List<Int>, boards: List<Board>) {
        val boardsWon = mutableListOf<Int>()
        drawNumbers.forEach { number ->
            boards.forEachIndexed { index, board ->
                if (!boardsWon.contains(index)) {
                    if (board.check(number)) {
                        println("Puzzle 2: Board ${index + 1} won!")
                        println(board.unmarkedNumbers.sum() * number)
                        boardsWon.add(index)
                    }
                }
            }
        }
    }

    solvePuzzle1(drawNumbers.map { it.toInt() }, rawBoards.map { Board.fromRawBoard(it) })
    solvePuzzle2(drawNumbers.map { it.toInt() }, rawBoards.map { Board.fromRawBoard(it) })
}

data class Board(
    private val index: Map<Int, Position>,
    val numbersOnBoard: List<Int>,
) {
    private val unmarkedRows = mutableListOf(5, 5, 5, 5, 5)
    private val unmarkedCols = mutableListOf(5, 5, 5, 5, 5)
    private val numbersFoundOnBoard = mutableListOf<Int>()
    val unmarkedNumbers
        get() = numbersOnBoard.filterNot { numbersFoundOnBoard.contains(it) }

    data class Position(val row: Int, val col: Int)

    companion object {
        fun fromRawBoard(rawBoard: String): Board {
            val numbers = mutableListOf<Int>()
            val index = mutableMapOf<Int, Position>()
            var col = 0
            var row = 0

            return rawBoard
                .split("""\s""".toRegex())
                .filter { it.isNotBlank() }
                .forEach {
                    numbers.add(it.toInt())
                    index[it.toInt()] = Position(row, col)
                    if (col < 4) {
                        col++
                    } else {
                        row++
                        col = 0
                    }
                }
                .let { Board(index, numbers) }
        }
    }

    fun check(number: Int): Boolean {
        var isWin = false

        if (numbersOnBoard.contains(number)) {
            numbersFoundOnBoard.add(number)

            val position = index[number]!!
            unmarkedRows[position.row]--
            unmarkedCols[position.col]--
            if (unmarkedRows[position.row] == 0 || unmarkedCols[position.col] == 0) {
                isWin = true
            }
        }

        return isWin
    }
}

