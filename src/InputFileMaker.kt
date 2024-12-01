import java.io.File

fun main() {
    (1..25).map {
        it.toString().padStart(2, '0')
    }.forEach {

        val name = "Day$it.txt"
        val testName = "Day${it}_test.txt"

        val file = File("input/$name")
        file.createNewFile()

        val testFile = File("input/$testName")
        testFile.createNewFile()
    }
}
