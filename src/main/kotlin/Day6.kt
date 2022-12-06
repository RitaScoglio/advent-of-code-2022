import java.io.File

class Day6 {
    fun readFileAsString(fileName: String): String
            = File(fileName).readText(Charsets.UTF_8)

    fun main() {
        val input = readFileAsString("input.txt")
        println(stars(input, 4))
        println(stars(input, 14))
    }

    private fun stars(input: String, size: Int): Int = input.windowed(size)
        .map { window -> if(window.toList().distinct().size == window.length) true else false }
        .indexOf(true)+size
}