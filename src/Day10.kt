import java.util.*

typealias Trail = Map<Point, Int>

fun main() {
    fun part1(input: List<String>): Int {

        return input.parseTrail().scoreHikes()
    }

    fun part2(input: List<String>): Int {
        return input.parseTrail().scoreHikes(true)
    }

    val testInput = readInput("Day10_test")
    println("test part1: ${part1(testInput)}")

    val input = readInput("Day10")
    part1(input).writeToConsole()

    println("test part2: ${part2(testInput)}")
    part2(input).writeToConsole()
}

private fun Trail.scoreHikes(rating: Boolean = false): Int {
    val startPoints = filter { (_, h) ->
        h == 0
    }.keys

    return startPoints.sumOf {
        this.scoreHikes(it, rating)
    }
}

fun Trail.scoreHikes(start: Point, rating: Boolean = false): Int {
    val visited = mutableSetOf<Point>()
    var score = 0
    val queue = LinkedList<Point>().apply { add(start) }

    while (queue.isNotEmpty()) {
        val next = queue.poll()
        if (visited.add(next) || rating) {
            if (this[next] == 9) score++
            queue.addAll(next.neighbours().filter { this[it] == this[next]!! + 1 })
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