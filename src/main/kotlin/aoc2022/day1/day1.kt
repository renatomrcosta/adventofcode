package aoc2022.day1

import aoc2022.readFile
import aoc2022.splitOnBlankLines
import aoc2022.splitOnLineBreaks

val testInput = """
    1000
    2000
    3000

    4000

    5000
    6000

    7000
    8000
    9000

    10000
""".trimIndent()

fun main() {
    part1()
    part2()
}

private fun part1() {
    // Test
    groupCaloriesPerElf(testInput).run {
        val mostCalories = getMostCalories()
        require(mostCalories == 24_000)
    }
    // Input
    val file = readFile("day1.txt")
    groupCaloriesPerElf(file).run {
        println("Most Calories: ${getMostCalories()}")
    }
}

private fun part2() {
    // Test
    groupCaloriesPerElf(testInput).run {
        val topThreeCalories = getTopThreeCaloriesSum()
        require(topThreeCalories == 45_000)
    }
    // Input
    val file = readFile("day1.txt")
    groupCaloriesPerElf(file).run {
        println("Top 3: ${getTopThreeCaloriesSum()}")
    }
}

private fun List<List<Int>>.getMostCalories() = this.map { it.sum() }.maxOf { it }
private fun List<List<Int>>.getTopThreeCaloriesSum() = this.map { it.sum() }.sortedDescending().take(3).sum()

private fun groupCaloriesPerElf(input: String): List<List<Int>> {
    return input.splitOnBlankLines().toList()
        .map { it.splitOnLineBreaks().toList() }
        .map { it.map { it.toInt() } }
        .toList()
}