fun main() {
    fun part1(input: List<String>): Long {

        val disk = input.toDisk()
        val repacked = generateSequence(disk.dropLastWhile { it == "." }.toMutableList()) { s ->
            val foo = s.last()
            s[s.indexOf(".")] = foo
            s.dropLast(1).toMutableList()
        }.takeWhile { it.contains(".") }.last().dropLast(1)

        return repacked.map { it.toLong() }.checkSum()
    }

    fun part2(input: List<String>): Long {
        val disk = input.toDisk2()
        (disk.maxOf { it.second } downTo 0).forEach { index ->
            val candidate = disk.first { it.second == index }
            val freeSpace = disk.firstOrNull { it.second == -1 && it.first >= candidate.first }
            if (freeSpace != null) {
                if (freeSpace.first > candidate.first) {
                    val i = disk.indexOf(freeSpace)
                    disk.removeAt(i)
                    disk.add(i, candidate)
                    disk.add(i+1, (freeSpace.first - candidate.first) to -1)
                    val j = disk.lastIndexOf(candidate)
                    disk.removeAt(j)
                    disk.add(j, candidate.first to -1)
                } else
                    disk.swap(candidate, freeSpace)
            }
        }
        return disk.checksum()
    }

    val testInput = readInput("Day09_test")
    println("test part1: ${part1(testInput)}")

    val input = readInput("Day09")
    part1(input).writeToConsole()

    println("test part2: ${part2(testInput)}")
    part2(input).writeToConsole()
}

fun List<Pair<Int, Int>>.checksum(): Long {
    var sum = 0L
    var index = 0
    map { p ->
        for (i in 0 until p.first) {
            if (p.second != -1)
                sum+= index * p.second
            index++
        }
    }
    return sum
}

fun List<String>.toDisk(): MutableList<String> {
    var freeSpace = false
    val disk = mutableListOf<String>()
    var index = 0
    first().forEach { c ->
        val size = c.digitToInt()
        if (freeSpace) {
            repeat(size) { disk.add(".") }
        } else {
            repeat(size) { disk.add(index.toString()) }
            index++
        }
        freeSpace = !freeSpace
    }
    return disk
}

fun List<String>.toDisk2(): MutableList<Pair<Int, Int>> {
    var freeSpace = false
    val disk = mutableListOf<Pair<Int, Int>>()
    var index = 0
    first().forEach { c ->
        val size = c.digitToInt()
        if (freeSpace) {
            disk.add(size to -1)
        } else {
            disk.add(size to index)
            index++
        }
        freeSpace = !freeSpace
    }
    return disk
}

fun List<Long>.checkSum(): Long = mapIndexed { index, c ->
    index * c
}.sum()