import java.io.File

class Day2 {

    fun readFileAsList(fileName: String): List<String> = File(fileName).useLines { it.toList() }

    var win = mapOf<Int, Int>(3 to 1, 1 to 2, 2 to 3)
    var lose = mapOf<Int, Int>(2 to 1, 3 to 2, 1 to 3)

    var mapping1 = mapOf<String, Int>("A" to 1, "B" to 2, "C" to 3, "X" to 1, "Y" to 2, "Z" to 3)
    var mapping2 = mapOf<String, Int>("A" to 1, "B" to 2, "C" to 3, "X" to 0, "Y" to 3, "Z" to 6)

    fun main() {
        val input = readFileAsList("Day2.txt").map { it.split(" ") }.flatten()
        println(firstStar(input.map { mapping1.get(it)!! }.chunked(2)))
        println(secondStar(input.map { mapping2.get(it)!! }.chunked(2)))
    }

    fun firstStar(game: List<List<Int>>): Int {
        var score = 0
        game.forEach {
            if (win.get(it[0]) == it[1])
                score += 6 + it[1]
            else if (lose.get(it[0]) == it[1])
                score += 0 + it[1]
            else
                score += 3 + it[1]
        }
        return score
    }

    fun secondStar(game: List<List<Int>>): Int {
        var score = 0
        game.forEach {
            when (it[1]) {
                0 -> score += lose.get(it[0])!!
                3 -> score += 3 + it[0]
                6 -> score += 6 + win.get(it[0])!!
            }
        }
        return score
    }
}