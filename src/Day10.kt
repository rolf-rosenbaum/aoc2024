import java.util.*

typealias Trail = Map<Point, Int>

fun main() {
    fun part1(input: List<String>): Int {

        return input.parseTrail().scoreAllHikes()
    }

    fun part2(input: List<String>): Int {
        return input.parseTrail().scoreAllHikes(true)
    }

    val testInput = readInput("Day10_test")
    println("test part1: ${part1(testInput)}")

    val input = readInput("Day10")
    part1(input).writeToConsole()

    println("test part2: ${part2(testInput)}")
    part2(input).writeToConsole()
}

private fun Trail.scoreAllHikes(rating: Boolean = false): Int =
    filter { (_, h) -> h == 0 }.keys
        .sumOf {
            scoreHikes(it, rating)
        }

fun Trail.scoreHikes(start: Point, rating: Boolean = false): Int {
    var score = 0
    val visited = mutableSetOf<Point>()
    val queue = LinkedList<Point>().apply { add(start) }
    while (queue.isNotEmpty()) {
        val next = queue.poll()
        if (visited.add(next) || rating) {
            if (this[next] == 9) score++
            queue.addAll(next.neighbours().filter { n -> this[n] == this[next]!! + 1 })
        }
    }
    return score
}

private fun List<String>.parseTrail(): Trail {
    val trail = mutableMapOf<Point, Int>()
    forEachIndexed { y, line ->
        line.forEachIndexed { x, n ->
            trail[Point(x, y)] = n.digitToInt()
        }
    }
    return trail
}