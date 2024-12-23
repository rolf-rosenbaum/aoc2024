import Direction.East
import Direction.North
import Direction.South
import Direction.West
import java.util.*
import kotlin.collections.ArrayDeque

typealias State = Pair<Point, Direction>

private val allDirections = listOf(East, West, North, South)
fun main() {


    fun part1(input: List<String>): Long {
        val maze = input.parseMaze()
        val start = Point(1, maze.maxY() - 1)
        val end = Point(maze.maxX() - 1, 1)

        return lowestScore(start to East, end, maze)
    }

    fun part2(input: List<String>): Int {
        val maze = input.parseMaze()
        val start = Point(1, maze.maxY() - 1)
        val end = Point(maze.maxX() - 1, 1)
        val targetScore = lowestScore(start to East, end, maze)

        return getAllShortestPathLengths(start, end, targetScore, maze)
    }

    val testInput = readInput("Day16_test")
    val input = readInput("Day16")

    println("test part1: ${part1(testInput)}")
    part1(input).writeToConsole()

    println("test part2: ${part2(testInput)}")
    part2(input).writeToConsole()
}

private fun lowestScore(start: State, end: Point, maze: Set<Point>): Long {
    val visited = HashSet<State>()
    val scores = mutableMapOf<State, Long>()
    val queue = PriorityQueue<Pair<State, Long>>(compareBy { it.second })

    queue.add((start) to 0)
    while (queue.isNotEmpty()) {
        val (pos, score) = queue.poll()
        if (pos.first == end) return score

        if (visited.contains(pos)) continue
        visited.add(pos)

        val nextMoves = pos.next()
        nextMoves.forEach { (next, cost) ->
            if (!(maze.contains(next.first) || visited.contains(next))) {
                val nextScore = score + cost
                if (nextScore < scores.getOrDefault(next, Long.MAX_VALUE)) {
                    scores[next] = nextScore
                    queue.add(next to nextScore)
                }
            }
        }
    }
    error("IMPOSSIBLE MAZE!!!")
}

private fun getAllShortestPathLengths(start: Point, end: Point, targetScore: Long, maze: Set<Point>): Int {
    val visited = mutableSetOf<State>()
    val queue = ArrayDeque<Pair<State, Long>>()
    val validPositions = mutableSetOf<Point>()

    queue.add(start to East to 0)
    while (queue.isNotEmpty()) {
        val (currentState, currentScore) = queue.removeFirst()
        validPositions.add(currentState.first)

        if (currentState.first == end) continue

        if (visited.contains(currentState)) continue
        visited.add(currentState)

        val nextMoves = currentState.next()
        for ((nextState, cost) in nextMoves) {
            if (maze.contains(nextState.first) || visited.contains(nextState)) continue
            if (currentScore + cost + lowestScore(nextState, end, maze) > targetScore) continue
            val nextScore = currentScore + cost
            queue.add(nextState to nextScore)
        }
    }

    return validPositions.count()
}

private fun State.next(): List<Step> {
    val (pos, dir) = this
    return allDirections.filterNot { it == dir.opposite() }.map {
        if (dir == it) Step(pos + dir.vector to it, 1)
        else Step(pos to it, 1000)
    }
}

private data class Step(val state: State, val cost: Int)

private fun List<String>.parseMaze(): Set<Point> {
    val maze = mutableSetOf<Point>()
    forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            if (c == '#') maze.add(Point(x, y))
        }
    }
    return maze
}

fun Direction.opposite(): Direction = when (this) {
    South -> North
    North -> South
    West -> East
    East -> West
}
