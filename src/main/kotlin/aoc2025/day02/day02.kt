package aoc2025.day02

import aoc2025.readFile

val testData = """
    11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124
""".trimIndent()

fun main() {
    val input = readFile("day02.txt")
    part1(testData).run { require(this == 1227775554L) { "Invalid result $this" } }
    part1(input).run { println("Part1: $this") }

//    part2(testData).run { require(this == 6) }
//    part2(input).run { println("Part2: $this") }

}

fun part1(input: String): Long {
    val parsedInput = input.parseInput()
    val results = parsedInput.findAllRepeatingPatterns()

    return results.sum()
}

private fun List<LongRange>.findAllRepeatingPatterns() = this.flatMap { it.findAllRepeatingPatterns() }

private fun LongRange.findAllRepeatingPatterns() = this.mapNotNull {
    val str = it.toString()
    if (str.length % 2 != 0) return@mapNotNull null
    val l = str.take(str.length / 2)
    val r = str.drop(str.length / 2)
    if (l != r) return@mapNotNull null
    it
}

fun String.parseInput() = splitToSequence(',').map {
    val (min, max) = it.split('-').map { it.toLong() }

    min..max
}.toList()