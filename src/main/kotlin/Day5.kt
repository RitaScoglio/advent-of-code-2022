import java.io.File

class Day5 {

    fun readFileAsString(fileName: String): String
            = File(fileName).readText(Charsets.UTF_8)

    fun main() {
        val input = readFileAsString("input.txt").split("\n\n")
        val numberOfStacks = input[0].last().digitToInt()
        val crates = input[0].chunked(4)
            .map{ layer -> layer.replace("""[\n\[\] ]""".toRegex(), "")}
            .dropLast(numberOfStacks)
        val instructions = input[1].split("\n")
                .map{it.split(" ")
                .filter { it != "move" && it != "from" && it!= "to" }
                .map { it.toInt() }}
        println(firstStar(instructions, createStack(crates, numberOfStacks)))
        println(secondStar(instructions, createStack(crates, numberOfStacks)))
    }

    fun firstStar(instructions: List<List<Int>>, stack: MutableList<MutableList<String>>): String {
        instructions.forEach { (move, from, to) ->
            (1..move).forEach {
                val toMove = stack[from - 1].takeLast(1)
                stack[from - 1].removeLast()
                stack[to - 1].addAll(toMove)
            }
        }
        return stack.map { it.last() }.joinToString("")
    }

    fun secondStar(instructions: List<List<Int>>, stack: MutableList<MutableList<String>>): String {
        instructions.forEach { (move, from, to) ->
            val toMove = stack[from-1].takeLast(move)
            stack[from-1] = stack[from-1].dropLast(move).toMutableList()
            stack[to-1].addAll(toMove)
        }
        return stack.map { it.last() }.joinToString("")
    }

    fun createStack(crates: List<String>, size: Int): MutableList<MutableList<String>> {
        val stack: MutableList<MutableList<String>> = MutableList(size){ mutableListOf() }
        (crates.size-1 downTo 0).map { index -> if(crates[index] != "") stack[index%size].add(crates[index]) }
        return stack
    }

}


