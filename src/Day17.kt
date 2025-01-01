import kotlin.math.pow

fun main() {

    fun part1(input: List<String>): String {
        return input.parseComputer().compute().joinToString(",")

    }

    fun part2(input: List<String>): Long {
        val computer = input.parseComputer()
        return computer.program
            .reversed()
            .map { it.toLong() }
            .fold(listOf(0L)) { candidates, instruction ->
                candidates.flatMap { candidate ->
                    val shifted = candidate * 8
                    (shifted..shifted + 8).mapNotNull { n ->
                        n.takeIf {
                            computer.copy(regA = n).compute().firstOrNull()?.toLong() == instruction
                        }
                    }
                }
            }.first()
    }

    val testInput = readInput("Day17_test")
    val input = readInput("Day17")

    println("test part1: ${part1(testInput)}")
    part1(input).writeToConsole()

    println("test part2: ${part2(testInput)}")
    part2(input).writeToConsole()
}

typealias Instruction = Pair<Int, Int>

data class Computer(
    val regA: Long,
    val regB: Long,
    val regC: Long,
    val program: List<Int>,
    val pointer: Int,
    val running: Boolean = true,
    val output: List<Long>
) {

    fun compute(): List<Long> {
        var c = this
        do {
            c = c.execute()
        } while (c.running)
        return c.output
    }

    val instructions: List<Instruction>
        get() = program.windowed(2, 2) { it.first() to it.second() }

    fun execute(): Computer {
        if (pointer >= instructions.size) return copy(running = false)
        return execute(instructions[pointer])
    }

    private fun execute(instruction: Instruction): Computer {
        val op = instruction.second
        when (instruction.first) {
            0 -> return copy(regA = regA / 2.0.pow(comboOperand(op).toInt()).toInt(), pointer = pointer + 1)
            1 -> return copy(regB = regB xor op.toLong(), pointer = pointer + 1)
            2 -> return copy(regB = comboOperand(op) % 8, pointer = pointer + 1)
            3 -> return if (regA == 0L) copy(pointer = pointer + 1) else copy(pointer = op)
            4 -> return copy(regB = regB xor regC, pointer = pointer + 1)
            5 -> return copy(pointer = pointer + 1, output = output + comboOperand(op) % 8)
            6 -> return copy(regB = regA / 2.0.pow(comboOperand(op).toInt()).toInt(), pointer = pointer + 1)
            7 -> return copy(regC = regA / 2.0.pow(comboOperand(op).toInt()).toInt(), pointer = pointer + 1)
            else -> error("Kernel panic")
        }

    }

    fun comboOperand(op: Int): Long {
        return when (op) {
            0, 1, 2, 3 -> op.toLong()
            4 -> regA
            5 -> regB
            6 -> regC
            else -> error("illegal operand")
        }
    }

}

private fun List<String>.parseComputer(): Computer {
    val (regA, regB, regC) = take(3).map { it.split(": ").second().toInt() }
    val program = last().split(": ").second().split(",").map(String::toInt)
    return Computer(
        regA = regA.toLong(),
        regB = regB.toLong(),
        regC = regC.toLong(),
        program = program,
        pointer = 0,
        output = emptyList()
    )
}