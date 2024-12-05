private typealias Rule = Pair<Int, Int>
private typealias Update = IntArray

fun main() {
    fun part1(input: List<String>): Int {
        val (rules, updates) = input.parseInput()

        return updates.filter {
            it.isCorrectlyOrdered(rules)
        }.sumOf { it[it.size / 2] }
    }

    fun part2(input: List<String>): Int {
        val (rules, updates) = input.parseInput()

        return updates.filter {
            !it.isCorrectlyOrdered(rules)
        }.map {
            it.orderBy(rules)
        }.sumOf { it[it.size / 2] }
    }

    val testInput = readInput("Day05_test")
    println("test part1: ${part1(testInput)}")

    val input = readInput("Day05")
    part1(input).writeToConsole()

    println("test part2: ${part2(testInput)}")
    part2(input).writeToConsole()
}

private fun IntArray.orderBy(rules: List<Rule>): IntArray {
    while (!isCorrectlyOrdered(rules)) {
        rules.forEach { rule ->
            val f = this.firstOrNull { it == rule.first }
            val s = this.firstOrNull { it == rule.second }
            if (f != null && s != null) {
                if (indexOf(f) > indexOf(s)) {
                    val tmp = this[indexOf(f)]
                    this[indexOf(f)] = s
                    this[indexOf(s)] = tmp
                }
            }
        }
    }
    return this
}

private fun IntArray.isCorrectlyOrdered(rules: List<Rule>): Boolean {
    rules.forEach { rule ->
        val f = this.firstOrNull { it == rule.first }
        val s = this.firstOrNull { it == rule.second }
        if (f != null && s != null) {
            if (indexOf(f) > indexOf(s)) return false
        }
    }
    return true
}

private fun List<String>.parseInput(): Pair<List<Rule>, List<Update>> {
    val rules = takeWhile { it.isNotBlank() }.map { line ->
        line.split("|").let { it.first().toInt() to it.second().toInt() }
    }
    val updates = dropWhile { it.isNotBlank() }.drop(1).map { line ->
        line.split(",").map(String::toInt).toIntArray()
    }
    return rules to updates
}
