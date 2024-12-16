import Direction.East
import Direction.North
import Direction.South
import Direction.West
import java.util.*

fun main() {

    fun part1(input: List<String>, test: Boolean = false): Int {

        return generateSequence(input.parseWarehouse()) {
            it.robotMove()
        }.dropWhile { it.step < it.moves.size }.first().boxes.sumOf {
            it.x + 100 * it.y
        }
    }

    fun part2(input: List<String>, test: Boolean = false): Int {
        val wh = input.map {
            it.replace(".", "..")
                .replace("O", "[]")
                .replace("#", "##")
                .replace("@", "@.")
        }.parseWarehouse(true)
        println(wh.prettyPrint(true))

        val finished = generateSequence(wh) {
            it.robotMove2().also { println(it.prettyPrint(true)) }
        }.dropWhile { it.step < it.moves.size }.first()
        println(finished.prettyPrint(true))
        return 0
    }

    val testInput = readInput("Day15_test")
    val input = readInput("Day15")

    println("test part1: ${part1(testInput, true)}")
    part1(input).writeToConsole()

    println("test part2: ${part2(testInput, true)}")
//    part2(input).writeToConsole()
}

data class Warehouse(
    val walls: Set<Point>,
    val boxes: MutableSet<Point>,
    val robot: Point,
    val moves: List<Direction>, val step: Int = 0
) {

    val maxX = walls.maxOf { it.x }
    val maxY = walls.maxOf { it.y }

    fun robotMove(): Warehouse {
        val direction = moves[step]
        val nextRobot = robot + direction.vector
        return if (walls.contains(nextRobot)) {
            copy(step = step + 1)
        } else if (boxes.contains(nextRobot)) {
            val adjacentBoxes = (listOf(nextRobot) + generateSequence(nextRobot) {
                it + direction.vector
            }.takeWhile {
                boxes.contains(it)
            }).toSet()
            if (walls.contains(adjacentBoxes.last() + direction.vector)) {
                copy(step = step + 1)
            } else {
                adjacentBoxes.reversed().forEach {
                    boxes.remove(it)
                    boxes.add(it + direction.vector)
                }
                copy(robot = nextRobot, step = step + 1)
            }
        } else
            copy(robot = nextRobot, step = step + 1)
    }

    fun robotMove2(): Warehouse {
        val direction = moves[step]
        println(direction)
        if (direction in listOf(East, West)) return robotMove()

        val nextRobot = robot + direction.vector
        return if (walls.contains(nextRobot)) {
            copy(step = step + 1)
        } else if (boxes.contains(nextRobot)) {
            val adjacentBoxes = adjacentBoxes(nextRobot, direction)
            if (walls.contains(adjacentBoxes.last() + direction.vector)) {
                copy(step = step + 1)
            } else {
                adjacentBoxes.reversed().forEach {
                    boxes.remove(it)
                    boxes.add(it + direction.vector)
                }
                copy(robot = nextRobot, step = step + 1)
            }
        } else
            copy(robot = nextRobot, step = step + 1)
    }

    fun prettyPrint(part2: Boolean = false): String = buildString {
        var left = true
        (0..maxY).forEach { y ->
            (0..maxX).forEach { x ->
                val p = Point(x, y)
                if (walls.contains(p)) append("#") else
                    if (part2)
                        if (boxes.contains(p)) append(if (left) ("[") else "]").also { left = !left }
                        else if (boxes.contains(p)) append("O")
                else
                if (robot == p) append("@") else append(".")
            }
            append("\n")
        }
    }

    fun adjacentBoxes(point: Point, direction: Direction): Set<Point> {
        val visited = mutableSetOf<Point>()
        val queue = LinkedList<Point>().apply { add(point) }
        while (queue.isNotEmpty()) {
            val next = queue.poll()
            if (visited.add(next)) {
                if (boxes.contains(next + direction.vector)) {
                    queue.add(next)
                    if (boxes.count {b->b.x < next.x} % 2 == 0) {
                        queue.add(next + East.vector)
                        queue.add(next + direction.vector + East.vector)
                    } else {
                        queue.add(next + West.vector)
                        queue.add(next + direction.vector + West.vector)
                    }
                }
            }
        }
        return visited

    }
}


fun List<String>.parseWarehouse(part2: Boolean = false): Warehouse {
    val (wh, m) = filter { it.isNotEmpty() }.partition { it.startsWith("#") }
    val walls = mutableSetOf<Point>()
    val boxes = mutableSetOf<Point>()
    var robot = Point(-1, -1)
    wh.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            val point = Point(x, y)
            if (part2) {
                if (c == '#') walls.add(point)
                if (c == '[' || c == ']') {
                    boxes.add(point)
                }
                if (c == '@') robot = point
            } else {
                if (c == '#') walls.add(point)
                if (c == 'O') boxes.add(point)
                if (c == '@') robot = point
            }
        }
    }
    val moves = mutableListOf<Direction>()
    m.joinToString("").forEach {
        moves.add(
            when (it) {
                '^' -> North
                '>' -> East
                'v' -> South
                '<' -> West
                else -> error("unkown dirction $it")
            }
        )
    }
    return Warehouse(walls = walls, boxes = boxes, robot = robot, moves = moves)
}
