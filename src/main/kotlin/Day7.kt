import java.io.File

class Day7 {

    data class Directory(
        val parent: Directory?
    ) {
        var size: Int = 0
            set(value) {
                field += value
                if (parent != null)
                    parent.size = value
            }
    }

    fun readFileAsList(fileName: String): List<String> = File(fileName).useLines { it.toList() }

    fun main() {
        val input = readFileAsList("input.txt")
        var currentDirectory = Directory(null)
        val directories = mutableListOf<Directory>(currentDirectory)
        input.drop(1).forEach { line ->
            if (line.equals("$ cd ..")) {
                currentDirectory = currentDirectory.parent!!
            } else if (line.contains("$ cd")) {
                val newDirectory = Directory(currentDirectory)
                directories.add(newDirectory)
                currentDirectory = newDirectory
            } else if (line[0].isDigit()) {
                val size = line.split(" ")[0].toInt()
                currentDirectory.size = size
            }
        }
        println(firstStar(directories.map { dir -> dir.size }))
        println(secondStar(directories.map { dir -> dir.size }))
    }

    private fun firstStar(directories: List<Int>): Int = directories
        .filter { it <= 100000 }
        .sum()

    private fun secondStar(directories: List<Int>): Int = directories
        .filter { it >= (30000000 - (70000000 - directories[0])) }
        .sorted()
        .first()
}