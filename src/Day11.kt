fun main() {
    fun part1(input: List<String>): Long {
        val stones = input.first().split(" ").map { it.toLong() }

        return stones.sumOf { it.blink(25) }
    }

    fun part2(input: List<String>): Long {
        val stones = input.first().split(" ").map { it.toLong() }

        return stones.sumOf { it.blink(75) }
    }

    val testInput = readInput("Day11_test")
    println("test part1: ${part1(testInput)}")

    val input = readInput("Day11")
    part1(input).writeToConsole()

    println("test part2: ${part2(testInput)}")
    part2(input).writeToConsole()
}

val cache = mutableMapOf<Pair<Long, Int>, Long>()

private fun Long.blink(times: Int): Long {
    if (times == 0) {
        return 1
    }
    val result =
        cache[this to times]
            ?: if (this == 0L) {
                1L.blink(times - 1)
            } else {
                val s = toString()
                if (s.length % 2 == 0) {
                    val left = s.take(s.length / 2).toLong()
                    val right = s.takeLast(s.length / 2).toLong()

                    left.blink(times - 1) + right.blink(times - 1)
                } else {
                    (this * 2024).blink(times - 1)
                }
            }

    cache[this to times] = result
    return result
}
