fun main(args: Array<String>) {
    val opens = listOf('{', '(', '[', '<')
    val closes = listOf('}', ')', ']', '>')
    val mapCloseOpen = closes.zip(opens).toMap()
    val mapOpenClose = opens.zip(closes).toMap()

    fun findCorruptedLines(
        lines: List<String>,
        callback: (line: Int, value: String, expected: Char, corrupted: Char) -> Unit
    ) {
        lineLoop@ for ((index, line) in lines.withIndex()) {
            val openings = mutableListOf<Char>()
            for (i in line) {
                if (opens.contains(i)) openings.add(i)
                else if (mapCloseOpen[i] == openings.last()) openings.removeLast()
                else {
                    val expected = mapOpenClose[openings.last()]!!

//                    println("$line is corrupted.")
//                    println("Expected $expected, but found $i instead.")

                    callback(index, line, expected, i)

                    continue@lineLoop
                }
            }
        }
    }

    fun solvePuzzle1(input: List<String>): Int {
        val corruptedCloses = mutableListOf<Char>()
        val scores = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)

        findCorruptedLines(input) { _, _, _, corrupted -> corruptedCloses.add(corrupted) }

        return corruptedCloses.sumOf { scores[it]!! }
    }


    fun solvePuzzle2(input: List<String>): Long {
        val corruptedLines = mutableListOf<Int>().also { findCorruptedLines(input) { line, _, _, _ -> it.add(line) } }
        val incompleteLines = input.filterIndexed { index, _ -> !corruptedLines.contains(index) }
        val scores = mutableMapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)

        return incompleteLines
            .map { line ->
                mutableListOf<Char>()
                    .also { openings ->
                        line.forEach { if (opens.contains(it)) openings.add(it) else openings.removeLast() }
                    }
                    .let { openings ->
                        buildString { openings.asReversed().forEach { append(mapOpenClose[it]) } }
                    }
            }
            .map { it.fold(0L) { acc, char -> acc * 5 + scores[char]!! } }
            .sorted()
            .let { it[it.size / 2] }
    }

    val input = readFile("src/main/resources/day_10.txt")
    println(solvePuzzle1(input))
    println(solvePuzzle2(input))
}


// Check corrupt
// {([(<{}[<>[]}>{[]{[(<()>
//
// map: { -> }, [ -> ], ( -> ), < -> >
// opens: {([(<[
// closes: }>]}


// Fix incomplete
// [({(<(())[]>[[{[]{<()<>>
//
// opens: [({([[{{
// closes: ))]>])>>