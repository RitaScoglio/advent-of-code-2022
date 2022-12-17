import java.io.File

class Day17 {

    fun readFileAsString(fileName: String): String = File(fileName).readText(Charsets.UTF_8)

    fun main() {
        val input = readFileAsString("input.txt").split("").filter { it != "" }
        val floor = mutableListOf<Pair<Int, Int>>(
            Pair(0, -1),
            Pair(1, -1),
            Pair(2, -1),
            Pair(3, -1),
            Pair(4, -1),
            Pair(5, -1),
            Pair(6, -1)
        )
        val shapes = mutableListOf<MutableList<Pair<Int, Int>>>(
            mutableListOf(Pair(0, 0), Pair(1, 0), Pair(2, 0), Pair(3, 0)),
            mutableListOf(Pair(1, 0), Pair(0, 1), Pair(1, 1), Pair(2, 1), Pair(1, 2)),
            mutableListOf(Pair(0, 0), Pair(1, 0), Pair(2, 0), Pair(2, 1), Pair(2, 2)),
            mutableListOf(Pair(0, 0), Pair(0, 1), Pair(0, 2), Pair(0, 3)),
            mutableListOf(Pair(0, 0), Pair(1, 0), Pair(0, 1), Pair(1, 1))
        )
        println(firstStar(input, floor, shapes))
        println(secondStar())
    }

    private fun firstStar(
        input: List<String>,
        floor: MutableList<Pair<Int, Int>>,
        shapes: MutableList<MutableList<Pair<Int, Int>>>
    ): Int {
        var rockCount = 0
        var index = 0
        var shapeIndex = 0
        var currentShape = shapes[shapeIndex].toMutableList().map { Pair(it.first + 2, it.second + 3) }
        while (rockCount < 2022) {
            when (input[index]) {
                "<" -> currentShape = push(floor, currentShape, -1, -1)
                ">" -> currentShape = push(floor, currentShape, 7, +1)
            }
            if (floor.map { it in currentShape.map { Pair(it.first, it.second - 1) } }.contains(true)) {
                floor.addAll(currentShape)
                shapeIndex = (shapeIndex + 1) % shapes.size
                (floor.map { it.second }.maxOrNull()!! + 4).also { newTop ->
                    currentShape = shapes[shapeIndex].toMutableList().map { Pair(it.first + 2, it.second + newTop) }
                }
                rockCount++
            } else {
                currentShape = currentShape.map {
                    Pair(it.first, it.second - 1)
                }
            }
            index = (index + 1) % input.size
        }
        return floor.map { it.second }.maxOrNull()!! + 1
    }

    private fun push(
        floor: MutableList<Pair<Int, Int>>,
        currentShape: List<Pair<Int, Int>>,
        margin: Int,
        pushBy: Int
    ): List<Pair<Int, Int>> {
        floor.map { it in currentShape.map { Pair(it.first + pushBy, it.second) } }.contains(true).also { floorCheck ->
            if (!currentShape.map { it.first + pushBy }.contains(margin) && !floorCheck) {
                return currentShape.map { Pair(it.first + pushBy, it.second) }
            } else
                return currentShape
        }
    }

    private fun secondStar(): Long {
        //values from printouts
        var cycle: Long = 174L
        var height: Long = 264L
        while (cycle + 1740 < 1_000_000_000_000) {
            cycle += 1740
            height += 2724
        }
        height += 1586
        return height
    }
}