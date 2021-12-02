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
    val parsedTestInput = testinput.parseInputPart1()

    println("Part1")
    check(calculatePart1(parsedTestInput) == 150L)
    calculatePart1(file.parseInputPart1()).run { println("Part1: $this") }

    println("----")
    println("Part2")
    check(calculatePart2(testinput) == 900L)
    calculatePart2(file).run { println("Part2: $this") }

}

private fun calculatePart1(input: Map<String, List<Long>>): Long {
    val forward = input["forward"]?.sum() ?: 0L
    val down = input["down"]?.sum() ?: 0L
    val up = input["up"]?.sum() ?: 0L
    return forward * (down - up)
}

data class State(
    var aim: Long = 0L,
    var horizontal: Long = 0L,
    var depth: Long = 0L,
) {
    val result: Long
        get() = horizontal * depth
}

private fun calculatePart2(input: String): Long {
    val state = State()
    input.splitOnLineBreaks()
        .map { it.split(" ").run { get(0) to get(1).toLong() } }
        .forEach { pair ->
            when (pair.first) {
                "down" -> state.aim += pair.second
                "up" -> state.aim -= pair.second
                "forward" -> {
                    state.horizontal += pair.second
                    state.depth += (pair.second * state.aim)
                }
                else -> error("invalid direction")
            }
        }

    println("final state: $state")
    return state.result
}

private fun String.parseInputPart1() = this.splitOnLineBreaks()
    .map { it.split(" ").run { get(0) to get(1).toLong() } }
    .groupBy({ it.first }) { it.second }

