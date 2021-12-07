import kotlin.math.abs

fun main(args: Array<String>) {
    fun solvePuzzle(input: String, isIncrementFuel: Boolean = false): Int {
        val positions = input.split(",").map { it.toInt() }
        val min = positions.minOf { it }
        val max = positions.maxOf { it }

        return (min..max).minOf { movingPos ->
            positions.sumOf { currentPos ->
                abs(currentPos - movingPos).let {
                    if (isIncrementFuel) (1..it).sum() else it
                }
            }
        }
    }

    val input = readEntireFile("src/main/resources/day_7.txt")
    println(solvePuzzle(input))//345197
    println(solvePuzzle(input, true))//96361606
}