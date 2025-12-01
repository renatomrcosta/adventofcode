package aoc2025.day01

import aoc2025.readFile
import aoc2025.splitOnLineBreaks

val testData = """
    L68
    L30
    R48
    L5
    R60
    L55
    L1
    L99
    R14
    L82
""".trimIndent()

fun main() {
    val input = readFile("day01.txt")
    part1(testData).run { require(this == 3) }
    part1(input).run { println("Part1: $this") }

    part2(testData).run { require(this == 6) }
    part2(input).run { println("Part2: $this") }

}

fun part2(input: String): Int {
    val parsedInput = input.parse()
    val safe = Safe()
    parsedInput.forEach { safe.turn(it) }
    return safe.clicksOnZero
}

fun part1(input: String): Int {
    val parsedInput = input.parse()
    val safe = Safe()
    val allValues = parsedInput.map {
        safe.turn(it)
        safe.currentValue
    }
    return allValues.filter { it == 0 }.size
}

class Safe {
    var clicksOnZero = 0
    var currentValue = 50
    private val minValue = 0
    private val maxValue = 99

    fun turn(turn: Turn) {
        repeat(turn.number) {
            doTurn(turn.direction)
        }
    }

    private fun doTurn(direction: Direction) {
        when (direction) {
            Direction.L -> {
                if (currentValue == minValue) {
                    currentValue = maxValue
                } else {
                    currentValue -= 1
                }
            }

            Direction.R -> {
                if (currentValue == maxValue) {
                    currentValue = minValue
                } else {
                    currentValue += 1
                }
            }
        }
        if(currentValue == 0) {
            clicksOnZero++
        }
    }
}

enum class Direction { L, R; }
data class Turn(val direction: Direction, val number: Int)

fun String.parse(): List<Turn> =
    this.splitOnLineBreaks()
        .map {
            val direction = it.take(1).toDirection()
            val number = it.drop(1).toInt()
            Turn(direction, number)
        }.toList()

fun String.toDirection(): Direction = Direction.valueOf(this.uppercase())