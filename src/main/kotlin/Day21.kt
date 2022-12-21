import java.io.File
import java.util.LinkedList

class Day21 {

    data class Monkey(val name: String) {
        var operation: String = ""
        var parent: Monkey? = null
        var children: Pair<Monkey, Monkey>? = null
        var value: Long? = null

        fun operate() {
            children?.let { child ->
                val first = child.first.value
                val second = child.second.value
                if (first != null && second != null) {
                    value = when (operation) {
                        "+" -> first + second
                        "-" -> first - second
                        "*" -> first * second
                        "/" -> first / second
                        "=" -> first.compareTo(second).toLong()
                        else -> 0
                    }
                }
            }
        }

        fun operateBackwards() {
            children?.let { child ->
                val first = child.first.value
                val second = child.second.value
                if (first == null && second != null) {
                    child.first.value = when (operation) {
                        "+" -> value!! - second
                        "-" -> value!! + second
                        "*" -> value!! / second
                        "/" -> value!! * second
                        "=" -> second
                        else -> 0
                    }
                } else if (second == null && first != null) {
                    child.second.value = when (operation) {
                        "+" -> value!! - first
                        "-" -> first - value!!
                        "*" -> value!! / first
                        "/" -> first / value!!
                        "=" -> first
                        else -> 0
                    }
                }
            }
        }
    }

    fun readFileAsList(fileName: String): List<String> = File(fileName).useLines { it.toList() }

    fun main() {
        val input = readFileAsList("input.txt")
            .map { it.split(": ", " ") }
        println(firstStar(getMonkeys(input)))
        println(secondStar(getMonkeys(input)))
    }

    private fun firstStar(monkeys: Pair<Monkey, Monkey>): Long? {
        val (rootMonkey, _) = monkeys
        bfs(rootMonkey)
        return rootMonkey.value
    }

    private fun secondStar(monkeys: Pair<Monkey, Monkey>): Long? {
        val (rootMonkey, me) = monkeys
        rootMonkey.operation = "="
        me.value = null
        bfs(rootMonkey)
        rootMonkey.value = 0
        val queue = LinkedList<Monkey>()
        queue.add(rootMonkey)
        while (queue.isNotEmpty()) {
            queue.poll().also { monkey ->
                monkey.children?.let { if (it.first.value == null) queue.add(it.first) else queue.add(it.second) }
                monkey.operateBackwards()
            }
        }
        return me.value
    }

    private fun getMonkeys(input: List<List<String>>): Pair<Monkey, Monkey> {
        var rootMonkey = Monkey("")
        var me = Monkey("")
        input.map { it[0] to Pair(Monkey(it[0]), it.subList(1, it.size)) }.toMap().also { mapOfMonkeys ->
            mapOfMonkeys.forEach { (name, pair) ->
                val (monkey, data) = pair
                if (data.size == 1)
                    monkey.value = data[0].toLong()
                else {
                    val childOne = mapOfMonkeys.get(data[0])!!.first.also { it.parent = monkey }
                    val childTwo = mapOfMonkeys.get(data[2])!!.first.also { it.parent = monkey }
                    monkey.children = Pair(childOne, childTwo)
                    monkey.operation = data[1]
                }
                if (name.equals("root")) {
                    rootMonkey = monkey
                } else if (name.equals("humn")) {
                    me = monkey
                }
            }
        }
        return Pair(rootMonkey, me)
    }

    private fun bfs(root: Monkey) {
        val queue = LinkedList<Monkey>()
        queue.add(root)
        while (queue.isNotEmpty()) {
            val current = queue.poll()
            if (current.value == null) {
                current.children?.let { children ->
                    if (children.first.value == null)
                        queue.add(children.first)
                    if (children.second.value == null)
                        queue.add(children.second)
                    if (children.first.value != null &&
                        children.second.value != null
                    ) {
                        current.operate()
                        current.parent?.let { queue.add(it) }
                    }
                }
            } else
                current.parent?.let { queue.add(it) }
        }
    }
}