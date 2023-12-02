package aoc2023.day02

import aoc2023.readFile
import aoc2023.splitOnLineBreaks

private val testInput = """
    Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
    Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
    Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
    Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
    Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
""".trimIndent()

fun main() {
    part1(testInput).run { require(this == 8L) }
    part1(readFile("day2.txt")).run { println("Part 1: $this") }

    part2(testInput).run { require(this == 2286L) }
    part2(readFile("day2.txt")).run { println("Part 2: $this") }
}

private fun part1(input: String): Long {
    val limits = mapOf(
        "red" to 12L,
        "green" to 13L,
        "blue" to 14L,
    )
    val gameSets = buildGameSets(input)
    println(gameSets)

    return gameSets
        .filterValues { set -> set.isValid(limits) }
        .keys
        .sum()
}

private fun part2(input: String): Long = buildGameSets(input)
    .values
    .sumOf { game ->
        val maxGreen = game.maxOf { it["green"] ?: 0L }
        val maxRed = game.maxOf { it["red"] ?: 0L }
        val maxBlue = game.maxOf { it["blue"] ?: 0L }
        maxGreen * maxRed * maxBlue
    }

private fun List<Map<String, Long>>.isValid(limits: Map<String, Long>): Boolean =
    this.all { gameRound ->
        limits.entries.all { (color, limit) ->
            val gameValue = gameRound[color] ?: 0L
            gameValue <= limit
        }
    }

private fun buildGameSets(input: String) = input.splitOnLineBreaks()
    .map { line ->
        val (gameDefinition, setDefinition) = line.split(":")
        val gameId = gameDefinition.split(" ").last().toLong()
        val sets = setDefinition.split(";")
            .map { it.buildGameMapping() }

        gameId to sets
    }
    .toMap()

private fun String.buildGameMapping(): Map<String, Long> {
    return this.split(",")
        .map {
            val (number, color) = it.trim().split(" ")
            color to number.toLong()
        }
        .groupBy { it.first }
        .mapValues { it.value.sumOf { it.second } }
        .toMap()
}
