fun main(args: Array<String>) {
    val input = readEntireFile("src/main/resources/day_13.txt").split("\n\n")
    val dots = input[0].split("\n")
    val foldInstructions = input[1].split("\n")

    fun fold(
        input: List<String>,
        foldInstructions: List<String>
    ): Set<String> {
        var newInput = input

        for (foldInstruction in foldInstructions) {
            val isFoldX = foldInstruction.contains('x')
            val foldValue = foldInstruction.split("=")[1].toInt()
            val (part1, part2) = newInput.partition {
                if (isFoldX) {
                    it.split(",")[0].toInt() < foldValue
                } else {
                    it.split(",")[1].toInt() < foldValue
                }
            }

            newInput = part1 + part2.map {
                val (x, y) = it.split(",").map { it.toInt() }
                if (isFoldX) {
                    val newX = foldValue - (x - foldValue)
                    "$newX,$y"
                } else {
                    val newY = foldValue - (y - foldValue)
                    "$x,$newY"
                }

            }.filter {
                val (x, y) = it.split(",").map { it.toInt() }
                if (isFoldX) {
                    x >= 0
                } else {
                    y >= 0
                }
            }
        }

        return newInput.toSet()
    }

    // Puzzle 1
    println(fold(dots, listOf(foldInstructions.first())).count())

    // Puzzle 2
    fold(dots, foldInstructions).let {
        val maxX = it.maxOf { it.split(",")[0].toInt() }
        val maxY = it.maxOf { it.split(",")[1].toInt() }
        val matrix: List<MutableList<String>> = buildList {
            repeat(maxY + 1) {
                this.add(
                    buildList { repeat(maxX + 1) { this.add(".") } }.toMutableList()
                )
            }
        }

        it.forEach {
            val (x, y) = it.split(",").map { it.toInt() }
            matrix[y][x] = "#"
        }

        matrix.forEach { println(it.joinToString("")) }
    }
}


//0  . . . . . . . . . .    0//6  . . . . . . . . . .
//1  . . . . . . . . . .    1//5  . . . . . . . . . .
//2  . . . . . . . . . .    2//4  . . . . . . . . . .

//3  . . . . . . . . . .

//2//4  . . . . . . . . . .
//1//5  . . . . . . . . . .
//0//6  . . . . . . . . . .
//-1//7  . . . . . . . . . .
//-2//8  . . . . . . . . . .
//-3//9  . . . . . . . . . .
//-4//10 . . . . . . . . . .
//-5//11 . . . . . . . . . .
//-6//12 . . . . . . . . . .
//-7//13 . . . . . . . . . .
//-8//14 . . . . . . . . . .
//
//
//6,10
//0,14
//9,10
//0,3
//10,4
//4,11
//6,0
//6,12
//4,1
//0,13
//10,12
//3,4
//3,0
//8,4
//1,10
//2,14
//8,10
//9,0
//->
//6,0          6,0
//9,0          9,0
//3,0          3,0
//0,0          0,0
//2,0          2,0
//4,1          4,1
//0,1          0,1
//6,2          6,2
//10,2    ->   10,2
//0,3          0,3
//4,3          4,3
//10,4         10,4
//3,4          3,4
//8,4          8,4
//8,4
//6,4          6,4
//9,4          9,4
//1,4          1,4
//
//
//6,10
//9,10
//1,10
//8,10
//4,11
//6,12
//10,12
//0,13
//0,14
//2,14
//
//maxX = 10
//maxY = 14


//6,0
//3,0
//9,0
//0,0
//2,0
//4,1
//0,1
//6,2
//10,2
//0,3
//4,3
//10,4
//3,4
//8,4
//6,4
//9,4
//1,4
//
//# . # # . . # . # .
//# . . . # . . . . .
//. . . . . . # . . #
//# . . . # . . . . .
//. # . # . # . # # #
//. . . . . . . . . .
//. . . . . . . . . .