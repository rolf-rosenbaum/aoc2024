import java.util.*

fun main() {
    fun part1(input: List<String>): Int {
        return countCheats(input, 2)
    }

    fun part2(input: List<String>): Int {
        return input.parseTrack().fastest().keys.toList().findCheats(100, 20)
    }

    val testInput = readInput("Day20_test")
    val input = readInput("Day20")

    println("test part1: ${part1(testInput)}")
    part1(input).writeToConsole()

    println("test part2: ${part2(testInput)}")
    part2(input).writeToConsole()
}

private fun List<Point>.findCheats(savingsGoal: Int, cheatTime: Int): Int =
    this.indices.sumOf { start ->
        (start + savingsGoal..<this.size).count { end ->
            val physicalDistance = this[start].distanceTo(this[end])
            physicalDistance <= cheatTime && physicalDistance <= end - start - savingsGoal
        }
    }

fun countCheats(input: List<String>, size: Int): Int {
    val track = input.parseTrack()
    val fastestWithoutCheating = track.fastest()
    val savings = mutableMapOf<Pair<Point, Point>, Int>()
    fastestWithoutCheating.map { (point, distance) ->
        fastestWithoutCheating.keys
            .filter { it.distanceTo(point) <= size }
            .filter { p ->
                fastestWithoutCheating[p] != null && fastestWithoutCheating[p]!! < distance - size
            }.forEach { p ->
                savings[point to p] = distance - fastestWithoutCheating[p]!! - size
            }
    }
    return savings.count { it.value > 99 }
}

private typealias RaceStep = Pair<Point, Int>

data class Track(val maze: Set<Point>, val start: Point, val end: Point) {

    fun fastest(benchmark: Int = Int.MAX_VALUE): Map<Point, Int> {
        val visited = mutableSetOf<Point>()
        val scores = mutableMapOf<Point, Int>().apply {this[start] = 0}
        val queue = PriorityQueue<Pair<Point, Int>>(compareBy { it.second })

        queue.add(start to 0)
        while (queue.isNotEmpty()) {
            val (pos, score) = queue.poll()
            if (pos == end) return scores
            if (score > benchmark) return emptyMap()

            if (visited.contains(pos)) continue
            visited.add(pos)

            val nextMoves = pos.neighbours().filterNot { it in maze || it in visited }
            nextMoves.forEach { p ->
                val nextScore = score + 1
                if (nextScore < scores.getOrDefault(p, Int.MAX_VALUE)) {
                    scores[p] = nextScore
                    queue.add(p to nextScore)
                }
            }
        }
        error("IMPOSSIBLE MAZE!!!")
    }

    fun isWall(it: Point) = it.x == 0 || it.y == 0 ||
            it.x == maze.maxX() || it.y == maze.maxY()


}

private fun List<String>.parseTrack(): Track {
    val maze = mutableSetOf<Point>()
    var start = Point(-1, -1)
    var end = Point(-1, -1)
    forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            if (c == '#') maze.add(Point(x, y))
            if (c == 'S') start = Point(x, y)
            if (c == 'E') end = Point(x, y)
        }
    }
    return Track(maze, start, end)
}
