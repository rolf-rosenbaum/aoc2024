import Ops.CONCAT
import Ops.PLUS
import Ops.TIMES
import kotlin.math.pow

fun main() {

    fun part1(input: List<String>): Long {
        return input.parseEquations().sumOf { eq ->
            eq.allResults(PLUS, TIMES).filter { it == eq.expectedResult }.distinct().sum()
        }
    }

    fun part2(input: List<String>): Long {
        return input.parseEquations().sumOf { eq ->
            eq.allResults(PLUS, TIMES, CONCAT).filter { it == eq.expectedResult }.distinct().sum()
        }
    }

    val testInput = readInput("Day07_test")
    println("test part1: ${part1(testInput)}")

    val input = readInput("Day07")
    part1(input).writeToConsole()

    println("test part2: ${part2(testInput)}")
    part2(input).writeToConsole()
}

data class Equation(val expectedResult: Long, val operands: List<Long>) {

    fun allResults(vararg ops: Ops): List<Long> = mutableListOf<Long>().let { results ->
        operationsFor(operands.size, ops.toList()).forEach {
            results.add(it.execute())
        }
        results
    }

    private fun operationsFor(n: Int, ops: List<Ops>): List<List<Ops>> = mutableListOf<List<Ops>>().let {
        val opsCount = ops.size.toDouble().pow(n - 1).toInt()
        (0..<opsCount).forEach { i ->
            val l = mutableListOf<Ops>()
            i.toString(ops.size).padStart((opsCount - 1).toString(ops.size).length, '0').forEach { c ->
                l.add(ops[c.digitToInt()])
            }
            it.add(l)
        }
        it
    }

    private fun List<Ops>.execute(): Long {
        var result = operands.first()
        operands.drop(1).indices.forEach {
            result = this[it].operation(result, operands[it + 1])
        }
        return result
    }
}

enum class Ops(val operation: (Long, Long) -> Long) {
    PLUS({ a, b -> a + b }),
    TIMES({ a, b -> a * b }),
    CONCAT({ a, b -> "$a$b".toLong() })
}

fun List<String>.parseEquations(): List<Equation> = map { line ->
    val (result, rest) = line.split(": ")
    Equation(result.toLong(), rest.split(" ").map { it.toLong() })
}
