import java.io.File
import java.lang.Math.abs

class Day15 {

    data class Sensor(val x: Int, val y: Int, val manhattan: Int)

    fun readFileAsList(fileName: String): List<String> = File(fileName).useLines { it.toList() }

    fun main() {
        val beacons = mutableSetOf<Pair<Int, Int>>()
        val sensors = mutableListOf<Sensor>()
        readFileAsList("input.txt")
            .map { line ->
                line.replace("Sensor at x=", "")
                    .replace("closest beacon is at x=", "")
                    .split(": ", ", y=").map { it.toInt() }
            }
            .map { (sensorX, sensorY, beaconX, beaconY) ->
                beacons.add(Pair(beaconX, beaconY))
                sensors.add(Sensor(sensorX, sensorY, abs(sensorX - beaconX) + abs(sensorY - beaconY)))
            }
        println(firstStar(sensors, beacons.filter { it.second == 2_000_000 }.size))
        println(secondStar(sensors))
    }

    private fun firstStar(sensors: List<Sensor>, beacons: Int) =
        getIntervals(sensors, 2_000_000).reduce { range1, range2 ->
            IntRange(
                minOf(range1.first, range2.first),
                maxOf(range1.last, range2.last)) }.count() - beacons

    private fun secondStar(sensors: List<Sensor>): Long {
        (0..4_000_000).forEach { x ->
            getIntervals(sensors, x).also {
                var ultimateRange = it[0]
                (1 until it.size).forEach { rangeIndex ->
                    if (ultimateRange.last < it[rangeIndex].first)
                        return (ultimateRange.last + 1) * 4_000_000L + x
                    else
                        ultimateRange = IntRange(
                            minOf(ultimateRange.first, it[rangeIndex].first),
                            maxOf(ultimateRange.last, it[rangeIndex].last)
                        )
                }
            }
        }
        return 0L
    }

    private fun getIntervals(sensors: List<Sensor>, x: Int): List<IntRange> {
        return sensors.mapNotNull { sensor ->
            val difference = sensor.manhattan - abs(sensor.y - x)
            (sensor.x - difference..sensor.x + difference).takeUnless { it.isEmpty() }
        }.sortedBy { it.first }
    }
}