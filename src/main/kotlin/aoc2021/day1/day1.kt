package aoc2021.day1

import aoc2021.readFile
import aoc2021.splitOnLineBreaks

fun main() {
    // Day 1 boilerplate
    val part1TestInput = listOf(199, 200, 208, 210, 200, 207, 240, 269, 260, 263)
    val part1Input = readFile("day1.txt").splitOnLineBreaks().map { it.toInt() }.toList()

    println("Part1")
    println("Test input")
    part1(part1TestInput)

    println("Part1")
    println("Input")
    part1(part1Input)
}

private fun part1(input: List<Int>) {
    val result = input.mapIndexedNotNull { index, i ->
        when {
            index == 0 -> {
                println("None")
                null
            }
            input[index - 1] < i -> {
                println("increaased")
                i
            }
            else -> {
                println("equal or decreased")
                null
            }
        }
    }
    println("Number of increased: ${result.size}")
}
