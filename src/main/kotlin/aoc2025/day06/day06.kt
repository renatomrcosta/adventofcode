package aoc2025.day06

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

    part2(testData).run { require(this == 3263827L) { "Invalid result $this" } }
    part2(input).run { println("Part2: $this") }
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


data class OperationGroup(
    val operation: String,
    val numbers: List<Long>,
)

private fun part2(input: String): Long {
    val parsed = input.parseInputP2()
    var acc = mutableListOf<MutableList<String>>()
    val grouped = mutableListOf<OperationGroup>()
    parsed.forEach { group ->
        if (group.all { it.isBlank() }) {
            if (acc.isNotEmpty()) {
                val operation = acc.last().last()
                val groups = acc.eraseOperation()
                    .map { it.joinToString("").trim() }
                    .filter { it.isNotBlank() }
//                .also { println(it) }
                    .map { it.toLong() }

                grouped.add(OperationGroup(operation = operation, numbers = groups))
                acc = mutableListOf()
            }
        } else {
            acc.add(group.toMutableList())
        }
    }

    return grouped.sumOf { (operation, nums) ->
        when (operation) {
            "*" -> nums.reduce { a, b -> a * b }
            "+" -> nums.reduce { a, b -> a + b }
            else -> error("Invalid operation $operation")
        }
    }
}

private fun MutableList<MutableList<String>>.eraseOperation() = map { list ->
    list.map { char ->
        when (char) {
            "*", "+" -> ""
            else -> char
        }
    }
}


@Suppress("UNCHECKED_CAST")
private fun String.parseInput(): List<List<String>> {
    return this.splitOnLineBreaks()
        .map { line -> line.split(" ").filter { it.isNotBlank() } }
        .toList()
        .transpose() as List<List<String>>
}

@Suppress("UNCHECKED_CAST")
private fun String.parseInputP2(): List<List<String>> = splitOnLineBreaks()
    .map { line -> line.reversed().split("") }
    .toList()
    .transpose(substitute = "") as List<List<String>>

private fun <T> List<List<T>>.transpose(substitute: T? = null): List<List<T?>> {
    val rowSize = this.size
    val colSize = this.first().size
    val transposed = MutableList(colSize) { MutableList<T?>(rowSize) { null } }

    for (i in 0 until rowSize) {
        for (j in 0 until colSize) {
//            transposed[j][i] = this[i][j]
            transposed[j][i] = this.getOrNull(i)?.getOrNull(j) ?: substitute
        }
    }
    return transposed
}