import java.io.File
import java.lang.Math.abs
import java.util.LinkedList

class Day18 {

    data class Cube(val x: Int, val y: Int, val z: Int, val neighbours: MutableList<Cube> = mutableListOf()) {
        fun distance(other: Cube): Int {
            return abs(x - other.x) + abs(y - other.y) + abs(z - other.z)
        }

        operator fun plus(other: Cube): Cube {
            return Cube(x + other.x, y + other.y, z + other.z)
        }

        override fun equals(other: Any?): Boolean {
            if (other !is Cube) return false
            return x == other.x && y == other.y && z == other.z
        }

        override fun hashCode(): Int {
            var result = x
            result = 31 * result + y
            result = 31 * result + z
            return result
        }
    }

    fun readFileAsList(fileName: String): List<String> = File(fileName).useLines { it.toList() }

    fun main() {
        val input = readFileAsList("input.txt").map { it.split(",").map { it.toInt() } }
        val cubes = mutableListOf<Cube>()
        input.forEach { cube ->
            val currentCube = Cube(cube[0], cube[1], cube[2])
            cubes.forEach { other ->
                if (currentCube.distance(other) == 1) {
                    currentCube.neighbours.add(other)
                    other.neighbours.add(currentCube)
                }
            }
            cubes.add(currentCube)
        }
        println(firstStar(cubes))
        println(secondStar(cubes))
    }

    private fun firstStar(cubes: MutableList<Cube>) = cubes.map { cube -> 6 - cube.neighbours.size }.sum()

    private fun secondStar(cubes: MutableList<Cube>): Int {
        val maxCoors =
            Triple(cubes.map { it.x }.maxOrNull()!!, cubes.map { it.y }.maxOrNull()!!, cubes.map { it.z }.maxOrNull()!!)
        val airNeighbours = mutableListOf<Cube>(
            Cube(1, 0, 0), Cube(-1, 0, 0),
            Cube(0, 1, 0), Cube(0, -1, 0),
            Cube(0, 0, 1), Cube(0, 0, -1)
        )
        val outside = mutableListOf<Cube>()
        val inside = mutableListOf<Cube>()
        cubes.forEach { cube ->
            airNeighbours.forEach { shift ->
                (cube + shift).also { air ->
                    if (air in inside)
                        cube.neighbours.add(air)
                    else if (air !in cube.neighbours && air !in outside) {
                        if (checkPotencialNeighbour(air, maxCoors, cubes, airNeighbours)) {
                            cube.neighbours.add(air)
                            inside.add(air)
                        } else {
                            outside.add(air)
                        }
                    }
                }
            }
        }
        return cubes.map { cube -> 6 - cube.neighbours.size }.sum()
    }

    private fun checkPotencialNeighbour(
        air: Cube,
        maxCoors: Triple<Int, Int, Int>,
        cubes: MutableList<Cube>,
        airNeighbours: MutableList<Cube>
    ): Boolean {
        val visited = mutableListOf<Cube>()
        val queue = LinkedList<Cube>()
        queue.add(air)
        visited.add(air)
        while (queue.isNotEmpty()) {
            val cube = queue.poll()
            airNeighbours.forEach { shift ->
                (cube + shift).also { air ->
                    isInsideSpace(air, maxCoors).also { insideSpace ->
                        if (air !in cubes && insideSpace && air !in visited) {
                            queue.add(air)
                            visited.add(air)
                        } else if (!insideSpace) {
                            return false
                        }
                    }
                }
            }
        }
        return true
    }

    private fun isInsideSpace(cube: Cube, maxCoors: Triple<Int, Int, Int>): Boolean =
        cube.x <= maxCoors.first && cube.x >= 1 &&
                cube.y <= maxCoors.second && cube.y >= 1 && cube.z <= maxCoors.third && cube.z >= 1
}