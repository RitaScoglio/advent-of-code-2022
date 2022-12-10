import java.io.File

class Day10 {
    fun readFileAsList(fileName: String): List<String> = File(fileName).useLines { it.toList() }

    fun main() {
        val input = readFileAsList("input.txt").map { it.split(" ") }
        val signal = mutableListOf<Int>(1)
        (0..input.size - 1).forEach { command ->
            signal.add(signal.last())
            if (input[command].size == 2)
                signal.add(signal.last() + input[command][1].toInt())
        }
        println(firstStar(signal))
        secondStar(signal).map { println(it) }
    }

    private fun firstStar(signal: MutableList<Int>) = listOf(20, 60, 100, 140, 180, 220)
        .map { signal[it - 1] * it }.sum()

    private fun secondStar(signal: MutableList<Int>): List<String> {
        var sprite = getSpritePosition(1)
        var output = ""
        (1..signal.size - 1).forEach { index ->
            if ((index - 1) % 40 in sprite)
                output += '#'
            else
                output += '.'
            if (signal[index] != signal[index - 1])
                sprite = getSpritePosition(signal[index])
        }
        return output.chunked(40)
    }

    private fun getSpritePosition(position: Int): List<Int> = (position - 1..position + 1).map { it }

}