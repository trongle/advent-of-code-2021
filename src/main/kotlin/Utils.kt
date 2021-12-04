import java.io.File

fun readFile(path: String): List<String> = File(path).readLines()

fun readFileAsInt(path: String): List<Int> = File(path).readLines().map { it.toInt() }

fun binariesToDecimal(binaries: List<String>): Int {
    var count = 0
    var decimal = 0
    for (i in binaries.size - 1 downTo 0) {
        decimal += if (binaries[i] == "1") 1 shl (count) else 0
        count += 1
    }
    return decimal
}