import java.io.File

class Day24 {

    data class Blizzard(val position: Coors, val direction: Char) {
        operator fun plusAssign(other: Blizzard) {
            position.row += other.position.row
            position.col += other.position.col
        }
    }

    data class Coors(var row: Int, var col: Int) {
        operator fun plus(other: Coors): Coors = Coors(row + other.row, col + other.col)
    }

    fun readFileAsList(fileName: String): List<String> = File(fileName).useLines { it.toList() }

    fun main() {
        val input = readFileAsList("input.txt")
        val start = Coors(0, input.first().indexOf('.'))
        val end = Coors(input.size - 1, input.last().indexOf('.'))
        println(firstStar(input, start, end))
        println(secondStart(input, start, end))
    }

    private fun secondStart(input: List<String>, start: Coors, end: Coors): Int {
        blizzardsAndField(input).also { (blizzards, field) ->
            return mutableListOf(Pair(start, end), Pair(end, start), Pair(start, end)).sumOf { (start, end) ->
                goThroughField(start, end, blizzards, field)
            }
        }
    }

    private fun firstStar(input: List<String>, start: Coors, end: Coors): Int {
        blizzardsAndField(input).also { (blizzards, field) -> return goThroughField(start, end, blizzards, field) }
    }

    private fun blizzardsAndField(input: List<String>): Pair<MutableList<Blizzard>, MutableList<Coors>> {
        val field = mutableListOf<Coors>()
        val blizzards = mutableListOf<Blizzard>()
        (1 until input.size - 1).forEach { row ->
            (1 until input[row].length - 1).forEach { col ->
                if (input[row][col] != '.')
                    blizzards.add(Blizzard(Coors(row, col), input[row][col]))
                field.add(Coors(row, col))
            }
        }
        return Pair(blizzards, field)
    }

    private fun goThroughField(
        start: Coors,
        end: Coors,
        blizzards: MutableList<Blizzard>,
        field: MutableList<Coors>
    ): Int {
        val directions = listOf(
            Blizzard(Coors(0, 1), '>'),
            Blizzard(Coors(0, -1), '<'),
            Blizzard(Coors(-1, 0), '^'),
            Blizzard(Coors(1, 0), 'v')
        )
        var minutes = 0
        val currentPositions = mutableSetOf(start)
        while (end !in currentPositions) {
            moveBlizzards(blizzards, field, directions)
            val nextPossitions = mutableSetOf<Coors>()
            currentPositions.forEach { position ->
                moveMe(position, blizzards, field, directions.map { it.position }, end).also { list ->
                    if (list.isNotEmpty())
                        list.forEach { newPosition ->
                            nextPossitions.add(newPosition)
                        }
                    if (position !in blizzards.map { it.position })
                        nextPossitions.add(position)
                }
            }
            currentPositions.clear()
            currentPositions.addAll(nextPossitions)
            minutes++
        }
        return minutes
    }

    private fun moveBlizzards(blizzards: MutableList<Blizzard>, field: MutableList<Coors>, directions: List<Blizzard>) {
        blizzards.forEach { blizzard ->
            blizzard += directions.filter { it.direction == blizzard.direction }.iterator().next()
            if (blizzard.position !in field) {
                when (blizzard.direction) {
                    '>' -> blizzard.position.col = field.filter { it.row == blizzard.position.row }.minOf { it.col }
                    '<' -> blizzard.position.col = field.filter { it.row == blizzard.position.row }.maxOf { it.col }
                    '^' -> blizzard.position.row = field.filter { it.col == blizzard.position.col }.maxOf { it.row }
                    'v' -> blizzard.position.row = field.filter { it.col == blizzard.position.col }.minOf { it.row }
                }
            }
        }
    }

    private fun moveMe(
        me: Coors,
        blizzards: MutableList<Blizzard>,
        field: MutableList<Coors>,
        directions: List<Coors>,
        end: Coors
    ): Set<Coors> {
        val possiblePositions = mutableSetOf<Coors>()
        directions.forEach { direction ->
            val newPosition = direction + me
            if ((newPosition in field
                        && newPosition !in blizzards.map { it.position })
                || newPosition == end
            ) {
                possiblePositions.add(newPosition)
            }
        }
        return possiblePositions
    }
}