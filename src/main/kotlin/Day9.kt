import java.io.File
import java.lang.Math.abs
import kotlin.math.sign

class Day9 {
    fun readFileAsList(fileName: String): List<String> = File(fileName).useLines { it.toList() }

    data class Coors(val x: Int, val y: Int) {
        fun moveTowards(other: Coors): Coors {
            return Coors(x + (other.x - x).sign, y + (other.y - y).sign)
        }
    }

    fun main() {
        val input = readFileAsList("input.txt")
            .map { it.split(" ") }
        println(simulateMotions(input, MutableList(2) { Coors(0, 0) }))
        println(simulateMotions(input, MutableList(10) { Coors(0, 0) }))
    }

    fun simulateMotions(input: List<List<String>>, rope: MutableList<Coors>): Int {
        val directions = mapOf("R" to Coors(0, 1), "L" to Coors(0, -1), "U" to Coors(1, 0), "D" to Coors(-1, 0))
        val tailPositions = mutableSetOf<Coors>()
        input.forEach { motion -> (1..motion[1].toInt())
                .forEach { directions.get(motion[0])?.let { moveRope(rope, it) }?.let { tailPositions.add(it) } }
        }
        return tailPositions.size
    }

    fun moveRope(rope: MutableList<Coors>, add: Coors): Coors {
        rope[0] = Coors(rope[0].x + add.x, rope[0].y + add.y)
        (1..rope.size - 1).forEach { knot ->
            if (abs(rope[knot].x - rope[knot - 1].x) > 1 || abs(rope[knot].y - rope[knot - 1].y) > 1) {
                rope[knot] = rope[knot].moveTowards(rope[knot - 1])
            }
        }
        return rope[rope.size - 1]
    }

}