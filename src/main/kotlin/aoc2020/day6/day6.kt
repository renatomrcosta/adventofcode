package aoc2020.day6

import aoc2020.readFile
import aoc2020.splitNewLines

fun main() {
    println("Part 1 - Test Data")
    parseInput(testData).run {
        val answerSum = sumOf { it.size }
        println("Group Reply Count = $answerSum")
        assert(answerSum == 11)
    }

    println("Part 1 - Input file")
    parseInput(readFile("day6.txt")).run {
        println("Group Reply Count = " + sumOf { it.size })
    }
}

private fun parseInput(input: String): List<Set<Char>> =
    input
        .splitNewLines()
        .filter { it.isNotEmpty() }
        .map { it.replace("\n", "") }
        .map { it.toSet() }

private val testData = """
    abc

    a
    b
    c

    ab
    ac

    a
    a
    a
    a

    b
""".trimIndent()
