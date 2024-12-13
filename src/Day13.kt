fun main() {

    fun part1(input: List<String>): Long {

        val machines = input.parseToClawMachines()
        val solutions = machines.map { it.solve() }

        return solutions.sumOf { it.first * 3 + it.second }
    }

    fun part2(input: List<String>): Long {
        val machines = input.parseToClawMachines(10000000000000)
        val solutions = machines.map { it.solve() }
        return solutions.sumOf {
            it.first * 3 + it.second
        }
    }

    val testInput = readInput("Day13_test")
    val input = readInput("Day13")

    println("test part1: ${part1(testInput)}")
    part1(input).writeToConsole()

    println("test part2: ${part2(testInput)}")
    part2(input).writeToConsole()
}

data class ClawMachine(val buttonA: VectorLong, val buttonB: VectorLong, val prizeLocation: VectorLong) {
    fun solve(): Pair<Long, Long> {
        val delta = buttonA.x * buttonB.y - buttonA.y * buttonB.x
        val delta1 = prizeLocation.x * buttonB.y - prizeLocation.y * buttonB.x
        val delta2 = prizeLocation.y * buttonA.x - prizeLocation.x * buttonA.y
        val first = delta1 / delta
        val second = delta2 / delta

        if (delta1 % delta != 0L || delta2 % delta != 0L) {
            return 0L to 0L
        }
        return first to second
    }
}

fun List<String>.parseToClawMachines(addition: Long = 0L): List<ClawMachine> {
    val machines = mutableListOf<ClawMachine>()
    val numRegex = """\D+X[+=](\d+), Y[+=](\d+)""".toRegex()

    this.filter { it.isNotEmpty() }.chunked(3)
        .forEach {
            val (xA, yA) = numRegex.find(it.first())?.groupValues?.drop(1)?.map { it.toLong() } ?: error("parse error")
            val (xB, yB) = numRegex.find(it.second())?.groupValues?.drop(1)?.map { it.toLong() } ?: error("parse error")
            val (xP, yP) = numRegex.find(it.last())?.groupValues?.drop(1)?.map { it.toLong() } ?: error("parse error")
            machines.add(ClawMachine(VectorLong(xA, yA), VectorLong(xB, yB), VectorLong(xP + addition, yP + addition)))
        }
    return machines
}

data class VectorLong(val x: Long, val y: Long)