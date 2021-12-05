import java.io.File

fun readFile(path: String): List<String> = File(path).readLines()

fun readFileAsInt(path: String): List<Int> = File(path).readLines().map { it.toInt() }