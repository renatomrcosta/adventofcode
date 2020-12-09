package aoc2020.day9

import aoc2020.readFile
import kotlin.math.absoluteValue

fun main() {
    part1()
}

fun part1() {
    println("Part 1 - Test Data")
    parseInput(testData).run {
        findOutlier(this, 5).also { println("Outlier is $it") }
    }

    println("Part 1 - Input Data")
    parseInput(readFile("day9.txt")).run {
        findOutlier(this, 25).also { println("Outlier is $it") }
    }
}

fun findOutlier(input: List<Long>, preamble: Int): Long {
    for ((startIndex, itemIndex) in ((preamble)..input.lastIndex).withIndex()) {
        val slice = input.subList(startIndex, itemIndex)
        val item = input[itemIndex]
        if (!hasSumInSlice(slice, item)) return item
    }
    error("no outlier found!")
}

fun hasSumInSlice(slice: List<Long>, target: Long): Boolean {
    slice.asReversed().forEach { item ->
        val operand = target.absoluteValue - item.absoluteValue
        if (slice.contains(operand.absoluteValue)) return true
    }
    return false
}

fun parseInput(input: String): List<Long> =
    input.split("\n")
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .map { it.toLong() }

val testData = """
    35
    20
    15
    25
    47
    40
    62
    55
    65
    95
    102
    117
    150
    182
    127
    219
    299
    277
    309
    576
    
""".trimIndent()
