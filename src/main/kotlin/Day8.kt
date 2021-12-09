fun main(args: Array<String>) {
    fun solvePuzzle1(input: List<String>): Int = input.sumOf {
        it
            .split(" | ")[1]
            .split(" ")
            .count { digit -> listOf(2, 4, 3, 7).contains(digit.length) }
    }

    fun solvePuzzle2(input: List<String>): Int {
//         0000          aaaa
//        1    2        b    c
//        1    2        b    c
//         3333    ==    dddd
//        4    5        e    f
//        4    5        e    f
//         6666          gggg

        val segmentsToDigits = mapOf(
            setOf(0, 1, 2, 4, 5, 6) to 0,
            setOf(2, 5) to 1,
            setOf(0, 2, 3, 4, 6) to 2,
            setOf(0, 2, 3, 5, 6) to 3,
            setOf(1, 2, 3, 5) to 4,
            setOf(0, 1, 3, 5, 6) to 5,
            setOf(0, 1, 3, 4, 5, 6) to 6,
            setOf(0, 2, 5) to 7,
            setOf(0, 1, 2, 3, 4, 5, 6) to 8,
            setOf(0, 1, 2, 3, 5, 6) to 9
        )

        fun randomConfig(patterns: List<String>, output: List<String>): Int {
            val inputCables = 0..6
            val inputChars = 'a'..'g'
            fun findRightPattern(): Map<Char, Int> {
                findPatternLoop@ while (true) {
                    val patternAttempting = inputChars.zip(inputCables.shuffled()).toMap()
                    for (pattern in patterns) {
                        val mapped = pattern.map { patternAttempting[it] }.toSet()
                        val isValid = segmentsToDigits.containsKey(mapped)
                        if (!isValid) continue@findPatternLoop
                    }
                    return patternAttempting
                }
            }

            return findRightPattern().let { pattern ->
                output
                    .map { segmentsToDigits[it.map { char -> pattern[char]!! }.toSet()] }
                    .joinToString("")
                    .toInt()
            }
        }

        return input.sumOf {
            val (patterns, output) = it.split(" | ")
            randomConfig(patterns.split(" "), output.split(" "))
        }
    }

    val input = readFile("src/main/resources/day_8.txt")
    println(solvePuzzle1(input))
    println(solvePuzzle2(input))
}