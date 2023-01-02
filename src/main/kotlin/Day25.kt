import java.io.File
import kotlin.math.pow

class Day25 {

    private val SNAFU = mapOf('2' to 2, '1' to 1, '0' to 0, '-' to -1, '=' to -2)

    fun readFileAsList(fileName: String): List<String> = File(fileName).useLines { it.toList() }

    fun main() {
        val input = readFileAsList("input.txt")
        println(convertToSNAFU(input.sumOf { convertToDecimal(it.reversed()) }))
    }

    private fun convertToDecimal(number: String): Double = number.indices.sumOf { place ->
        (5.0).pow(place) * SNAFU[number[place]]!!
    }

    private fun convertToSNAFU(sum: Double): String {
        var number = ""
        do {
            number = getNextChar(number, sum)
        } while (convertToDecimal(number) != sum)
        return number.reversed()
    }

    private fun getNextChar(currentSNAFU: String, sum: Double): String {
        val divider = (5.0).pow(currentSNAFU.length + 1)
        return SNAFU.keys.joinToString("") { char ->
            val newSNAFU = currentSNAFU + char
            if ((sum - convertToDecimal(newSNAFU)) % divider == 0.0)
                newSNAFU
            else ""
        }
    }
}