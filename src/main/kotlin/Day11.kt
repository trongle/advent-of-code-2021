fun main(args: Array<String>) {
    data class Octopus(val x: Int, val y: Int, var energy: Int)

    val input = readFile("src/main/resources/day_11.txt")

    fun List<String>.toOctopusesMatrix() = mapIndexed { indexX, x ->
        x.mapIndexed { indexY, y ->
            Octopus(indexX, indexY, y.toString().toInt())
        }
    }

    val maxX = input.size
    val maxY = input[0].length

    fun gainEnergy(
        x: Int,
        y: Int,
        flashedOctopuses: MutableList<Octopus> = mutableListOf(),
        octopusesMatrix: List<List<Octopus>>
    ): Int = when {
        x < 0 || x >= maxX || y < 0 || y >= maxY || flashedOctopuses.contains(octopusesMatrix[x][y]) || ++octopusesMatrix[x][y].energy < 10 -> 0
        else -> {
            octopusesMatrix[x][y].energy = 0
            flashedOctopuses.add(octopusesMatrix[x][y])

            1 +
                    gainEnergy(x - 1, y, flashedOctopuses, octopusesMatrix) +
                    gainEnergy(x - 1, y + 1, flashedOctopuses, octopusesMatrix) +
                    gainEnergy(x, y + 1, flashedOctopuses, octopusesMatrix) +
                    gainEnergy(x + 1, y + 1, flashedOctopuses, octopusesMatrix) +
                    gainEnergy(x + 1, y, flashedOctopuses, octopusesMatrix) +
                    gainEnergy(x + 1, y - 1, flashedOctopuses, octopusesMatrix) +
                    gainEnergy(x, y - 1, flashedOctopuses, octopusesMatrix) +
                    gainEnergy(x - 1, y - 1, flashedOctopuses, octopusesMatrix)
        }

    }

    fun solvePuzzle1(octopusesMatrix: List<List<Octopus>>): Int {
        var totalFlashes = 0

        repeat(100) {
            val flashedOctopuses = mutableListOf<Octopus>()
            for (octopuses in octopusesMatrix) {
                for (octopus in octopuses) {
                    totalFlashes += gainEnergy(octopus.x, octopus.y, flashedOctopuses, octopusesMatrix)
                }
            }
        }

        return totalFlashes
    }

    fun solvePuzzle2(octopusesMatrix: List<List<Octopus>>): Int {
        var steps = 0
        val newOctopusesMatrix = octopusesMatrix

        while (newOctopusesMatrix.flatten().any { it.energy != 0 }) {
            val flashedOctopuses = mutableListOf<Octopus>()
            for (octopuses in octopusesMatrix) {
                for (octopus in octopuses) {
                    gainEnergy(octopus.x, octopus.y, flashedOctopuses, octopusesMatrix)
                }
            }
            steps++
        }

        return steps
    }

    println(solvePuzzle1(input.toOctopusesMatrix()))
    println(solvePuzzle2(input.toOctopusesMatrix()))
}