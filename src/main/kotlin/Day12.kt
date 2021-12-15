fun main(args: Array<String>) {
    val input = readFile("src/main/resources/day_12.txt")
    fun buildMap(input: List<String>) = buildMap<String, MutableList<String>> {
        input
            .map { val (from, to) = it.split("-"); from to to }
            .forEach { (from, to) ->
                this.getOrPut(from) { mutableListOf() }.add(to)
                this.getOrPut(to) { mutableListOf() }.add(from)
            }
    }
    fun countPaths(
        map: Map<String, MutableList<String>>,
        origin: String,
        target: String,
        visited: MutableSet<String>,
        extraVisit: Boolean = false
    ): Int = when {
        origin == target -> 1
        else -> {
            visited += origin
            val sum = map[origin]!!
                .filter { it !in visited || it.bigCave() }
                .sumOf { countPaths(map, it, target, visited.toMutableSet(), extraVisit) }
            val extraSum = if (!extraVisit) 0 else map[origin]!!
                .filter { it in visited && !it.bigCave() && it !in listOf("start", "end") }
                .sumOf { countPaths(map, it, target, visited.toMutableSet(), false) }
            visited -= origin

            sum + extraSum
        }
    }

    println(countPaths(buildMap(input), "start", "end", mutableSetOf<String>()))
    println(countPaths(buildMap(input), "start", "end", mutableSetOf<String>(), true))
}

fun String.bigCave(): Boolean = all { it.isUpperCase() }