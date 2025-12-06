package aoc2025.day06

import aoc2025.println
import aoc2025.readFile
import aoc2025.splitOnLineBreaks

private val testData = """
    123 328  51 64 
     45 64  387 23 
      6 98  215 314
    *   +   *   +  
""".trimIndent()

fun main() {
    val input = readFile("day06.txt")

    part1(testData).run { require(this == 4277556L) { "Invalid result $this" } }
    part1(input).run { println("Part1: $this") }
}

private fun part1(input: String): Long {
    return input.parseInput().sumOf { row ->
        val operation = row.last()
        val nums = row.dropLast(1).map { it.toLong() }
        when (operation) {
            "*" -> nums.reduce { a, b -> a * b }
            "+" -> nums.reduce { a, b -> a + b }
            else -> error("Invalid operation $operation")
        }
    }
}


private fun part2(input: String): Long = TODO()


@Suppress("UNCHECKED_CAST")
private fun String.parseInput(): List<List<String>> {
    return this.splitOnLineBreaks()
        .map { line -> line.split(" ").filter { it.isNotBlank() } }
        .toList()
        .transpose() as List<List<String>>
}

private fun <T> List<List<T>>.transpose(): List<List<T?>> {
    val rowSize = this.size
    val colSize = this.first().size
    val transposed = MutableList(colSize) { MutableList<T?>(rowSize) { null } }

    for (i in 0 until rowSize) {
        for (j in 0 until colSize) {
            transposed[j][i] = this[i][j]
        }
    }
    return transposed
}