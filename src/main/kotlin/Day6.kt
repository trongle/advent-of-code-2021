fun main(args: Array<String>) {
    fun solvePuzzles(input: List<Int>, days: Int): Long {
        val timers = Array(9) { 0L }.let { timers ->
            input.toMutableList().forEach { timers[it] = timers[it] + 1 }
            timers
        }

        repeat(days) {
            val news = timers[0]

            (1..8).forEach { timers[it - 1] = timers[it] }

            timers[6] = timers[6].plus(news)
            timers[8] = news
        }
        return timers.sum()
    }

    val input = readEntireFile("src/main/resources/day_6.txt").split(",").map { it.toInt() }
    println(solvePuzzles(input, 80))
    println(solvePuzzles(input, 256))
}