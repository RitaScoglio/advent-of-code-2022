import java.io.File
import java.util.LinkedList
import java.util.Queue

class Day11 {

    data class Monkey(
        val items: Queue<Long>,
        var operation: List<String>,
        var test: Int,
        var trueMonkey: Int,
        var falseMonkey: Int,
        var inspectedItems: Long
    ) {
        fun operate(oldWorry: Long): Long {
            var newWorry = 0.toLong()
            val change:Long
            if (operation[1] == "old")
                change = oldWorry
            else
                change = operation[1].toLong()
            when (operation[0]) {
                "+" -> newWorry = oldWorry + change
                "*" -> newWorry = oldWorry * change
            }
            return newWorry
        }
    }

    fun main() {
        println(firstStar(parseMonkeys()))
        println(secondStar(parseMonkeys()))
    }

    private fun firstStar(monkeys: List<Monkey>) =
        throwItems(monkeys, 20, 3, ::division)

    private fun secondStar(monkeys: List<Monkey>) =
        throwItems(monkeys, 10000, monkeys.map { it.test }.reduce { a, b -> a * b }, ::modulo)

    fun readFileAsString(fileName: String): String = File(fileName).readText(Charsets.UTF_8)

    private fun parseMonkeys(): MutableList<Monkey> {
        val input = readFileAsString("input.txt")
            .split("\n\n")
            .map { it.split("\n") }
        val monkeys = mutableListOf<Monkey>()
        input.forEach { monkey ->
            monkeys.add(
                Monkey(
                    LinkedList<Long>().also { queue ->
                        monkey[1].substringAfter(": ").split(", ")
                            .forEach { queue.add(it.toInt().toLong()) }
                    },
                    monkey[2].substringAfter("old ").split(" "),
                    monkey[3].substringAfter("by ").toInt(),
                    monkey[4].substringAfter("monkey ").toInt(),
                    monkey[5].substringAfter("monkey ").toInt(),
                    0
                )
            )
        }
        return monkeys
    }

    private fun division(number: Long, divideBy: Int): Long = number / divideBy

    private fun modulo(number: Long, moduloBy: Int): Long = number % moduloBy

    private fun throwItems(monkeys: List<Monkey>, numberOfCycles: Int, manageBy: Int, manageWorry: (Long, Int) -> Long): Long {
        (1..numberOfCycles).forEach { round ->
            monkeys.forEach { monkey ->
                while (monkey.items.isNotEmpty()) {
                    val item = monkey.items.poll()
                    val newWorry = manageWorry(monkey.operate(item), manageBy)
                    if (newWorry % monkey.test == 0.toLong())
                        monkeys[monkey.trueMonkey].items.add(newWorry)
                    else
                        monkeys[monkey.falseMonkey].items.add(newWorry)
                    monkey.inspectedItems += 1
                }
            }
        }
        return monkeys.map { monkey -> monkey.inspectedItems }.sortedDescending().take(2).reduce { a, b -> a * b }
    }

}