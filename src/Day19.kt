fun main() {

    fun part1(input: List<String>): Int {
        val (towels, patterns) = input.parseTowels()

        return patterns.count {
            makePattern(towels, it) > 0
        }
    }

    fun part2(input: List<String>): Long {
        val (towels, patterns) = input.parseTowels()
        return patterns.sumOf { makePattern(towels, it) }
    }

    val testInput = readInput("Day19_test")
    val input = readInput("Day19")

    println("test part1: ${part1(testInput)}")
    part1(input).writeToConsole()

    println("test part2: ${part2(testInput)}")
    part2(input).writeToConsole()
}

tailrec fun makePattern(towels: List<String>, pattern: String, cache: MutableMap<String, Long> = mutableMapOf()): Long {
    if (pattern.isEmpty()) return 1L
    return cache.getOrPut(pattern) {
        towels.filter { pattern.startsWith(it) }
            .sumOf {
                makePattern(towels, pattern.removePrefix(it), cache)
            }
    }
}

private fun List<String>.parseTowels(): Pair<List<String>, List<String>> =
    first().split(", ").sortedByDescending { it.length } to drop(2)