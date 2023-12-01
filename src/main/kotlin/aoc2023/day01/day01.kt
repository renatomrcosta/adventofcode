package aoc2023.day01

import aoc2023.readFile
import aoc2023.splitOnLineBreaks

private val testInput = """
    1abc2
    pqr3stu8vwx
    a1b2c3d4e5f
    treb7uchet
""".trimIndent()

fun main() {
    part1(testInput).run { require(this == 142L) }
    part1(readFile("day1.txt")).run { println("Part 1: $this") }
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
