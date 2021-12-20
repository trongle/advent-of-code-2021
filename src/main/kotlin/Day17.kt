import kotlin.math.abs

fun main(args: Array<String>) {
    val succeedProbes = mutableSetOf<Probe>()
    fun showProbePath(probe: Probe, log: Boolean = false) {
        while (!probe.missedTarget()) {
            val currentPosition = probe.currentPosition()
            if (probe.isInTarget()) {
                succeedProbes.add(probe)
                if (log) println("--> In target at: ${currentPosition.x},${currentPosition.y}")
                break
            }
            if (log) print("current ${currentPosition.x},${currentPosition.y} ")
            probe.move()
            if (log) println()
        }
    }

    val (rangeXString, rangeYString) = Regex("x=(.+), y=(.+)")
        .find(readEntireFile("src/main/resources/day_17.txt"))!!
        .value.split(", ")
    val rangeX = rangeXString.removePrefix("x=").split("..").let { IntRange(it[0].toInt(), it[1].toInt()) }
    val rangeY = rangeYString.removePrefix("y=").split("..").let { IntRange(it[0].toInt(), it[1].toInt()) }

    for (x in 0..rangeX.last) for (y in rangeY.first .. abs(rangeY.first))  {
        println("==============Launched at $x,$y==============")
        showProbePath(Probe.launch(x, y, rangeX, rangeY))
    }

    println("Highest y: ${succeedProbes.maxOf { it.historyOfMoves.maxOf { it.y } }}")
    println("Total success launches: ${succeedProbes.count()}")
}

data class Probe(val launchAt: Position, val target: TargetArea) {
    private var currentX = launchAt.x
    private var currentY = launchAt.y
    private var currentPosition = launchAt
    val historyOfMoves = mutableListOf(launchAt)

    companion object {
        fun launch(x: Int, y: Int, rangeX: IntRange, rangeY: IntRange) =
            Probe(Position(x, y), TargetArea(rangeX, rangeY))
    }

    fun move() {
        if (currentX > 0) currentX--
        currentY--
        currentPosition = Position(currentPosition.x + currentX, currentPosition.y + currentY)
        historyOfMoves.add(currentPosition)
    }

    fun isInTarget(): Boolean =
        currentPosition.x in target.rangeX && currentPosition.y in target.rangeY

    fun currentPosition() = currentPosition

    fun missedTarget() = currentPosition().y < target.rangeY.first()
}

data class Position(var x: Int, var y: Int)

data class TargetArea(val rangeX: IntRange, val rangeY: IntRange)