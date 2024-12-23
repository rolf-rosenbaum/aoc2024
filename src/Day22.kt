fun main() {


    fun part1(input: List<String>): Long {

        val secrets = input.toLong().map { secret ->
            generateSequence(secret) { s ->
                s.nextSecretNumber()
            }.drop(2000).first()
        }
        return secrets.sum()
    }

    fun part2(input: List<String>): Int {
        return buildMap {
            input.toLong().map { secret ->
                generateSequence(secret) { s ->
                    s.nextSecretNumber()
                }.take(2001).map { (it % 10).toInt() }.toList()
            }.forEach { sequence ->
                sequence.windowed(5).map { it.zipWithNext { first, second -> second - first } to it.last() }
                    .distinctBy { it.first }.forEach { (k, v) -> this[k] = (this[k] ?: 0) + v }
            }
        }.maxOf { it.value }
    }

    val testInput = readInput("Day22_test")
    val input = readInput("Day22")

    println("test part1: ${part1(testInput)}")
    part1(input).writeToConsole()

    println("test part2: ${part2(testInput)}")
    part2(input).writeToConsole()
}

fun List<String>.toLong() = map(String::toLong)
fun Long.prune() = this % 16777216L
fun Long.mix(other: Long) = other xor this
fun Long.nextSecretNumber(): Long =
    (this * 64).mix(this).prune()
        .let {
            (it / 32).mix(it).prune()
        }.let {
            (it * 2048L).mix(it).prune()
        }
