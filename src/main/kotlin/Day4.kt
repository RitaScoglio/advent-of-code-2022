import java.io.File

class Day4 {

    fun readFileAsList(fileName: String): List<String> = File(fileName).useLines { it.toList() }

    fun main() {
        val input = readFileAsList("input.txt")
            .map{ pair -> pair.split(",")
                .map { elf -> val range = elf.split("-")
                listOf(range[0].toInt()..range[1].toInt()).flatten()
                } }
        println(firstStar(input))
        println(secondStar(input))
    }

    fun firstStar(input: List<List<List<Int>>>): Int = input.sumOf { (elf1, elf2) ->
            val common = elf1.intersect(elf2).toList()
            if (common == elf1 || common == elf2)
                1 as Int
            else 0 as Int
        }

    fun secondStar(input: List<List<List<Int>>>): Int = input.sumOf { (elf1, elf2) ->
        val common = elf1.intersect(elf2)
        if (common.isNotEmpty()) 1 as Int
        else 0 as Int
    }
}