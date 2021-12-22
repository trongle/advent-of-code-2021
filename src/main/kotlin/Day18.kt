import java.util.*
import kotlin.math.ceil
import kotlin.math.floor


//- add two snailfish numbers
//        + For example, [1,2] + [[3,4],5] becomes [[1,2],[[3,4],5]]
//- snailfish numbers must always be reduced, and the process of adding two snailfish numbers can result in snailfish numbers that need to be reduced.
//        + repeatedly do the first action in this list that applies to the snailfish number
//            + If any pair is nested inside four pairs, the leftmost such pair explodes.
//                + the pair's left value is added to the first regular number to the left of the exploding pair (if any)
//                + the pair's right value is added to the first regular number to the right of the exploding pair (if any)
//                + the entire exploding pair is replaced with the regular number 0
//                + Ex: [[[[[9,8],1],2],3],4] becomes [[[[0,9],2],3],4] (the 9 has no regular number to its left, so it is not added to any regular number).
//            + If any regular number is 10 or greater, the leftmost such regular number splits.
//                + replace it with a pair
//                + the left element of the pair should be the regular number divided by two and rounded down
//                + the right element of the pair should be the regular number divided by two and rounded up
//                + For example, 10 becomes [5,5], 11 becomes [5,6], 12 becomes [6,6]
//        + Once no action in the above list applies, the snailfish number is reduced
//        + During reduction, at most one action applies
//- To check whether it's the right answer, the snailfish teacher only checks the magnitude of the final sum
//        + The magnitude of a pair is 3 times the magnitude of its left element plus 2 times the magnitude of its right element
//        + For example, the magnitude of [9,1] is 3*9 + 2*1 = 29; [[1,2],[[3,4],5]] becomes 143
//
//[1,1]
//[2,2]
//[3,3]
//[4,4]
//Sum:     [[[[1,1], [2,2]], [3,3]], [4,4]]
//
//[1,1]
//[2,2]
//[3,3]
//[4,4]
//[5,5]
//Sum: [[[[[1,1], [2,2]], [3,3]], [4,4]], [5,5]]
//Explode: [[[[0, [3,2]], [3,3]], [4,4]], [5,5]]
//Explode: [[[[3, 0], [5,3]], [4,4]], [5,5]]

fun main(args: Array<String>) {
    fun calculateMagnitude(input: List<String>) =
        input.map { SnailfishNumber.from(it) }.reduce { acc, current -> acc + current }.magnitude()

    fun solvePuzzle1(input: List<String>) = calculateMagnitude(input)

    fun solvePuzzle2(input: List<String>) = input.maxOf { first ->
        input
            .filter { it != first }
            .map { second -> calculateMagnitude(listOf(first, second)) }
            .maxOf { it }
    }

    val input = readFile("src/main/resources/day_18.txt")
    println(solvePuzzle1(input))
    println(solvePuzzle2(input))

}

sealed class SnailfishNumber {
    var parent: SnailfishNumber? = null
    abstract fun pairsInOrderWithDepth(depth: Int = 0): List<Pair<PairNumber, Int>>
    abstract fun regularsInOrder(): List<RegularNumber>
    abstract fun magnitude(): Int

    companion object {
        fun from(input: String): SnailfishNumber {
            val stack = Stack<SnailfishNumber>()
            input.forEach { char ->
                when {
                    char.isDigit() -> stack.add(RegularNumber(char.digitToInt()))
                    char == ']' -> {
                        val right = stack.pop()
                        val left = stack.pop()
                        stack.add(PairNumber(left, right))
                    }
                }
            }
            return stack.pop()
        }
    }

    operator fun plus(other: SnailfishNumber): SnailfishNumber = PairNumber(this, other).also {
//        println("After addition: ${root()}")
        reduce()
    }

    open fun reduce() {
        do {
            val reduced = explode() || split()
        } while (reduced)
    }

    private fun explode(): Boolean {
        val root = root()
        val pairs = root.pairsInOrderWithDepth()
        val explodingPair = pairs.firstOrNull { it.second == 4 }

        if (explodingPair != null) {
            val pairNumber = explodingPair.first
            val regularNumbers = root.regularsInOrder()
            regularNumbers
                .elementAtOrNull(regularNumbers.indexOfFirst { it === pairNumber.left } - 1)
                ?.addValue(pairNumber.left as RegularNumber)
            regularNumbers
                .elementAtOrNull(regularNumbers.indexOfFirst { it === pairNumber.right } + 1)
                ?.addValue(pairNumber.right as RegularNumber)
            (pairNumber.parent as PairNumber).hasChildExploded(pairNumber)
//            println("After explode: $root")
        }

        return explodingPair != null
    }

    private fun split(): Boolean {
        val root = root()
        val regulars = root.regularsInOrder()
        val splittingRegular = regulars.firstOrNull { it.value >= 10 }

        if (splittingRegular != null) {
            val parent = splittingRegular.parent as PairNumber
            val pair = PairNumber(
                RegularNumber(floor(splittingRegular.value.toDouble() / 2.0).toInt()),
                RegularNumber(ceil(splittingRegular.value.toDouble() / 2.0).toInt())
            ).also { it.parent = parent }

            if (parent.left === splittingRegular) parent.left = pair else parent.right = pair

//            println("After split: $root")
        }

        return splittingRegular != null
    }

    private fun root(): SnailfishNumber = parent?.root() ?: this
}

class RegularNumber(var value: Int) : SnailfishNumber() {
    override fun pairsInOrderWithDepth(depth: Int): List<Pair<PairNumber, Int>> = emptyList()
    override fun regularsInOrder(): List<RegularNumber> = listOf(this)

    fun addValue(amount: RegularNumber) {
        value += amount.value
    }

    override fun magnitude(): Int = value

    override fun toString(): String = value.toString()
}

class PairNumber(var left: SnailfishNumber, var right: SnailfishNumber) : SnailfishNumber() {
    init {
        left.parent = this
        right.parent = this
    }

    override fun pairsInOrderWithDepth(depth: Int): List<Pair<PairNumber, Int>> =
        this.left.pairsInOrderWithDepth(depth + 1) +
                listOf(Pair(this, depth)) +
                this.right.pairsInOrderWithDepth(depth + 1)

    override fun regularsInOrder() = left.regularsInOrder() + right.regularsInOrder()

    fun hasChildExploded(child: PairNumber) {
        val replacement = RegularNumber(0).apply { parent = this@PairNumber }
        if (left === child) left = replacement else right = replacement
    }

    override fun magnitude(): Int = left.magnitude() * 3 + right.magnitude() * 2

    override fun toString(): String = "[$left,$right]"
}