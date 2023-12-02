package aoc2023.day02

import aoc2023.readFile

private val testInput = """
    
""".trimIndent()

fun main() {
    part1(testInput).run { require(this == "") }
    part1(readFile("day2.txt")).run { println("Part 1: $this") }
}

private fun part1(input: String) = "TODO()"
}
