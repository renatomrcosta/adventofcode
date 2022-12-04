package aoc2022.day4

import aoc2022.readFile
import aoc2022.splitOnLineBreaks

private val testInput = """
    2-4,6-8
    2-3,4-5
    5-7,7-9
    2-8,3-7
    6-6,4-6
    2-6,4-8
""".trimIndent()

fun main() {
    val input = readFile("day4.txt")
    part1(testInput).run { require(this == 2) }
    part1(input).run { println("Part1: $this") }
}

private fun part1(input: String): Int {
    return input.parseInput().count { (left, right) ->
        left.containsAll(right) || right.containsAll(left)
    }
}

private fun String.parseInput() = this.splitOnLineBreaks()
    .map {
        val (left, right) = it.split(",")
        left.toSet() to right.toSet()
    }

fun String.toSet(): Set<Int> {
    val (left, right) = this.split("-")
    val range = left.toInt()..right.toInt()
    return range.toSet()
}