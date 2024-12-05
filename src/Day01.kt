import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val (first, second) = input.parseLists()
        return first.indices.sumOf {
            abs(first[it] - second[it])
        }
    }

    fun part2(input: List<String>): Int {
        val (first, second) = input.parseLists()
        return first.sumOf { n ->
            n * second.count { it == n }
        }
    }

    val input = readInput("Day01")
    part1(input).writeToConsole()
    part2(input).writeToConsole()
}

private fun List<String>.parseLists(): Pair<List<Int>, List<Int>> =
    map { it.split("   ").first().toInt() }.sorted() to
            map { it.split("   ").second().toInt() }.sorted()



