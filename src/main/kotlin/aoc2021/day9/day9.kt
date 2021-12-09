package aoc2021.day9

import aoc2021.readFile
import aoc2021.splitOnLineBreaks

private val testInput = """
    2199943210
    3987894921
    9856789892
    8767896789
    9899965678
""".trimIndent()

fun main() {
    val file = readFile("day9.txt")
    println("Part1")
    calculatePart1(testInput.parse()).run { check(this == 15) }
    calculatePart1(file.parse()).run { println("Part1 Result: $this") }
}

private fun calculatePart1(input: Array<IntArray>): Int {
    val lowPoints = getAllLowPoints(input)
    return lowPoints.sumOf { it + 1 }
}

private fun getAllLowPoints(input: Array<IntArray>): List<Int> = buildList {
    input.forEachIndexed { i, row ->
        row.forEachIndexed { j, cell ->
            val adjacents = input.getAdjacentOfIndex(i, j)
            if (adjacents.all { it > cell }) {
                add(cell)
            }
        }
    }
}

private fun Array<IntArray>.getAdjacentOfIndex(x: Int, y: Int): List<Int> {
    val left = this.getOrNull(x - 1)?.getOrNull(y)
    val right = this.getOrNull(x + 1)?.getOrNull(y)
    val up = this.getOrNull(x)?.getOrNull(y - 1)
    val down = this.getOrNull(x)?.getOrNull(y + 1)

    return listOfNotNull(left, right, up, down)
}

private fun String.parse() = this.splitOnLineBreaks()
    .map { it.split("") }
    .map { it.filter { it.isNotBlank() } }
    .map { it.map { it.toInt() }.toIntArray() }
    .toList()
    .toTypedArray()