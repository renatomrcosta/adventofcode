package aoc2023.day01

import aoc2023.readFile
import aoc2023.splitOnLineBreaks

private val testInput = """
    1abc2
    pqr3stu8vwx
    a1b2c3d4e5f
    treb7uchet
""".trimIndent()

private val testInputPart2 = """
    two1nine
    eightwothree
    abcone2threexyz
    xtwone3four
    4nineeightseven2
    zoneight234
    7pqrstsixteen
""".trimIndent()

fun main() {
    part1(testInput).run { require(this == 142L) }
    part1(readFile("day1.txt")).run { println("Part 1: $this") }

    part2(testInputPart2).run { require(this == 281L) }
    part2(readFile("day1.txt")).run { println("Part 2: $this") }
}

private fun part1(input: String): Long {
    return input.splitOnLineBreaks()
        .map { line ->
            val first = line.first { it.isDigit() }
            val last = line.last { it.isDigit() }

            "$first$last".toLong()
        }
        .onEach { println(it) }
        .sum()
}

private fun part2(input: String): Long = input.splitOnLineBreaks()
    .mapIndexed { idx, line ->
        println("[${idx + 1}] - $line")
        val first = line.findAnyOf(validDigits)?.second ?: error("no string found in line $line")
        val last = line.findLastAnyOf(validDigits)?.second ?: error("no string found in line $line")

        "${first.toLongDigit()}${last.toLongDigit()}".toLong()
    }
    .onEach { println(it) }
    .sum()

private val validDigits = setOf(
    "one", "two", "three", "four", "five", "six", "seven", "eight", "nine",
    "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
)

private fun String.toLongDigit() = when (this) {
    "one" -> 1L
    "two" -> 2L
    "three" -> 3L
    "four" -> 4L
    "five" -> 5L
    "six" -> 6L
    "seven" -> 7L
    "eight" -> 8L
    "nine" -> 9L
    else -> this.toLong()
}
