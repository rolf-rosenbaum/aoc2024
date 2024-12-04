fun main() {

    fun part1(input: List<String>): Int {
        return addMultiplications(input.joinToString(""))
    }

    fun part2(input: List<String>): Int {
        return addMultiplications2(input.joinToString(""))
    }

    val testInput = readInput("Day03_test")
    part1(testInput).writeToConsole()

    val input = readInput("Day03")
    part1(input).writeToConsole()

    part2(testInput).writeToConsole()
    part2(input).writeToConsole()
}

val multiplications = "mul\\(\\d{1,3},\\d{1,3}\\)".toRegex()

private fun addMultiplications(line: String) = multiplications.findAll(line).sumOf {
    it.value.split(",").let { factors ->
        factors.first().drop(4).toInt() * factors.second().dropLast(1).toInt()
    }
}

private fun addMultiplications2(line: String): Int {
    var rest = line
    var sum = 0
    var pos: Int

    while (rest.isNotEmpty()) {
        // take all to first don't()
        pos = rest.indexOf("don't")
        if (pos == -1) pos = rest.length
        sum += addMultiplications(rest.take(pos))

        // drop the scanned part
        rest = rest.drop(pos)

        // skip to next do()
        pos = rest.indexOf("do()")
        if (pos == -1) pos = rest.length
        rest = rest.drop(pos)
    }

    return sum
}