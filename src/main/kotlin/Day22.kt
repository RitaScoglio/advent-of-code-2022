import java.io.File

class Day22 {

    data class Coors(val row: Int, val col: Int) {
        var minRow = 0
        var maxRow = 0
        var minCol = 0
        var maxCol = 0

        operator fun plus(other: Coors): Coors = Coors(row + other.row, col + other.col)
    }

    fun readFileAsList(fileName: String): List<String> = File(fileName).useLines { it.toList() }

    fun main() {
        val input = readFileAsList("input.txt")
        val monkeyMap = mutableMapOf<Coors, String>()
        (0 until input.size - 2).forEach { row ->
            input[row].indices.forEach { col ->
                if (input[row][col] == '.')
                    monkeyMap[Coors(row, col)] = "open"
                else if (input[row][col] == '#')
                    monkeyMap[Coors(row, col)] = "wall"
            }
        }
        val pathLength = input.last().split("R", "L").map { it.toInt() }
        val rotation = input.last().filter { it == 'R' || it == 'L' }
        println(simulatePath(monkeyMap, pathLength, rotation, ::firstStar))
        println(simulatePath(monkeyMap, pathLength, rotation, ::secondStar))
    }

    private fun simulatePath(
        monkeyMap: MutableMap<Coors, String>,
        pathLength: List<Int>,
        rotation: String,
        function: (Coors, Int) -> Pair<Coors, Int>
    ): Int {
        var current = monkeyMap.keys.filter { it.row == 0 }.minByOrNull { it.col }!!
        var facing = 0
        var move = 0
        while (move < pathLength.size) {
            var (possibleTiles, direction) = tilesAndDirection(monkeyMap, facing, current)
            var step = 0
            while (step < pathLength[move]) {
                var newTile = current + direction
                var type = monkeyMap[newTile]
                if (newTile in possibleTiles && type == "open")
                    current = newTile
                else if (newTile in possibleTiles && type == "wall")
                    break
                else if (newTile !in possibleTiles) {
                    current.also { coors ->
                        if (facing in listOf(0, 2))
                            monkeyMap.keys.filter { it.row == coors.row }.also { tiles ->
                                coors.minCol = tiles.minOfOrNull { it.col }!!
                                coors.maxCol = tiles.maxOfOrNull { it.col }!!
                            }
                        else
                            monkeyMap.keys.filter { it.col == coors.col }.also { tiles ->
                                coors.minRow = tiles.minOfOrNull { it.row }!!
                                coors.maxRow = tiles.maxOfOrNull { it.row }!!
                            }
                    }
                    var possibleFacing: Int
                    function(current, facing).also { (newPosition, newFacing) ->
                        newTile = newPosition
                        type = monkeyMap[newTile]
                        possibleFacing = newFacing
                    }
                    if (type == "open") {
                        current = newTile
                        facing = possibleFacing
                        tilesAndDirection(monkeyMap, facing, current).also {
                            possibleTiles = it.first
                            direction = it.second
                        }
                    } else
                        break
                }
                step++
            }
            if (move < rotation.length)
                facing = if (rotation[move] == 'R') Math.floorMod(facing + 1, 4) else Math.floorMod(facing - 1, 4)
            move++
        }
        return ((current.row + 1) * 1000) + ((current.col + 1) * 4) + facing
    }

    private fun tilesAndDirection(
        monkeyMap: MutableMap<Coors, String>,
        facing: Int,
        position: Coors
    ): Pair<List<Coors>, Coors> =
        Pair(
            if (facing == 0 || facing == 2) monkeyMap.keys.filter { it.row == position.row }
            else monkeyMap.keys.filter { it.col == position.col },
            getDirection(facing)
        )

    private fun getDirection(facing: Int): Coors {
        return when (facing) {
            0 -> Coors(0, 1)
            1 -> Coors(1, 0)
            2 -> Coors(0, -1)
            else -> Coors(-1, 0)
        }
    }

    private fun firstStar(position: Coors, facing: Int): Pair<Coors, Int> {
        return Pair(
            when (facing) {
                0 -> Coors(position.row, position.minCol)
                1 -> Coors(position.minRow, position.col)
                2 -> Coors(position.row, position.maxCol)
                else -> Coors(position.maxRow, position.col)
            }, facing
        )
    }

    private fun secondStar(position: Coors, facing: Int): Pair<Coors, Int> {
        if (position.row == 0 && position.col in (50..99) && facing == 3) {
            return Pair(Coors(position.col + 100, 0), 0)
        } else if (position.row in (150..199) && position.col == 0 && facing == 2) {
            return Pair(Coors(0, position.row - 100), 1)
        } else if (position.row == 0 && position.col in (100..149) && facing == 3) {
            return Pair(Coors(199, position.col - 100), 3)
        } else if (position.row == 199 && position.col in (0..49) && facing == 1) {
            return Pair(Coors(0, position.col + 100), 1)
        } else if (position.row in (0..49) && position.col == 149 && facing == 0) {
            return Pair(Coors(149 - position.row, 99), 2)
        } else if (position.row in (100..149) && position.col == 99 && facing == 0) {
            return Pair(Coors(149 - position.row, 149), 2)
        } else if (position.row == 49 && position.col in (100..149) && facing == 1) {
            return Pair(Coors(position.col - 50, 99), 2)
        } else if (position.row in (50..99) && position.col == 99 && facing == 0) {
            return Pair(Coors(49, position.row + 50), 3)
        } else if (position.row == 149 && position.col in (50..99) && facing == 1) {
            return Pair(Coors(position.col + 100, 49), 2)
        } else if (position.row in (150..199) && position.col == 49 && facing == 0) {
            return Pair(Coors(149, position.row - 100), 3)
        } else if (position.row in (0..49) && position.col == 50 && facing == 2) {
            return Pair(Coors(149 - position.row, 0), 0)
        } else if (position.row in (100..149) && position.col == 0 && facing == 2) {
            return Pair(Coors(149 - position.row, 50), 0)
        } else if (position.row in (50..99) && position.col == 50 && facing == 2) {
            return Pair(Coors(100, position.row - 50), 1)
        } else if (position.row == 100 && position.col in (0..49) && facing == 3) {
            return Pair(Coors(position.col + 50, 50), 0)
        } else
            return Pair(Coors(0, 0), 0)
    }

}