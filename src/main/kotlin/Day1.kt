import java.io.File

fun readFile(fileName: String): String = File(fileName).readText(Charsets.UTF_8)

fun main() {
    val input = readFile("Day1.txt")
            .split("\n\n")
            .map { elf -> elf.split("\n").map { cal -> cal.toInt() } }
            .map { it.sum() }
    println(firstStar(input))
    println(secondStar(input))
}

fun firstStar(input: List<Int>): Int = input.maxOf { it }

fun secondStar(input: List<Int>): Int = input.sortedDescending().take(3).sum()

