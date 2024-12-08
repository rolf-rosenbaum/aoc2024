fun main() {

    fun part1(input: List<String>): Int {
        return input.parseCity().findAntinodes().size
    }

    fun part2(input: List<String>): Int {
        return input.parseCity().findAntinodes2().size
    }

    val testInput = readInput("Day08_test")
    println("test part1: ${part1(testInput)}")

    val input = readInput("Day08")
    part1(input).writeToConsole()

    println("test part2: ${part2(testInput)}")
    part2(input).writeToConsole()
}

data class City(val antennas: Map<Char, List<Point>>, val maxX: Int, val maxY: Int) {

    fun findAntinodes2(): List<Point> {
        return antennas.map { (_, points) ->
            points.findAntinodes2()
        }.flatten()
            .distinct()
    }

    private fun List<Point>.findAntinodes2(): Set<Point> {
        val antinodes = mutableSetOf<Point>()
        this.forEach { first ->
            (this - first).forEach { second ->
                val diff = second - first
                antinodes.addAll(generateSequence(first) { p -> p - diff }.takeWhile { it.isInCity() })
                antinodes.addAll(generateSequence(second) { p -> p + diff }.takeWhile { it.isInCity() })
            }
        }
        return antinodes
    }

    fun findAntinodes(): List<Point> {
        return antennas.map { (_, points) ->
            points.findAntinodes()
        }.flatten()
            .distinct()
    }

    private fun Point.isInCity() = x in 0..maxX && y in 0..maxY
    private fun List<Point>.findAntinodes(): Set<Point> {
        val antinodes = mutableSetOf<Point>()
        this.forEach { first ->
            this.forEach { second ->
                if (second != first) {
                    val diff = second - first
                    antinodes.add(first - diff)
                    antinodes.add(second + diff)
                }
            }
        }
        return antinodes.filter { it.isInCity() }.toSet()
    }
}

private fun List<String>.parseCity(): City {
    val antennas = mutableMapOf<Char, MutableList<Point>>()

    mapIndexed { y, line ->
        line.mapIndexed { x, c ->
            if (c != '.')
                if (antennas[c]?.add(Point(x, y)) != true) antennas[c] = mutableListOf(Point(x, y))
        }
    }
    return City(antennas, first().length - 1, size - 1)

}
