import java.io.File
import java.lang.Math.min
import java.util.*

class Day12 {

    data class Coors(
        val x: Int = 0,
        val y: Int = 0,
        var elevation: Char = 'a',
        var pathScore: Int = Integer.MAX_VALUE
    )

    fun readFileAsList(fileName: String): List<String> = File(fileName).useLines { it.toList() }

    fun main() {
        val input = readFileAsList("input.txt")
        println(solve(input, ::pathScoreFirstStar))
        println(solve(input, ::pathScoreSecondStar))
    }

    private fun solve(input: List<String>, pathScoreFunction: (Coors, Coors) -> Int): Int {
        val matrix = mutableMapOf<String, Coors>()
        var start = Coors()
        var end = Coors()
        (0..input.size - 1).forEach { x ->
            (0..input[x].length - 1).forEach { y ->
                matrix.put("$x,$y", Coors(x, y, input[x][y])
                    .also { coors ->
                        when (coors.elevation) {
                            'S' -> {
                                start = coors
                                coors.elevation = 'a' - 1
                                coors.pathScore = 0
                            }
                            'E' -> {
                                end = coors
                                coors.elevation = 'z' + 1
                            }
                        }
                    })
            }
        }
        val directions = listOf(Coors(1, 0), Coors(-1, 0), Coors(0, 1), Coors(0, -1))
        val queue: Queue<Coors> = LinkedList<Coors>()
        queue.add(matrix.get("${start.x},${start.y}"))
        while (queue.isNotEmpty()) {
            val current = queue.poll()
            directions.forEach { direction -> checkAndAdd(direction, current, matrix, queue, pathScoreFunction) }
        }
        return end.pathScore
    }

    private fun checkAndAdd(
        direction: Coors,
        current: Coors,
        matrix: MutableMap<String, Coors>,
        queue: Queue<Coors>,
        pathScoreFunction: (Coors, Coors) -> Int
    ) {
        matrix.get("${current.x + direction.x},${current.y + direction.y}")?.let { neighbour ->
            if ((neighbour.elevation - current.elevation) >= -25 && (neighbour.elevation - current.elevation) <= 1) {
                val newPathScore =
                    min(neighbour.pathScore, pathScoreFunction(neighbour, current))
                if (newPathScore != neighbour.pathScore) {
                    neighbour.pathScore = newPathScore
                    queue.add(neighbour)
                }
            }
        }
    }

    private fun pathScoreFirstStar(neighbour: Coors, current: Coors) = current.pathScore + 1

    private fun pathScoreSecondStar(neighbour: Coors, current: Coors) =
        if (neighbour.elevation == 'a') 0 else current.pathScore + 1
}