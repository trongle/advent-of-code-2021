fun main(args: Array<String>) {
    fun solvePuzzles(input: List<Int>, days: Int): Long {
        val timers = mutableMapOf<Int, Long>(
            0 to 0,
            1 to 0,
            2 to 0,
            3 to 0,
            4 to 0,
            5 to 0,
            6 to 0,
            7 to 0,
            8 to 0
        ).let { timers ->
            input.toMutableList().forEach { timers[it] = timers[it]!! + 1 }
            timers
        }

        repeat(days) {
            val news = timers[0]!!

            (1..8).forEach { timers[it - 1] = timers[it]!! }

            timers[6] = timers[6]!!.plus(news)
            timers[8] = news
        }
        return timers.values.sum()
    }

    val input = readEntireFile("src/main/resources/day_6.txt").split(",").map { it.toInt() }
    println(solvePuzzles(input, 80))
    println(solvePuzzles(input, 256))
}