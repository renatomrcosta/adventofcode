package aoc2022.day3

import aoc2022.readFile
import aoc2022.splitOnLineBreaks

private val testInput = """
    vJrwpWtwJgWrhcsFMMfFFhFp
    jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
    PmmdzqPrVvPwwTWBwg
    wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
    ttgJtRGJQctTZtZT
    CrZsJsPPZsGzwwsLwLmpwMDw
""".trimIndent()

fun main() {
    val input = readFile("day3.txt")
    part1(testInput).run { require(this == 157) }
    part1(input).run { println("Part1: $this") }

    //
    part2(testInput).run { require(this == 70) }
    part2(input).run { println("Part2: $this") }
}

private fun part1(input: String): Int {
    return input.splitOnLineBreaks()
        .flatMap { it.toList().chunked(it.length / 2) }
        .windowed(size = 2, step = 2)
        .map { (c1, c2) -> c1 intersect c2 }
        .sumOf { it.first().priority }
}

private fun part2(input: String): Int = input.splitOnLineBreaks()
    .windowed(3, step = 3)
    .map { (c1, c2, c3) -> c1.toList() intersect c2.toList() intersect c3.toList() }
    .sumOf { it.first().priority }

private val letters = ('a'..'z') + ('A'..'Z')
private val Char.priority: Int get() = letters.indexOf(this) + 1 // don't care to error treat index not found

