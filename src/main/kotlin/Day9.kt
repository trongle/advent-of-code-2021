fun main(args: Array<String>) {
    fun solvePuzzle1(input: List<String>): Int {
        var sum = 0
        input.forEachIndexed { rowIndex, valueOfRow ->
            valueOfRow
                .map { it.toString().toInt() }
                .let {
                    it.forEachIndexed { colIndex, valueOfCol ->
                        val top = input.getOrNull(rowIndex - 1)?.getOrNull(colIndex)?.toString()?.toInt() ?: 9
                        val right = it.getOrElse(colIndex + 1) { 9 }
                        val bottom = input.getOrNull(rowIndex + 1)?.getOrNull(colIndex)?.toString()?.toInt() ?: 9
                        val left = it.getOrElse(colIndex - 1) { 9 }

                        if (valueOfCol < top && valueOfCol < right && valueOfCol < bottom && valueOfCol < left) {
                            sum += valueOfCol + 1
                        }
                    }
                }
        }

        return sum
    }

    fun solvePuzzle2(input: List<String>): Int {
        data class Point(val x: Int, val y: Int, val value: Int, var visited: Boolean = false)

        val heightMapPoints = input.mapIndexed { indexX, x ->
            x.mapIndexed { indexY, y -> Point(indexX, indexY, y.toString().toInt()) }
        }
        val maxX = heightMapPoints.size
        val maxY = heightMapPoints[0].size

        fun countPointsInBasin(x: Int, y: Int): Int =
            if (x < 0 || x >= maxX || y < 0 || y >= maxY || heightMapPoints[x][y].visited || heightMapPoints[x][y].value == 9) 0
            else {
                heightMapPoints[x][y].visited = true
                1 + countPointsInBasin(x - 1, y) + countPointsInBasin(x + 1, y) + countPointsInBasin(x, y - 1) + countPointsInBasin(x, y + 1)
            }

        return heightMapPoints
            .asSequence()
            .flatten()
            .mapNotNull { (x, y, value, visited) ->
                if (visited || value == 9) null
                else countPointsInBasin(x, y)
            }
            .sortedDescending()
            .take(3)
            .reduce { acc, i -> acc * i }
    }

    val input = readFile("src/main/resources/day_9.txt")
    println(solvePuzzle1(input))
    println(solvePuzzle2(input))
}