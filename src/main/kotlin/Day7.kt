import kotlin.math.abs

fun main(args: Array<String>) {
    fun solvePuzzle1(input: String): Int {
        val positions = input.split(",").map { it.toInt() }
        val min = positions.minOf { it }
        val max = positions.maxOf { it }

        return (min..max).minOf { positions.sumOf { y -> abs(y - it) } }
    }

    fun solvePuzzle2(input: String): Int {
        val positions = input.split(",").map { it.toInt() }
        val min = positions.minOf { it }
        val max = positions.maxOf { it }

        return (min..max).minOf {
            positions.sumOf { y ->
                var sum = 0
                for (i in 1..abs(y - it)) sum += i
                sum
            }
        }
    }

    val input = readEntireFile("src/main/resources/day_7.txt")
    println(solvePuzzle1(input))
    println(solvePuzzle2(input))
}