fun main() {

    fun part1(input: List<String>): Int {
        val puzzle = input.parseToWordPuzzle()

        val maxX = puzzle.maxOf { it.key.x }
        val minX = puzzle.minOf { it.key.x }
        val maxY = puzzle.maxOf { it.key.y }
        val minY = puzzle.minOf { it.key.y }

        val xPoints = puzzle.filter { it.value == 'X' }
        val mPoints = puzzle.filter { it.value == 'M' }
        val aPoints = puzzle.filter { it.value == 'A' }
        val sPoints = puzzle.filter { it.value == 'S' }

        val candidates = mutableListOf<List<Point>>()
        xPoints.forEach { (xp, _) ->
            val xmPoints = xp.allNeighbours(minX, maxX, minY, maxY).filter { mPoints.keys.contains(it) }
            mPoints.filter { it.key in xmPoints }.forEach { (mp, _) ->
                val xmaPoints = mp.allNeighbours(minX, maxX, minY, maxY)
                    .filter { aPoints.keys.contains(it) && it - mp == mp - xp }

                aPoints.filter { it.key in xmaPoints }.forEach { (ap, _) ->
                    val xmasPoints = ap.allNeighbours(minX, maxX, minY, maxY)
                        .filter { sPoints.keys.contains(it) && it - ap == ap - mp }
                    if (xmasPoints.isNotEmpty()) {
                        candidates.add(listOf(xp, mp, ap))
                    }
                }
            }
        }
        return candidates.size
    }

    fun part2(input: List<String>): Int {
        val puzzle = input.parseToWordPuzzle()

        val maxX = puzzle.maxOf { it.key.x }
        val minX = puzzle.minOf { it.key.x }
        val maxY = puzzle.maxOf { it.key.y }
        val minY = puzzle.minOf { it.key.y }

        val mPoints = puzzle.filter { it.value == 'M' }
        val aPoints = puzzle.filter { it.value == 'A' }
        val sPoints = puzzle.filter { it.value == 'S' }

        val candidates = mutableListOf<List<Point>>()

        mPoints.forEach { (mp, _) ->
            val maPoints = mp.allNeighbours(minX, maxX, minY, maxY).filter { aPoints.keys.contains(it) }

            aPoints.filter { it.key in maPoints }.forEach { (ap, _) ->
                val masPoints = ap.allNeighbours(minX, maxX, minY, maxY)
                    .filter { sPoints.keys.contains(it) && it - ap == ap - mp }
                if (masPoints.isNotEmpty()) {
                    candidates.add(listOf(mp, ap, sPoints.keys.first {
                        it.allNeighbours(minX, maxX, minY, maxY).contains(ap) && it - ap == ap - mp
                    }))
                }
            }
        }
        return candidates.filter { c -> candidates.any { it.isCrossWith(c) } }.size / 2
    }

    val testInput = readInput("Day04_test")
    part1(testInput).writeToConsole()

    val input = readInput("Day04")
    part1(input).writeToConsole()

    part2(testInput).writeToConsole()
    part2(input).writeToConsole()
}

private fun List<String>.parseToWordPuzzle(): Map<Point, Char> {
    val puzzle = mutableMapOf<Point, Char>()

    this.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            puzzle[Point(x, y)] = c
        }
    }
    return puzzle
}

private fun List<Point>.isCrossWith(other: List<Point>): Boolean {
    if (this.size != 3 || other.size != 3) return false
    if (this.second() != other.second()) return false
    val a = this.second()
    return (this + other).toSet().containsAll(
        listOf(
            a,
            a + Point(1, 1),
            a + Point(-1, -1),
            a + Point(1, -1),
            a + Point(-1, 1),
        )
    )
}

