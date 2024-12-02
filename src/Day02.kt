import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        return input.count { line ->
            val numbers = line.split(" ").map { it.toInt() }
            numbers.safe()
        }
    }

    fun part2(input: List<String>): Int {
        return input.count { line ->
            val numbers = line.split(" ").map { it.toInt() }
            numbers.safe() ||
                    numbers.indices.any {
                        val candidate = numbers.toMutableList()
                        candidate.removeAt(it)
                        candidate.safe()
                    }
        }
    }

    val testInput = readInput("Day02_test")
    println("test part1: ${part1(testInput)}")

    val input = readInput("Day02")
    part1(input).writeToConsole()

    println("test part2: ${part2(testInput)}")
    part2(input).writeToConsole()
}

fun List<Int>.safe() = (allDecreasing() || allIncreasing()) && allStepsIn(1..3)
fun List<Int>.allIncreasing(): Boolean = windowed(2, 1).all { it.first() < it.last() }
fun List<Int>.allDecreasing(): Boolean = windowed(2, 1).all { it.first() > it.last() }
fun List<Int>.allStepsIn(range: IntRange): Boolean = windowed(2, 1).all { abs(it.first() - it.last()) in range }


