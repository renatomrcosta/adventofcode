package aoc2025.day03

import aoc2025.readFile
import aoc2025.splitOnLineBreaks


val testData = """
    987654321111111
    811111111111119
    234234234234278
    818181911112111
""".trimIndent()

fun main() {
    val input = readFile("day03.txt")
    part1(testData).run { require(this == 357L) { "Invalid result $this" } }
    part1(input).run { println("Part1: $this") }

    part2(testData).run { require(this == 3121910778619) { "Invalid result $this" } }
    part2(input).run { println("Part2: $this") }
}

private fun part1(input: String): Long {
    val parsedRows = input.parseInput()
    return parsedRows.mapToJoltages().also { println(it) }.sum()
}

private fun part2(input: String): Long {
    val parsedRows = input.parseInput()
    return parsedRows.mapToJoltagesP2().also { println(it) }.sum()
}

private fun List<List<Long>>.mapToJoltages(): List<Long> = this.map { it.mapToJoltage() }

private fun List<Long>.mapToJoltage(): Long {
    val maxFirstValue = this.dropLast(1).max()
    val maxFirstIndex = this.indexOf(maxFirstValue)
    val others = this.subList(maxFirstIndex + 1, this.size)
    val maxSecondValue = others.max()
    return (maxFirstValue * 10) + maxSecondValue
}

private fun List<List<Long>>.mapToJoltagesP2(): List<Long> = this.map { it.mapToJoltageP2() }

private fun List<Long>.mapToJoltageP2(): Long {
    var grace = 11
    val items = mutableListOf<Long>()
    var target = this
    while (items.size < 12) {
        val maxy = target.dropLast(grace--).max()
        items.add(maxy)
        var lastIndex = target.indexOf(maxy)
        target = target.subList(lastIndex + 1, target.size)
    }
    return items.joinToString("") { it.toString() }.toLong()
}

private fun String.parseInput(): List<List<Long>> = this.splitOnLineBreaks()
    .map { it.split("").filter { it.isNotBlank() }.map { it.toLong() } }
    .toList()