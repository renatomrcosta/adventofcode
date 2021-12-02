package aoc2021.day2

import aoc2021.readFile
import aoc2021.splitOnLineBreaks

private val testinput = """
    forward 5
    down 5
    forward 8
    up 3
    down 8
    forward 2
""".trimIndent()

fun main() {
    val file = readFile("day2.txt")
    val parsedTestInput = testinput.parseInput()

    println("Part1")
    check(calculatePart1(parsedTestInput) == 150L)
    calculatePart1(file.parseInput()).run { println("Part1: $this") }

}

private fun calculatePart1(input: Map<String, List<Long>>): Long {
    val forward = input["forward"]?.sum() ?: 0L
    val down = input["down"]?.sum() ?: 0L
    val up = input["up"]?.sum() ?: 0L
    return forward * (down - up)
}

private fun String.parseInput() = this.splitOnLineBreaks()
    .map { it.split(" ").run { get(0) to get(1).toLong() } }
    .groupBy({ it.first }) { it.second }
