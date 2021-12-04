fun main(args: Array<String>) {
    //answer: 1127
    fun solvePuzzle2() {
        readFileAsInt("src/main/resources/day_1.txt")
            .windowed(3)
            .map { it.sumOf { i -> i } }
            .windowed(2)
            .count { (a, b) -> b > a }
            .let { println(it) }
    }

    // answer: 1154
    fun solvePuzzle1() {
        readFileAsInt("src/main/resources/day_1.txt")
            .windowed(2)
            .count { (a, b) -> b > a }
            .let { println(it) }
    }

    solvePuzzle1()
    solvePuzzle2()
}
