import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.contains

fun main() {


    fun part1(input: List<String>): Int {

        val (locks, keys) = input.parse().partition { it.isLock() }

        val lockHeights = locks.map { it.toHeights() }
        val keyHeights = keys.map { it.toHeights() }
        val flatMap = keyHeights.flatMap { key ->
            lockHeights.mapNotNull { lock ->
                if (key.fits(lock)) key to lock else null
            }
        }
        return flatMap.distinct().size
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("Day25_test")
    val input = readInput("Day25")

    println("test part1: ${part1(testInput)}")
    part1(input).writeToConsole()

    println("test part2: ${part2(testInput)}")
    part2(input).writeToConsole()
}

fun Set<Point>.toHeights(): List<Int> =
    if (isLock()) {
        (0..4).map { c ->
            this.filter { it.x == c }.maxY()
        }
    } else {
        (0..4).map { c ->
            6 - this.filter { it.x == c }.minY()
        }
    }

fun List<Int>.fits(other: List<Int>): Boolean {
    return zip(other).none { it.first + it.second > 5}
}

private fun Set<Point>.isLock() = contains(Point(0, 0))

fun List<String>.parse(): Set<Set<Point>> {
    val result = mutableSetOf<Set<Point>>()

    this.filterNot { it.isBlank() }.chunked(7).map { candidate ->
        val tmp = mutableSetOf<Point>()
        candidate.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (c == '#')
                    tmp.add(Point(x, y))
            }
        }
        result.add(tmp)
    }
    return result
}