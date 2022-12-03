import java.io.File

class Day3 {

    var ALPHABET = ('a'..'z').toList() + ('A'..'Z').toList()

    fun readFileAsList(fileName: String): List<String> = File(fileName).useLines { it.toList() }

    fun main() {
        val input = readFileAsList("input.txt").map { it.toList() }
        println(firstStar(input))
        println(secondStar(input))
    }

    fun firstStar(input: List<List<Char>>) = input.map { rucksack -> rucksack.chunked(rucksack.size / 2) }
        .sumOf { (c1, c2) -> ALPHABET.indexOf(c1.intersect(c2).iterator().next()) + 1 }

    fun secondStar(input: List<List<Char>>) = input.chunked(3)
        .sumOf { (c1, c2, c3) -> ALPHABET.indexOf(c1.intersect(c2).intersect(c3).iterator().next()) + 1 }

}