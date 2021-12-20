val hexToBinary = mapOf(
    '0' to "0000",
    '1' to "0001",
    '2' to "0010",
    '3' to "0011",
    '4' to "0100",
    '5' to "0101",
    '6' to "0110",
    '7' to "0111",
    '8' to "1000",
    '9' to "1001",
    'A' to "1010",
    'B' to "1011",
    'C' to "1100",
    'D' to "1101",
    'E' to "1110",
    'F' to "1111",
)

fun Iterator<Char>.next(n: Int): String = (0 until n).map { next() }.joinToString("")
fun Iterator<Char>.nextInt(n: Int): Int = next(n).toInt(2)
fun <T> Iterator<Char>.executeUntilEmpty(function: (Iterator<Char>) -> T): List<T> {
    val output = mutableListOf<T>()
    while (this.hasNext()) {
        output.add(function(this))
    }
    return output
}

sealed class BitPackage(open val version: Int) {
    abstract val value: Long

    companion object {
        fun from(input: Iterator<Char>): BitPackage {
            val version = input.nextInt(3)
            return when (val typeId = input.nextInt(3)) {
                4 -> LiteralValuePackage.from(version, input)
                else -> OperatorPackage.from(version, input, typeId)
            }
        }
    }
}

data class LiteralValuePackage(override val version: Int, override val value: Long) : BitPackage(version) {

    companion object {
        fun from(version: Int, input: Iterator<Char>): LiteralValuePackage =
            LiteralValuePackage(version, parseValue(input))

        private fun parseValue(input: Iterator<Char>): Long {
            var value = ""
            do {
                val group = input.next(5)
                value += group.slice(1 until group.length)
            } while (group.startsWith('1'))

            return value.toLong(2)
        }
    }
}

data class OperatorPackage(override val version: Int, val typeId: Int, val subPackages: List<BitPackage>) :
    BitPackage(version) {
    override val value: Long = when (typeId) {
        0 -> subPackages.sumOf { it.value }
        1 -> subPackages.fold(1) { acc, c -> acc * c.value }
        2 -> subPackages.minOf { it.value }
        3 -> subPackages.maxOf { it.value }
        5 -> if (subPackages[0].value > subPackages[1].value) 1 else 0
        6 -> if (subPackages[0].value < subPackages[1].value) 1 else 0
        7 -> if (subPackages[0].value == subPackages[1].value) 1 else 0
        else -> error("Not support type id: $typeId")
    }

    companion object {
        fun from(version: Int, input: Iterator<Char>, typeId: Int): BitPackage =
            when (val lengthId = input.nextInt(1)) {
                0 -> {
                    val lengthOfSubPackages = input.nextInt(15)
                    val subPackagesIterator = input.next(lengthOfSubPackages).iterator()
                    val subPackages = subPackagesIterator.executeUntilEmpty { from(it) }
                    OperatorPackage(version, typeId, subPackages)
                }
                1 -> {
                    var numOfSubpackages = input.nextInt(11)
                    val subPackages = mutableListOf<BitPackage>()
                    while (numOfSubpackages > 0) {
                        numOfSubpackages--
                        subPackages += from(input)
                    }
                    OperatorPackage(version, typeId, subPackages)
                }
                else -> error("Not support length id $lengthId")
            }

    }
}

fun main(args: Array<String>) {
    data class Package(val binaries: String, val operator: String? = null) {
        val version = binaries.slice(0..2).toInt(2)
        var typeId = binaries.slice(3..5).toInt(2)
        val isLiteralValue = typeId == 4

        fun subPackage(): Package? {
            if (isLiteralValue) {
                var start = 6
                do {
                    val group = binaries.slice(start..start + 4)
                    start += 5
                } while (group.startsWith('1'))

                val newValue = binaries.slice(start until binaries.length)
                return if (newValue.all { it == '0' }) null else Package(newValue)
            } else {
                return if (binaries[6] == '0') {
                    val newValue = binaries.slice(22 until binaries.length)
                    if (newValue.all { it == '0' }) null else Package(newValue)
                } else {
                    val newValue = binaries.slice(18 until binaries.length)
                    if (newValue.all { it == '0' }) null else Package(newValue)
                }
            }
        }
    }

    fun solvePuzzle1(input: String): Int {
        var aPackage: Package? = Package(input)
        var sum = 0
        while (aPackage != null) {
            sum += aPackage.version
            aPackage = aPackage.subPackage()
        }
        return sum
    }

    fun solvePuzzle2(input: String): Long = BitPackage.from(input.iterator()).value


    val input = readEntireFile("src/main/resources/day_16.txt").fold("") { acc, c -> acc + hexToBinary[c] }
    println(solvePuzzle2(input))
}

//9C0141080250320F1802104A08
//
//100 111 0 000000001010000 [01000010000000001001010000001100100000111100011000000000100001000001001010000010] is equal [00] is ignore
//4   7   0 80              [010 000 1 00000000010 [0101000000110010000011] is sum                     110 001 1 00000000010 [0001000001001010000010] is multiply] is equal
//2   0   1 2           [010 100 [00001] is (1) 100 100 [00011] is (3)] is sum  6   1   1 2           [000 100 [00010] is (2) 010 100 [00010] is (2)] is multiply] is equal