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
            n * second.count{it == n}
        }
    }

    // Test if implementation meets criteria from the description, like:
//    check(part1(listOf("test_input")) == 1)

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day01_test")
//    check(part1(testInput) == 1)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).writeToConsole()
    part2(input).writeToConsole()
}

fun List<String>.parseLists(): Pair<List<Int>, List<Int>> =
    map { it.split("   ").first().toInt() }.sorted() to
            map { it.split("   ").second().toInt() }.sorted()



