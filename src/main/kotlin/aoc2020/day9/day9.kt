package aoc2020.day9

import aoc2020.readFile
import kotlin.math.absoluteValue

fun main() {
    part1()
}

fun part1() {
    println("Part 1 - Test Data")
    parseInput(testData).run {
        val outlier = findOutlier(this, 5).also { println("Outlier is $it") }
        println("Part2 - Test Data")
        findContiguousSum(this, outlier)
            .filter { it.size > 1 }
            .map { getResult(it) }
            .forEach { println(it) }

    }

    println("Part 1 - Input Data")
    parseInput(readFile("day9.txt")).run {
        val outlier = findOutlier(this, 25).also { println("Outlier is $it") }
        println("Part2 - Input Data")
        findContiguousSum(this, outlier)
            .filter { it.size > 1 }
            .also { println(it) }
            .map { getResult(it) }
            .forEach { println(it) }
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

fun findContiguousSum(input: List<Long>, target: Long): List<List<Long>> {
    // Consider that more than one combination might exist?
    val results = mutableListOf<List<Long>>()
    for (index in input.indices - 1) {
        input
            .drop(index)
            .runningFoldIndexed(0L) { foldIndex, acc, item ->
                val sum = acc + item
                if (sum == target) {
                    results.add(input.subList(index, (foldIndex + index) + 1))
                } else if (sum > target) {
                    // Break the fold???
                }
                sum
            }
    }
    return results
}

fun getResult(contiguousSumList: List<Long>): Long {
    val min = contiguousSumList.minOrNull() ?: error("something funky. No min value")
    val max = contiguousSumList.maxOrNull() ?: error("something funky. No max value")
    return min + max
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
