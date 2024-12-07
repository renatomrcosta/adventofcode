package aoc2024.day01

import aoc2024.splitOnLineBreaks
import aoc2024.extractNumbers
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

private const val TEST_EXPECTED_RESULT_PART1 = 11L
private const val TEST_EXPECTED_RESULT_PART2 = 31L


fun String.parse(): Pair<List<Long>, List<Long>> {
    val left = mutableListOf<Long>()
    val right = mutableListOf<Long>()

    this.splitOnLineBreaks()
        .forEach { line ->
            val (num1, num2) = line.extractNumbers()
            left.add(num1.toLong())
            right.add(num2.toLong())
        }
    return left to right
}

fun part1(input: String): Long {
    val (left, right) = input.parse()
    val paired = left.sorted().zip(right.sorted())
    val distanceList = paired.map { (l, r) -> abs(l - r) }
    return distanceList.sum()
}

fun part2(input: String): Long {
    val (left, right) = input.parse()
    val valueSimilarityMap = left.map {
        it to right.filter { r -> r == it }.count()
    }
//    println(valueSimilarityMap)
    return valueSimilarityMap.map { (v, sm) -> v * sm }.sum()
}

fun main() {
    val input = readFile("day01.txt")
    part1(testData).run { require(this == TEST_EXPECTED_RESULT_PART1) }
    part1(input).run { println("Part1: $this") }

    part2(testData).run{ require(this == TEST_EXPECTED_RESULT_PART2) }
    part2(input).run { println("Part2: $this") }

}