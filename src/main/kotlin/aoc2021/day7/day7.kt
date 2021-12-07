package aoc2021.day7

import aoc2021.readFile
import kotlin.math.abs

private const val testInput = "16,1,2,0,4,2,7,1,2,14"

fun main() {
    val file = readFile("day7.txt")

    println("Part1")
    testInput.parse().run { calculatePart1(this) == 37 }
    val part1 = calculatePart1(file.parse())
    println("Part1 = $part1")

//    testInput.parse().run { calculatePart2(this) == 168 }
//    val part2 = calculatePart2(file.parse())
//    println("Part2 = $part2")

}

private fun calculatePart1(input: List<Int>): Int {
    val maxSize = input.last()
    val map = mutableMapOf<Int, Int>().apply {
        (0..maxSize).forEach { this[it] = 0 }
    }
    input.sorted().groupingBy { it }.eachCountTo(map)

    // For each position, find the sum of the deltas of all items and their distances
    val (index, totalFuel) = map.keys.map { position ->
        val sum = map.entries.filter { it.value != 0 }.sumOf { (index, crabCount) ->
            val distance = abs(index - position)
            distance * crabCount
        }
        position to sum
    }.minByOrNull { it.second } ?: error("no min value found")

    println("Index: $index, Total fuel spent $totalFuel")
    return totalFuel
}

private fun String.parse() = this.splitToSequence(",").map { it.toInt() }.toList().sorted()