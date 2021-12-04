fun main(args: Array<String>) {
    val input = readFile("src/main/resources/day_3.txt")

    fun solvePuzzle1(input: List<String>): Int {
        val cols = input[0].length
        val rows = input.size
        val gammaRate = mutableListOf<String>()
        val epsilonRate = mutableListOf<String>()

        for (col in 0 until cols) {
            var count0bits = 0
            var count1bits = 0
            for (row in 0 until rows) {
                if (input[row][col] == '0') count0bits++
                else count1bits++
            }
            if (count1bits > count0bits) {
                gammaRate.add("1")
                epsilonRate.add("0")
            } else {
                gammaRate.add("0")
                epsilonRate.add("1")
            }
        }

        return binariesToDecimal(gammaRate) * binariesToDecimal(epsilonRate)
    }

    fun solvePuzzle2(input: List<String>): Int {
        val cols = input[0].length
        var oxygenGeneratorRating = input.toMutableList()
        var co2GeneratorRating = input.toMutableList()

        for (col in 0 until cols) {
            if (oxygenGeneratorRating.size == 1 && co2GeneratorRating.size == 1) break

            if (oxygenGeneratorRating.size > 1) {
                oxygenGeneratorRating = oxygenGeneratorRating
                    .partition { it[col] == '0' }
                    .let { (count0bits, count1bits) ->
                        if (count1bits.size >= count0bits.size) count1bits.toMutableList()
                        else count0bits.toMutableList()
                    }
            }

            if (co2GeneratorRating.size > 1) {
                co2GeneratorRating = co2GeneratorRating
                    .partition {it[col] == '0'}
                    .let { (count0bits, count1bits) ->
                        if (count0bits.size <= count1bits.size) count0bits.toMutableList()
                        else count1bits.toMutableList()
                    }
            }
        }

        return binariesToDecimal(oxygenGeneratorRating.first().toList().map { it.toString() }) *
                binariesToDecimal(co2GeneratorRating.first().toList().map { it.toString() })
    }

    println(solvePuzzle1(input)) // 3847100
    println(solvePuzzle2(input)) // 4105235
}