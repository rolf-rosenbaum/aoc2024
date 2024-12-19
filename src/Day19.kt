
fun main() {

    fun part1(input: List<String>): Int {
        val (towels, patterns) = input.parseTowels()

        return patterns.count {
            it.matchesPatterns(towels)
        }

    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("Day19_test")
    val input = readInput("Day19")

    println("test part1: ${part1(testInput)}")
    part1(input).writeToConsole()

    println("test part2: ${part2(testInput)}")
    part2(input).writeToConsole()
}

private fun CharSequence.matchesPatterns(towels: List<String>): Boolean {
    generateSequence(this) {
        it
    }

    return true
}

private fun List<String>.parseTowels(): Pair<List<String>, List<String>> =
    first().split(", ").sortedByDescending { it.length } to drop(2)