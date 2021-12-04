fun main(args: Array<String>) {
    // answer: 1154
    fun solvePuzzle1(input: List<Int>): Int =
        input
            .windowed(2)
            .count { (a, b) -> b > a }

    //answer: 1127
    fun solvePuzzle2(input: List<Int>): Int =
        input
            .windowed(3)
            .map { it.sumOf { i -> i } }
            .windowed(2)
            .count { (a, b) -> b > a }

    val input = readFileAsInt("src/main/resources/day_1.txt")
    println(solvePuzzle1(input))
    println(solvePuzzle2(input))
}
