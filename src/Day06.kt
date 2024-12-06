fun main() {

    fun part1(input: List<String>): Int? {
        return input.parseRoom().recordPath()?.size
    }

    fun part2(input: List<String>): Int {
        return input.parseRoom()
            .recordPath()
            ?.drop(1) // start position cannot get an obstacle
            ?.count { candidate ->
                // path == null means there is a loop
                (input.parseRoom() + candidate).recordPath() == null
            } ?: error("NO PATH")
    }

    val testInput = readInput("Day06_test")
    println("test part1: ${part1(testInput)}")

    val input = readInput("Day06")
    part1(input).writeToConsole()

    println("test part2: ${part2(testInput)}")
    part2(input).writeToConsole()
}

data class Room(
    val obstacles: Set<Point>,
    val maxX: Int,
    val maxY: Int,
    val start: Point
) {
    private val directions = listOf(
        Vector(0, -1),
        Vector(1, 0),
        Vector(0, 1),
        Vector(-1, 0),
    )

    operator fun plus(p: Point) = copy(obstacles = obstacles + p)

    fun recordPath(direction: Vector = directions.first()): Set<Point>? {
        var pos = start
        var dir = direction
        val path = mutableSetOf(start to dir)
        var turnCount = 0

        while ((pos + dir).isInRoom()) {
            val next = pos + dir
            if (!obstacles.contains(next)) {
                if (!path.add(next to dir)) return null // loop found -> return null
                pos = next
            } else
                dir = directions[turnCount++ % 4]
        }
        return path.map { it.first }.toSet()
    }
    private fun Point.isInRoom(): Boolean = x in 0..maxX && y in 0..maxY
}


private fun List<String>.parseRoom(): Room {
    var startPos = Point(-1, -1)
    return Room(
        flatMapIndexed { y: Int, line: String ->
            line.mapIndexedNotNull() { x, c ->
                if (c == '^') startPos = Point(x, y)
                if (c == '#')
                    Point(x, y)
                else
                    null
            }
        }.toSet(),
        first().length - 1,
        size - 1,
        startPos
    )
}
