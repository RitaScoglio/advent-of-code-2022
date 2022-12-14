import java.io.File

class Day14 {

    data class Coors(val x: Int, val y: Int) {
        operator fun plus(other: Coors): Coors {
            return Coors(x + other.x, y + other.y)
        }
    }

    fun readFileAsList(fileName: String): List<String> = File(fileName).useLines { it.toList() }

    fun main() {
        val input = readFileAsList("input.txt").map { it.split(" -> ") }
        println(simulateSand(parseInput(input), ::flowingOut1) - 1)
        println(simulateSand(parseInput(input), ::flowingOut2))
    }

    private fun parseInput(input: List<List<String>>): MutableList<Coors> {
        val notAnAir = mutableListOf<Coors>()
        input.forEach { path ->
            (0 until path.size - 1).forEach { index ->
                val (fromX, fromY) = path[index].split(",").map { it.toInt() }
                val (toX, toY) = path[index + 1].split(",").map { it.toInt() }
                if (fromX == toX) {
                    if (fromY < toY)
                        goHorizontal(fromY, toY, fromX, notAnAir)
                    else
                        goHorizontal(toY, fromY, fromX, notAnAir)
                } else {
                    if (toX < fromX)
                        goVertical(toX, fromX, fromY, notAnAir)
                    else
                        goVertical(fromX, toX, fromY, notAnAir)
                }
            }
        }
        return notAnAir
    }

    private fun goHorizontal(from: Int, to: Int, x: Int, notAnAir: MutableList<Coors>) =
        (from..to).forEach { notAnAir.add(Coors(x, it)) }

    private fun goVertical(from: Int, to: Int, y: Int, notAnAir: MutableList<Coors>) =
        (from..to).forEach { notAnAir.add(Coors(it, y)) }

    private fun flowingOut1(sand: Coors, rock: Int): Boolean = sand.y == rock

    private fun flowingOut2(sand: Coors, rock: Int): Boolean = sand == Coors(500, 0)

    private fun simulateSand(notAnAir: MutableList<Coors>, isFlowingOut: (Coors, Int) -> Boolean): Int {
        val rocks = notAnAir.size
        val lastRock = notAnAir.map { it.y }.reduce { a, b -> maxOf(a, b) }
        var flowsOut = false
        while (!flowsOut) {
            var sand = Coors(500, 0)
            var inRest = false
            while (!inRest) {
                if (sand.y + 1 < lastRock + 2)
                    when {
                        sand + Coors(0, 1) !in notAnAir -> sand += Coors(0, 1)
                        sand + Coors(-1, 1) !in notAnAir -> sand += Coors(-1, 1)
                        sand + Coors(1, 1) !in notAnAir -> sand += Coors(1, 1)
                        else -> inRest = true
                    }
                else inRest = true
                flowsOut = isFlowingOut(sand, lastRock)
                if (flowsOut) break
            }
            notAnAir.add(sand)
        }
        return notAnAir.size - rocks
    }
}