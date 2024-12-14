fun main() {

    fun part1(input: List<String>, test: Boolean = false): Int {
        val robots = input.parseRobots()

        val maxX = if (test) 10 else 100
        val maxY = if (test) 6 else 102
        val midX = if (test) 5 else 50
        val midY = if (test) 3 else 51

        val result = generateSequence(robots) {
            it.step(maxX, maxY)
        }.drop(100).first()
        result.prettyPrint(maxX, maxY)
        return result.safetyFactor(midX, midY)

    }

    fun part2(input: List<String>, test: Boolean = false): Int {
        val robots = input.parseRobots()

        val maxX = if (test) 10 else 100
        val maxY = if (test) 6 else 102
        var count = 0
        val tree = generateSequence(robots) {
            it.step(maxX, maxY).also { robots ->
                robots.prettyPrint()
                count++
                print("$count\r")
            }
        }.dropWhile { !it.hasChristmasTree() }.first()
        println(tree.prettyPrint(maxX, maxY))
        return count
    }

    val testInput = readInput("Day14_test")
    val input = readInput("Day14")

    println("test part1: ${part1(testInput, true)}")
    part1(input).writeToConsole()

//    println("test part2: ${part2(testInput, true)}")
    part2(input).writeToConsole()
}

data class Robot(val position: Point, val vector: Vector)

fun List<Robot>.prettyPrint(maxX: Int = 10, maxY: Int = 6): String =
    buildString {
        (0..maxY).forEach { y ->
            (0..maxX).forEach { x ->
                val robots = this@prettyPrint.count { it.position == Point(x, y) }
                append(if (robots > 0) robots else ".")
            }
            append("\n")
        }
    }

fun List<Robot>.hasChristmasTree(): Boolean = prettyPrint(100, 102).contains("1111111111")

fun List<Robot>.step(maxX: Int, maxY: Int): List<Robot> =
    map {
        val newX = (it.position.x + it.vector.x + maxX + 1) % (maxX + 1)
        val newY = (it.position.y + it.vector.y + maxY + 1) % (maxY + 1)
        Robot(
            position = Point(x = newX, y = newY),
            vector = it.vector
        )
    }

fun List<Robot>.safetyFactor(midX: Int, midY: Int) =
    count { it.position.x < midX && it.position.y < midY } *
            count { it.position.x > midX && it.position.y < midY } *
            count { it.position.x < midX && it.position.y > midY } *
            count { it.position.x > midX && it.position.y > midY }

fun List<String>.parseRobots(): List<Robot> {
    val robots = mutableListOf<Robot>()
    map { line ->
        val (position, vector) = line.split(" ")
        val (xP, yP) = position.split(",")
        val (xV, yV) = vector.split(",")
        val pos = Point(xP.drop(2).toInt(), yP.toInt())
        val vec = Vector(xV.drop(2).toInt(), yV.toInt())
        robots.add(Robot(pos, vec))
    }
    return robots
}
