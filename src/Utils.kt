import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.abs
import kotlin.math.absoluteValue


typealias Vector = Point

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("input/$name.txt").readLines()


/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.writeToConsole() = println(this)

fun <T> List<T>.second() = this[1]
fun <T, R> Pair<T, R>.reverse() = second to first

data class Point(val x: Int, val y: Int) {
    fun neighbours(includeCenter: Boolean = false): List<Point> =
        if (includeCenter) listOf(Point(x, y + 1), Point(x + 1, y), this, Point(x, y - 1), Point(x - 1, y))
        else listOf(Point(x, y + 1), Point(x + 1, y), Point(x, y - 1), Point(x - 1, y))

    /**
     * includes diagonal neighbors
     */

    fun allNeighbours(
        minX: Int = Int.MIN_VALUE,
        maxX: Int = Int.MAX_VALUE,
        minY: Int = Int.MIN_VALUE,
        maxY: Int = Int.MAX_VALUE,
    ): Set<Point> =
        setOf(
            Point(x - 1, y - 1),
            Point(x, y - 1),
            Point(x + 1, y - 1),
            Point(x - 1, y),
            Point(x + 1, y),
            Point(x - 1, y + 1),
            Point(x, y + 1),
            Point(x + 1, y + 1)
        ).filter { it.x in minX..maxX && it.y in minY .. maxY}.toSet()

    fun leftOf() = Point(x - 1, y)
    fun rightOf() = Point(x + 1, y)
    fun topOf() = Point(x, y - 1)
    fun bottomOf() = Point(x, y + 1)

    fun distanceTo(other: Point) = abs(x - other.x) + abs(y - other.y)
    operator fun plus(other: Point) = Point(x + other.x, y + other.y)
    operator fun minus(other: Point) = Point(other.x - x, other.y - y)
    fun move(direction: Direction, distance: Int): Point = when (direction) {
        Direction.North -> copy(x, y - distance)
        Direction.East -> copy(x + distance, y)
        Direction.South -> copy(x, y + distance)
        Direction.West -> copy(x - distance, y)
    }
}

fun IntRange.fullyContains(other: IntRange) =
    contains(other.first) && contains(other.last)

fun IntRange.overlapsWith(other: IntRange) =
    contains(other.first) || contains(other.last)

fun IntRange.union(other: IntRange): IntRange? {
    return if (overlapsWith(other))
        IntRange(minOf(first, other.first), maxOf(last, other.last))
    else null
}

fun IntRange.merge(other: IntRange): IntRange {
    return IntRange(maxOf(first, other.first), minOf(last, other.last)).let { if (it.first >= it.last) 0..0 else it }
}

fun List<Int>.findPattern(minWindow: Int = 1, startIndex: Int = 1): Pair<Int, Int> {
    (startIndex..size / 2).forEach { windowSize ->
        print("$windowSize\r")
        val tmp = this.windowed(windowSize)
        tmp.forEachIndexed { index, intList ->
            if (index + windowSize >= tmp.size)
                return@forEachIndexed
            if (intList == tmp[index + windowSize])
                return index + 1 to windowSize
        }
    }
    error("no pattern")
}

fun Long.primeFactors(): List<Long> = mutableListOf<Long>().let { f ->
    (2..this / 2).filter { this % it == 0L }
        .let {
            f.addAll(it)
            if (f.isEmpty()) listOf(this) else f
        }
}

fun Collection<Point>.maxX() = maxOf { it.x }
fun Collection<Point>.maxY() = maxOf { it.y }
fun Collection<Point>.minX() = minOf { it.x }
fun Collection<Point>.minY() = minOf { it.y }

enum class Direction(val vector: Vector) {
    North(Vector(0, -1)),
    East(Vector(1, 0)),
    South(Vector(0, 1)),
    West(Vector(-1, 0));

    fun forwardDirections() = when (this) {
        North -> setOf(North, East, West)
        East -> setOf(East, North, South)
        South -> setOf(South, East, West)
        West -> setOf(West, South, North)
    }
}

fun Iterable<Point>.polygonArea(includingPerimeter: Boolean = true): Long {
    val iter = iterator()
    val start = iter.next()
    var perimeter = 0L
    var sum = 0L
    var last = start

    fun continueLace(from: Point, to: Point) {
        perimeter += from.distanceTo(to)
        sum += from.x.toLong() * to.y.toLong()
        sum -= from.y.toLong() * to.x.toLong()
    }

    while (iter.hasNext()) {
        val next = iter.next()
        continueLace(last, next)
        last = next
    }

    continueLace(last, start)

    val insideArea = sum.absoluteValue / 2

    return if (includingPerimeter) insideArea - perimeter / 2 + 1 + perimeter
    else insideArea
}

fun IntArray.swap(a: Int, b: Int) {
    val tmp = this[indexOf(a)]
    this[indexOf(a)] = b
    this[indexOf(b)] = tmp

}