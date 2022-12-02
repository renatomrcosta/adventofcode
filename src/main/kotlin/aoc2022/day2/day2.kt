package aoc2022.day2

import aoc2022.readFile
import aoc2022.splitOnLineBreaks

val testData = """
    A Y
    B X
    C Z
""".trimIndent()

fun main() {
    part1()
}

private fun part1() {
    testData.parseInput().sumOf { (opponent, player) -> player.play(opponent) }.run { require(this == 15) }
    readFile("day2.txt").parseInput().sumOf { (opponent, player) -> player.play(opponent) }.run { println("Total: $this")}
}

private fun String.parseInput() =
    this.splitOnLineBreaks()
        .map { it.split(" ") }
        .map { it.first().toRPS() to it.last().toRPS() }

private enum class RPS(private val score: Int) {
    Rock(score = 1), Paper(score = 2), Scissors(score = 3);

    fun play(oppenent: RPS): Int {
        val outcome = when (this) {
            Rock -> when (oppenent) {
                Rock -> DRAW
                Paper -> LOSE
                Scissors -> WIN
            }

            Paper -> when (oppenent) {
                Rock -> WIN
                Paper -> DRAW
                Scissors -> LOSE
            }

            Scissors -> when (oppenent) {
                Rock -> LOSE
                Paper -> WIN
                Scissors -> DRAW
            }
        }
        return outcome + this.score
    }
}

private fun String.toRPS(): RPS = when (this.uppercase()) {
    "A", "X" -> RPS.Rock
    "B", "Y" -> RPS.Paper
    "C", "Z" -> RPS.Scissors
    else -> error("Invalid input <$this>")
}

private const val LOSE = 0
private const val DRAW = 3
private const val WIN = 6