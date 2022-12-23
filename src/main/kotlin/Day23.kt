import java.io.File

class Day23 {
    data class Coors(val row: Int, val col: Int) {
        operator fun plus(other: Coors): Coors = Coors(row + other.row, col + other.col)
    }

    fun readFileAsList(fileName: String): List<String> = File(fileName).useLines { it.toList() }

    fun main() {
        val input = readFileAsList("input.txt")
        val elves = mutableListOf<Coors>()
        input.indices.forEach { row ->
            input[row].indices.forEach { col ->
                if (input[row][col] == '#')
                    elves.add(Coors(row, col))
            }
        }
        val(firstStar, secondStar) = simulateElvesMoves(elves)
        println(firstStar)
        println(secondStar)
    }

    private fun simulateElvesMoves(elves: MutableList<Coors>): Pair<Int, Int> {
        var directions = linkedSetOf(
            listOf(Coors(-1, -1), Coors(-1, 0), Coors(-1, 1)),
            listOf(Coors(1, -1), Coors(1, 0), Coors(1, 1)),
            listOf(Coors(-1, -1), Coors(0, -1), Coors(1, -1)),
            listOf(Coors(-1, 1), Coors(0, 1), Coors(1, 1))
        )
        var round = 0
        var checkProgress = 0
        do {
            val proposedMoves = mutableMapOf<Coors?, List<Coors>>()
            elves.forEach { elf ->
                var choosedMove: Coors? = null
                if (doesTheElfWantToMove(elf, directions, elves)) {
                    directions.forEach { direction ->
                        if (choosedMove == null && !direction.map { it + elf }.map { it in elves }.contains(true))
                            choosedMove = elf + direction[1]
                    }
                }
                proposedMoves.merge(choosedMove, listOf(elf), ::merge)
            }
            elves.clear()
            proposedMoves.forEach { (moveTo, list) ->
                if (list.size == 1 && moveTo != null)
                    elves.add(moveTo)
                else
                    elves.addAll(list)
            }
            directions = moveFirstToBack(directions)
            if(round == 10)
                checkProgress = emptyTiles(elves)
            round++
        } while (proposedMoves.size > 1)
        return Pair(checkProgress, round)
    }

    private fun doesTheElfWantToMove(
        elf: Coors,
        directions: LinkedHashSet<List<Coors>>,
        elves: MutableList<Coors>
    ): Boolean = directions.map { direction ->
        direction.map { it + elf }.map { it in elves }
    }.flatten().contains(true)

    private fun emptyTiles(elves: MutableList<Coors>): Int {
        val minX = elves.minOf { it.col }
        val maxX = elves.maxOf { it.col }
        val minY = elves.minOf { it.row }
        val maxY = elves.maxOf { it.row }
        return (maxX - minX + 1) * (maxY - minY + 1) - elves.size
    }

    private fun merge(old: List<Coors>, new: List<Coors>): List<Coors> = old + new

    private fun moveFirstToBack(directions: LinkedHashSet<List<Coors>>): LinkedHashSet<List<Coors>> {
        directions.first().also{
            directions.remove(it)
            directions.add(it)
        }
        return directions
    }
    
}