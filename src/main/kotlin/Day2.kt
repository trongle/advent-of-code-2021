fun main(args: Array<String>) {
    fun solvePuzzle1(input: List<String>): Int {
        var position = 0
        var depth = 0
        return input
            .map { it.split(" ") }
            .forEach { (command, valueString) ->
                val value = valueString.toInt()
                when (command) {
                    "forward" -> position += value
                    "down" -> depth += value
                    "up" -> depth -= value
                    else -> throw Exception("Invalid command.")
                }
            }
            .let { position * depth }
    }

    fun solvePuzzle2(input: List<String>): Int {
        var position = 0
        var depth = 0
        var aim = 0
        return input
            .map { it.split(" ") }
            .forEach { (command, valueString) ->
                val value = valueString.toInt()
                when (command) {
                    "forward" -> {
                        position += value
                        depth += aim * value
                    }
                    "down" -> aim += value
                    "up" -> aim -= value
                    else -> throw Exception("Invalid command.")
                }
            }
            .let { position * depth }
    }

    val input = readFile("src/main/resources/day_2.txt")
    println(solvePuzzle1(input))
    println(solvePuzzle2(input))
}

