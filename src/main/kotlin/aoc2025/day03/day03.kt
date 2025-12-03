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
}

private fun part1(input: String): Long {
    val parsedRows = input.parseInput()
    return parsedRows.mapToJoltages().also { println(it) }.sum()
}

private fun List<List<Long>>.mapToJoltages(): List<Long> = this.map { it.mapToJoltage() }

private fun List<Long>.mapToJoltage(): Long {
    val maxFirstValue = this.dropLast(1).max()
    val maxFirstIndex = this.indexOf(maxFirstValue)
    val others = this.subList(maxFirstIndex + 1, this.size)
    val maxSecondValue = others.max()
    return (maxFirstValue * 10) + maxSecondValue
}

private fun String.parseInput(): List<List<Long>> = this.splitOnLineBreaks()
    .map { it.split("").filter { it.isNotBlank() }.map { it.toLong() } }
    .toList()