import java.io.File
import java.lang.Math.abs

class Day9 {
    fun readFileAsList(fileName: String): List<String> = File(fileName).useLines { it.toList() }

    data class Coors(val x: Int, val y: Int) {
        fun moveTowards(other: Coors): Coors {
            var newX = other.x - x
            var newY = other.y - y
            if (newX != 0)
                newX = newX / abs(newX)
            if (newY != 0)
                newY = newY / abs(newY)
            return Coors(x + newX, y + newY)
        }
    }

    fun main() {
        val input = readFileAsList("input.txt")
            .map { it.split(" ") }
        println(simulateMotions(input, MutableList(2) { Coors(0, 0) }))
        println(simulateMotions(input, MutableList(10) { Coors(0, 0) }))
    }

    fun simulateMotions(input: List<List<String>>, rope: MutableList<Coors>): Int {
        val tailPositions = mutableSetOf<Coors>()
        input.forEach { motion ->
            when (motion[0]) {
                "R" -> (1..motion[1].toInt())
                    .forEach { step ->
                        tailPositions.add(moveRope(rope, 0, 1))
                    }
                "L" -> (1..motion[1].toInt())
                    .forEach { step ->
                        tailPositions.add(moveRope(rope, 0, -1))
                    }
                "U" -> (1..motion[1].toInt())
                    .forEach { step ->
                        tailPositions.add(moveRope(rope, 1, 0))
                    }
                "D" -> (1..motion[1].toInt())
                    .forEach { step ->
                        tailPositions.add(moveRope(rope, -1, 0))
                    }
            }
        }
        return tailPositions.size
    }

    fun moveRope(rope: MutableList<Coors>, addX: Int, addY: Int): Coors {
        rope[0] = Coors(rope[0].x + addX, rope[0].y + addY)
        (1..rope.size - 1).forEach { knot ->
            if (abs(rope[knot].x - rope[knot - 1].x) > 1 || abs(rope[knot].y - rope[knot - 1].y) > 1) {
                rope[knot] = rope[knot].moveTowards(rope[knot - 1])
            }
        }
        return rope[rope.size - 1]
    }

}