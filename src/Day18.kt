import java.util.*

fun main() {

    fun part1(input: List<String>, test: Boolean = false): Int {
        val num = if (test) 12 else 1024
        val end = if (test) Point(6, 6) else Point(70, 70)
        val corrupted = input.parseFallingBytes().take(num).toSet()
        return findShortestPath(corrupted, end)
    }

    fun part2(input: List<String>, test: Boolean = false): String {
        var num = if (test) 12 else 1024
        val end = if (test) Point(6, 6) else Point(70, 70)
        val corrupted = input.parseFallingBytes().toList()

        do {
            num++
            val n = findShortestPath(corrupted.take(num).toSet(), end)

        } while (n != -1)
        println("num: $num")
        return corrupted[num - 1].toString()
    }

    val testInput = readInput("Day18_test")
    val input = readInput("Day18")

    println("test part1: ${part1(testInput, true)}")
    part1(input).writeToConsole()

    println("test part2: ${part2(testInput, true)}")
    part2(input).writeToConsole()
}

private fun List<String>.parseFallingBytes(): Set<Point> {
    return mutableSetOf<Point>().let {
        this.forEach { line ->
            val (x, y) = line.split(",").map(String::toInt)
            it.add(Point(x, y))
        }
        it
    }
}

fun prettyPrint(maze: Set<Point>, steps: Set<Point>, test: Boolean = false): String = buildString {
    var left = true
    val maxX = if (test) 6 else 70
    val maxY = if (test) 6 else 70
    (0..maxY).forEach { y ->
        (0..maxX).forEach { x ->
            val p = Point(x, y)
            if (maze.contains(p)) append("#") else
                if (steps.contains(p)) append("O") else append(".")
        }
        append("\n")
    }
}

fun findShortestPath(maze: Set<Point>, end: Point): Int {
    val visited = mutableSetOf<Point>()
    val scores = mutableMapOf<Point, Int>()
    val queue = PriorityQueue<Pair<Point, Int>>(compareBy { it.second })

    val start = Point(0, 0)

    queue.add((start) to 0)
    while (queue.isNotEmpty()) {
        val (pos, score) = queue.poll()
        if (pos == end) return score
        if (visited.contains(pos)) continue

        visited.add(pos)
        val nextMoves = pos.neighbours().filter {
            it !in maze &&
                    it.x >= 0 &&
                    it.y >= 0 &&
                    it.x <= end.x &&
                    it.y <= end.y
        }
        nextMoves.forEach {
            if (!(maze.contains(it) || visited.contains(it))) {
                val nextScore = score + 1
                if (nextScore < scores.getOrDefault(it, Int.MAX_VALUE)) {
                    scores[it] = nextScore
                    queue.add(it to nextScore)
                }
            }
        }
    }
    return -1

}