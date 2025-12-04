package aoc2025.day04

import aoc2025.adjacentIndicesOf
import aoc2025.readFile
import aoc2025.splitOnLineBreaks


val testData = """
    ..@@.@@@@.
    @@@.@.@.@@
    @@@@@.@.@@
    @.@@@@..@.
    @@.@@@@.@@
    .@@@@@@@.@
    .@.@.@.@@@
    @.@@@.@@@@
    .@@@@@@@@.
    @.@.@@@.@.
""".trimIndent()

fun main() {
    val input = readFile("day04.txt")
    part1(testData).run { require(this == 13L) { "Invalid result $this" } }
    part1(input).run { println("Part1: $this") }
}

private fun part1(input: String): Long {
    val grid = input.parseInput()
    return grid.findAllRollWithFewerThanFourAdjacencies().size.toLong()
}

private fun Grid.findAllRollWithFewerThanFourAdjacencies(): List<Position> {
    val grid = this
    return buildList {
        grid.forEachIndexed { i, row ->
            row.forEachIndexed { j, col ->
                if (col) {
                    val adjacentRolls = adjacentIndicesOf(i, j).filter { (xAdjacent, yAdjacent) ->
                        grid.getOrNull(xAdjacent)?.getOrNull(yAdjacent) == true
                    }
                    if (adjacentRolls.size < 4) {
                        add(i to j)
                    }
                }
            }
        }
    }
}

private fun part2(input: String): Long {
    TODO()
}

typealias Grid = List<List<Boolean>>
typealias Position = Pair<Int, Int>

private fun String.parseInput(): Grid {
    return this@parseInput.splitOnLineBreaks().map { line ->
        line.map { c -> c == '@' }
    }.toList()
}