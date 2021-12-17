fun main(args: Array<String>) {
    fun solvePuzzle1(template: String, pairInsertionRules: Map<String, String>): Int {
        var newTemplate = template

        repeat(10) {
            newTemplate = newTemplate
                .windowed(2)
                .fold(newTemplate[0].toString()) { acc, s ->
                    var new = s
                    val part2 = pairInsertionRules[new]
                    if (part2 != null) {
                        new = part2 + new[1]
                    }
                    acc + new
                }
        }

        return newTemplate
            .split("")
            .groupBy { it }
            .filter { it.key.isNotBlank() }
            .map { it.value.count() }
            .let { result -> result.maxOf { it } - result.minOf { it } }
    }

    val input = readFile("src/main/resources/day_14.txt")
    val pairInsertionRules = buildMap {
        input
            .slice(2 until input.size)
            .forEach {
                val (part1, part2) = it.split(" -> ")
                this[part1] = part2
            }
    }

    fun solvePuzzle2(template: String, pairInsertionRules: Map<String, String>): Long {
        data class ParsedRule(val from: String, val tos: List<String>)

        val parsedRules = pairInsertionRules.map { (from, to) ->
            ParsedRule(from, listOf("${from[0]}$to", "$to${from[1]}"))
        }

        var newPolymer = template.windowed(2).map { it to 1L }
        repeat(40) {
            newPolymer = newPolymer.flatMap { (id, count) ->
                val tos = parsedRules.find { (from, _) -> from == id }!!.tos
                tos.map { it to count }
            }
            newPolymer = newPolymer
                .distinctBy { it.first }
                .let { uniquePolymer ->
                    uniquePolymer.map { (id, _) ->
                        id to newPolymer.filter { it.first == id }.sumOf { it.second }
                    }
                }
        }

        val map = mutableMapOf<Char, Long>()
        for (polymer in newPolymer) {
            val second = polymer.first[1]
            map[second] = (map[second] ?: 0) + polymer.second
        }
        map[template[0]] = (map[template[0]] ?: 0) + 1

        return map.maxOf { it.value } - map.minOf { it.value }
    }

    println(solvePuzzle1(input[0], pairInsertionRules))
    println(solvePuzzle2(input[0], pairInsertionRules))
}