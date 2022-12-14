import java.io.File

class Day13 {

    data class Node(
        var value: Int? = null,
        val parent: Node? = null,
        var isLeaf: Boolean = false,
        val children: MutableList<Node> = mutableListOf()
    ) {
        override fun toString(): String {
            return if (!isLeaf) {
                val stringChildren = children.joinToString { child -> child.toString() }
                if (stringChildren.isNotEmpty())
                    "[${stringChildren.substring(0, stringChildren.length - 1)}],"
                else
                    "[$stringChildren],"
            } else
                "$value,"
        }
    }

    private fun readFileAsString(fileName: String): String = File(fileName).readText(Charsets.UTF_8)

    fun main() {
        val input = readFileAsString("input.txt")
        println(firstStar(input.split("\n\n").map { it.split("\n") }))
        println(secondStar(input.split("\n\n", "\n")))
    }

    private fun firstStar(input: List<List<String>>): Int {
        val correct = mutableListOf<Int>()
        input.indices.forEach { index ->
            val (firstPacket, _) = parseToTree(input[index][0].substring(1, input[index][0].length), Node())
            val (secondPacket, _) = parseToTree(input[index][1].substring(1, input[index][1].length), Node())
            comparePackets(firstPacket, secondPacket).also { if (it != false) correct.add(index + 1) }
        }
        return correct.sum()
    }

    private fun secondStar(input: List<String>): Int {
        var dividerPacket2 = 1
        var dividerPacket6 = 2
        input.map { packet ->
            packet.replace("""[\[\]]""".toRegex(), "")
                .split(",")
                .first()
        }
            .map { first ->
                if (first == "")
                    -1
                else
                    first.toInt()
            }
            .forEach { value ->
                if (value < 2) dividerPacket2++
                if (value < 6) dividerPacket6++
            }
        return dividerPacket2 * dividerPacket6
    }

    private fun parseToTree(packet: String, parent: Node): Pair<Node, Int> {
        var index = 0
        if (packet.isEmpty())
            return Pair(Node(null, parent, true), index)
        while (index < packet.length) {
            if (packet[index].isDigit()) {
                smaller(packet.indexOf(',', index), packet.indexOf(']', index))
                    .also { end ->
                        parent.children.add(Node(packet.substring(index, end).toInt(), parent, true))
                        index = end - 1
                    }
            } else if (packet[index] == '[') {
                parseToTree(packet.substring(index + 1, packet.length), Node(null, parent))
                    .also { (subtree, movedIndex) ->
                        parent.children.add(subtree)
                        index += movedIndex + 1
                    }
            } else if (packet[index] == ']')
                return Pair(parent, index)
            index++
        }
        return Pair(parent, index)
    }

    private fun smaller(a: Int, b: Int): Int = if (a == -1) b else a.coerceAtMost(b)

    private fun comparePackets(first: Node, second: Node): Boolean? {
        val left = first.children
        val right = second.children
        left.indices.forEach { index ->
            if (index < right.size) {
                if (!left[index].isLeaf && right[index].isLeaf) {
                    comparePackets(
                        left[index],
                        Node().also { list -> list.children.add(right[index]) })?.let { return it }
                } else if (!right[index].isLeaf && left[index].isLeaf) {
                    comparePackets(
                        Node().also { list -> list.children.add(left[index]) },
                        right[index]
                    )?.let { return it }
                } else if (!right[index].isLeaf && !left[index].isLeaf) {
                    comparePackets(left[index], right[index])?.let { return it }
                } else compareValues(left[index].value, right[index].value).also { if (it != 0) return it == -1 }
            } else return false
        }
        if (left.size < right.size)
            return true
        return null
    }
}