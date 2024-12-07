package aoc2024.day01

import aoc2024.splitOnLineBreaks
import aoc2024.readFile
import kotlin.math.abs

private val testData = """
3   4
4   3
2   5
1   3
3   9
3   3
""".trimIndent()

private const val TEST_EXPECTED_RESULT = 11L

private val NUM_REGEX = Regex("[0-9]+")

fun String.parse(): Pair<List<Long>, List<Long>> {
    val left = mutableListOf<Long>()
    val right = mutableListOf<Long>()

    this.splitOnLineBreaks()
        .forEach { line ->
            val (num1, num2) = NUM_REGEX.findAll(line).flatMap { it.groupValues }.toList()
            left.add(num1.toLong())
            right.add(num2.toLong())
        }
    return left.sorted() to right.sorted()
}

fun part1(input: String): Long {
    val (left, right) = input.parse()
    val paired = left.zip(right)
    val distanceList = paired.map { (l, r) -> abs(l - r) }
    return distanceList.sum()
}

fun main() {
    val input = readFile("day01.txt")
    part1(testData).run { require(this == TEST_EXPECTED_RESULT) }
    part1(input).run { println("Part1: $this") }
}