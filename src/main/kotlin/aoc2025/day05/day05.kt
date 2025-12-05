package aoc2025.day05

import aoc2025.readFile
import aoc2025.splitOnBlankLines
import aoc2025.splitOnLineBreaks

private val testData = """
    3-5
    10-14
    16-20
    12-18

    1
    5
    8
    11
    17
    32
""".trimIndent()

fun main() {
    val input = readFile("day05.txt")
//    part1(testData).run { require(this == 3L) { "Invalid result $this" } }
//    part1(input).run { println("Part1: $this") }

    part2(testData).run { require(this == 14L) { "Invalid result $this" } }
    part2(input).run { println("Part2: $this") }
}

private fun part1(input: String): Long =
    input
        .parseInput()
        .findAllFreshIngredients()
        .size.toLong()


private fun part2(input: String): Long = input.parseInput().countAmountOfPossibleFreshIngredientId()

class Ingredients(
    val ranges: List<LongRange>,
    val ingredients: List<Long>,
) {
    fun findAllFreshIngredients(): List<Long> = ingredients.filter { ingredient ->
        ranges.any { it.contains(ingredient) }
    }

    fun countAmountOfPossibleFreshIngredientId(): Long {
        var currentFirst = ranges.first().first
        var currentLast = ranges.first().last
        var count = 0L

        fun doCount() {
            count += currentLast - currentFirst + 1
        }

        for (range in ranges.drop(1)) {
            if (range.first <= currentLast + 1) {
                if (range.last > currentLast) {
                    currentLast = range.last
                }
            } else {
                doCount()
                currentFirst = range.first
                currentLast = range.last
            }
        }
        doCount()
        return count
    }

}

private fun String.parseInput(): Ingredients {
    val (ranges, ingredients) = this.splitOnBlankLines().toList()

    val parsedRanges = ranges.splitOnLineBreaks()
        .map { row ->
            val (l, r) = row.split('-')
            l.toLong()..r.toLong()
        }.toList()

    val parsedIngredients = ingredients.splitOnLineBreaks()
        .filter { it.isNotBlank() }
        .map { it.toLong() }
        .toList()

    return Ingredients(
        ranges = parsedRanges.sortedBy { it.first },
        ingredients = parsedIngredients
    )
}