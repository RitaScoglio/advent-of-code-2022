import java.io.File

class Day8 {

    fun readFileAsList(fileName: String): List<String> = File(fileName).useLines { it.toList() }

    fun main() {
        val input = readFileAsList("input.txt")
            .map { it.chunked(1).map { it.toInt() } }
        println(firstStar(input))
        println(secondStar(input))
    }

    fun getVisibility(line: List<Int>, height: Int): Int = line.indexOfFirst { it >= height } +1

    fun getScenic(line: List<Int>, height: Int): Int {
        val view = getVisibility(line, height)
        return if (view == 0) line.size else view
    }

    fun firstStar(input: List<List<Int>>): Int {
        var visibleInGrid = 0
        (1..input.size - 2).forEach { x ->
            (1..input.size - 2).forEach { y ->
                val directions = mutableListOf<Int>()
                val height = input[x][y]
                val row = input[x]
                val column = (input.indices).map { input[it][y] }
                directions.add(getVisibility(row.subList(y + 1, input.size), height))
                directions.add(getVisibility(row.subList(0, y).reversed(), height))
                directions.add(getVisibility(column.subList(0, x).reversed(), height))
                directions.add(getVisibility(column.subList(x + 1, input.size), height))
                if (directions.contains(0)) {
                    visibleInGrid += 1
                }
            }
        }
        val visibleByDefault = input.size * 2 + (input.size - 2) * 2
        return visibleInGrid + visibleByDefault
    }

    fun secondStar(input: List<List<Int>>): Int {
        val scenicScores = mutableListOf<Int>()
        (1..input.size - 2).forEach { x ->
            (1..input.size - 2).forEach { y ->
                val directions = mutableListOf<Int>()
                val height = input[x][y]
                val row = input[x]
                val column = (input.indices).map { input[it][y] }
                directions.add(getScenic(row.subList(y + 1, input.size), height))
                directions.add(getScenic(row.subList(0, y).reversed(), height))
                directions.add(getScenic(column.subList(0, x).reversed(), height))
                directions.add(getScenic(column.subList(x + 1, input.size), height))
                scenicScores.add(directions.reduce { a, b -> a * b })
            }
        }
        return scenicScores.maxOf { it }
    }

}