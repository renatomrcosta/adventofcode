package aoc2021.day7

import aoc2021.readFile
import kotlin.math.abs

private const val testInput = "16,1,2,0,4,2,7,1,2,14"

fun main() {
    val file = readFile("day7.txt")

    println("Part1")
    testInput.parse().run { check(calculatePart1(this) == 37) }
    val part1 = calculatePart1(file.parse())
    println("Part1 = $part1")

    println("Part2")
    testInput.parse().run { check(calculatePart2(this) == 168) }
    val part2 = calculatePart2(file.parse())
    println("Part2 = $part2")

}

private fun calculatePart2(input: List<Int>): Int {
    val maxSize = input.last()
    val map = input.sorted().groupingBy { it }.eachCount()

    val (index, totalFuel) = (0..maxSize).map { position ->
        val sum = map.entries.sumOf { (index, crabCount) ->
            val distance = abs(index - position)

            /***
            // I completely brainfarted in here, and made an iteration because I couldn't work out the equation quickly
            var totalCost = 0
            var initialCost = 1
            repeat(distance) {
            totalCost += initialCost
            initialCost++
            }
            println("Total Cost for $index = $totalCost")
             ***/
            val totalCost = ((distance + 1) * distance) / 2
            crabCount * totalCost
        }
        position to sum
    }.minByOrNull { it.second } ?: error("no min value found")

    println("Index: $index, Total fuel spent $totalFuel")
    return totalFuel
}

private fun calculatePart1(input: List<Int>): Int {
    val maxSize = input.last()
    val map = input.sorted().groupingBy { it }.eachCount()

    // For each position, find the sum of the deltas of all items and their distances
    val (index, totalFuel) = (0..maxSize).map { position ->
        val sum = map.entries.sumOf { (index, crabCount) ->
            val distance = abs(index - position)
            distance * crabCount
        }
        position to sum
    }.minByOrNull { it.second } ?: error("no min value found")

    println("Index: $index, Total fuel spent $totalFuel")
    return totalFuel
}

private fun String.parse() = this.splitToSequence(",").map { it.toInt() }.toList().sorted()