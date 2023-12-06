package aoc2023.day04

import aoc2023.readFile
import aoc2023.splitOnLineBreaks
import kotlin.math.pow

private val testInput = """
    Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
    Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
    Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
    Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
    Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
    Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
""".trimIndent()

fun main() {
    part1(testInput).run { require(this == 13L) }
    part1(readFile("day4.txt")).run { println("Part1: $this") }

    part2(testInput).run { require(this == 30L) }
    part2(readFile("day4.txt")).run { println("Part2: $this") }
}

private fun part1(input: String): Long {
    return input.toListInput()
        .map { (_, winners, drawn) ->
            when (val matches = drawn.intersect(winners.toSet()).size) {
                0 -> 0.0
                else -> 2.0.pow(matches - 1.0)
            }
        }
//        .also { println(it.toList()) }
        .sum().toLong()
}

private fun part2(input: String): Long {
    val mappedInput = input.toListInput()
        .associate { (index, winners, drawn) -> index to (winners to drawn) }
        .toMutableMap()

    val buckets = mappedInput.entries.associate { (index, _) -> index to 1 }.toMutableMap()

    mappedInput.entries
        .forEach { (index, card) ->
            val (winners, drawn) = card
            val matches = drawn.intersect(winners.toSet()).size

            repeat(buckets[index]!!) {
                (index + 1..index + matches).forEach {
                    buckets[it] = 1 + (buckets[it]!!)
                }
            }
        }

    return buckets.values.sum().toLong()
}

private fun String.splitNumbers() = this.splitToSequence(" ")
    .map { it.trim() }
    .filter { it.isNotEmpty() }
    .map { it.toInt() }
    .toList()

private fun String.toListInput() = this.splitOnLineBreaks()
    .mapIndexed { idx, line ->
        val (_, numbers) = line.split(":")
        val (winners, drawn) = numbers.split("|")
        Triple(idx + 1, winners.splitNumbers(), drawn.splitNumbers())
    }
