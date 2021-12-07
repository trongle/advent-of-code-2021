import kotlin.math.max
import kotlin.math.min

fun main(args: Array<String>) {
    fun solvePuzzle1(input: List<String>): Int =
        input.map { VentLine.fromRaw(it) }.let { MatrixOfVentLines(it).check() }

    fun solvePuzzle2(input: List<String>): Int =
        input.map { VentLine.fromRaw(it) }.let { MatrixOfVentLines(it).check(true) }

    val input = readFile("src/main/resources/day_5.txt")
    println(solvePuzzle1(input))
    println(solvePuzzle2(input))
}

data class MatrixOfVentLines(val ventLines: List<VentLine>) {
    private var maxX: Int? = null
    private var maxY: Int? = null
    private val matrix: MutableMap<Int, MutableList<Int>> = mutableMapOf()
    private var isIncludeDiagonalLines = false

    fun check(isIncludeDiagonalLines: Boolean = false): Int {
        this.isIncludeDiagonalLines = isIncludeDiagonalLines

        build()
        populate()

        return matrix.values.sumOf { it.filter { it >= 2 }.size }
    }

    private fun build() {
        maxX = ventLines.maxOf { max(it.x, it.x2) }
        maxY = ventLines.maxOf { max(it.y, it.y2) }

        for (i in 0..maxY!!) {
            val list = mutableListOf<Int>()
            for (j in 0..maxX!!) {
                list.add(0)
            }
            matrix[i] = list
        }
    }

    private fun populate() {
        ventLines
            .filter { if (!isIncludeDiagonalLines) it.x == it.x2 || it.y == it.y2 else true }
            .forEach {
                if (it.x == it.x2) {
                    val max = max(it.y, it.y2)
                    val min = min(it.y, it.y2)
                    for (i in min..max) {
                        matrix[i]!![it.x]++
                    }
                } else if (it.y == it.y2) {
                    val max = max(it.x, it.x2)
                    val min = min(it.x, it.x2)
                    for (i in min..max) {
                        matrix[it.y]!![i]++
                    }
                } else {
                    if (it.y > it.y2) { // Diagonal Lines tend to go up
                        if (it.x > it.x2) { // Diagonal Lines tend to go up-left
                            var i = it.x
                            var j = it.y
                            while (j >= it.y2 && i >= it.x2) {
                                matrix[j--]!![i--]++
                            }
                        } else { // Diagonal Lines tend to go up-right
                            var i = it.x
                            var j = it.y
                            while (j >= it.y2 && i <= it.x2) {
                                matrix[j--]!![i++]++
                            }
                        }
                    } else { // Diagonal Lines tend to go down
                        if (it.x > it.x2) { // Diagonal Lines tend to go bottom-left
                            var i = it.x
                            var j = it.y
                            while (j <= it.y2 && i >= it.x2) {
                                matrix[j++]!![i--]++
                            }
                        } else { // Diagonal Lines tend to go bottom-right
                            var i = it.x
                            var j = it.y
                            while (j <= it.y2 && i <= it.x2) {
                                matrix[j++]!![i++]++
                            }
                        }
                    }
                }
            }
    }
}

data class VentLine(val x: Int, val y: Int, val x2: Int, val y2: Int) {
    companion object {
        fun fromRaw(input: String): VentLine {
            val (xy, x2y2) = input.split(" -> ")
            val (x, y) = xy.split(",").map { it.toInt() }
            val (x2, y2) = x2y2.split(",").map { it.toInt() }
            return VentLine(x, y, x2, y2)
        }
    }
}

//0,9 -> 5,9
//8,0 -> 0,8
//9,4 -> 3,4
//2,2 -> 2,1
//7,0 -> 7,4
//6,4 -> 2,0
//0,9 -> 2,9
//3,4 -> 1,4
//0,0 -> 8,8
//5,5 -> 8,2
// Pattern (x, y) -> (x2, y2) and between 2 points.
// find max x, max y of matrix
//    max x: 9, max y: 9
// build a matrix
//  x x x x x x x x x x
//y 0 0 0 0 0 0 0 1 0 0
//y 0 0 1 0 0 0 0 1 0 0
//y 0 0 1 0 0 0 0 1 0 0
//y 0 0 0 0 0 0 0 1 0 0
//y 0 1 1 2 1 1 1 2 1 1
//y 0 0 0 0 0 0 0 0 0 0
//y 0 0 0 0 0 0 0 0 0 0
//y 0 0 0 0 0 0 0 0 0 0
//y 0 0 0 0 0 0 0 0 0 0
//y 2 2 2 1 1 0 0 0 0 0


//0,9 -> 5,9
//8,0 -> 0,8
//9,4 -> 3,4
//2,2 -> 2,1
//7,0 -> 7,4
//6,4 -> 2,0
//0,9 -> 2,9
//3,4 -> 1,4
//0,0 -> 8,8
//5,5 -> 8,2
// Pattern (x, y) -> (x2, y2) and between 2 points.
// find max x, max y of matrix
//    max x: 9, max y: 9
// build a matrix
//  x x x x x x x x x x
//y 1 0 1 0 0 0 0 1 1 0
//y 0 1 1 1 0 0 0 2 0 0
//y 0 0 2 0 1 0 1 1 1 0
//y 0 0 0 1 0 2 0 2 0 0
//y 0 1 1 2 3 1 3 2 1 1
//y 0 0 0 1 0 2 0 0 0 0
//y 0 0 1 0 0 0 1 0 0 0
//y 0 1 0 0 0 0 0 1 0 0
//y 1 0 0 0 0 0 0 0 1 0
//y 2 2 2 1 1 0 0 0 0 0