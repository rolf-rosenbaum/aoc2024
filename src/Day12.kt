import java.util.*

typealias Garden = Map<Point, Char>
typealias Region = Set<Point>

fun main() {

    fun part1(input: List<String>): Int {
        val garden = input.parseGarden()
        val regions = garden.toRegions()

        return regions.sumOf { it.size * it.perimeter() }
    }

    fun part2(input: List<String>): Int {
        val garden = input.parseGarden()
        val regions = garden.toRegions()
        return regions.sumOf { it.size * it.sides() }
    }

    val testInput = readInput("Day12_test")

    val input = readInput("Day12")

    println("test part1: ${part1(testInput)}")
    part1(input).writeToConsole()

    println("test part2: ${part2(testInput)}")
    part2(input).writeToConsole()
}

fun Garden.toRegions(): Set<Set<Point>> {
    val regions = mutableSetOf<Region>()
    val done = mutableSetOf<Point>()

    values.forEach { type ->
        val foo = this.filter { (_, c) -> c == type }
        foo.keys.forEach { k ->
            if (!done.contains(k)) {
                val region = toRegion(k, type)
                regions.add(region).also { done.addAll(region) }
            }
        }
    }
    return regions
}

fun Garden.toRegion(p: Point, type: Char): Region {
    val visited = mutableSetOf<Point>()
    val queue = LinkedList<Point>().apply { add(p) }
    while (queue.isNotEmpty()) {
        val next = queue.poll()
        if (visited.add(next)) {
            queue.addAll(next.neighbours().filter { this[it] == type })
        }
    }
    return visited
}

fun Region.perimeter(): Int {
    return sumOf {
        4 - it.neighbours().count { p -> contains(p) }
    }
}

fun List<Int>.groups(): Int {
    if (isEmpty()) return 0
    var g = 1
    (0 until size - 1).forEach { i ->
        if (this[i + 1] - this[i] > 1) {
            g++
        }
    }
    return g
}

fun Region.sides(): Int {
    return scanFromTop() + scanFromBottom() + scanFromLeft() + scanFromRight()
}

fun Region.scanFromTop(): Int =
    (minY()..maxY()).sumOf { y ->
        (minX()..maxX()).mapNotNull { x ->
            if (contains(Point(x, y)) && !contains(Point(x, y - 1))) x else null
        }.groups()
    }

fun Region.scanFromBottom(): Int =
    (maxY() downTo minY()).sumOf { y ->
        (minX()..maxX()).mapNotNull { x ->
            if (contains(Point(x, y)) && !contains(Point(x, y + 1))) x else null
        }.groups()
    }

fun Region.scanFromLeft(): Int =
    (minX()..maxX()).sumOf { x ->
        (minY()..maxY()).mapNotNull { y ->
            if (contains(Point(x, y)) && !contains(Point(x - 1, y))) y else null
        }.groups()
    }

fun Region.scanFromRight(): Int =
    (maxX() downTo minX()).sumOf { x ->
        (minY()..maxY()).mapNotNull { y ->
            if (contains(Point(x, y)) && !contains(Point(x + 1, y))) y else null
        }.groups()
    }

fun List<String>.parseGarden(): Garden {
    val result = mutableMapOf<Point, Char>()

    forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            result[Point(x, y)] = c
        }
    }
    return result
}