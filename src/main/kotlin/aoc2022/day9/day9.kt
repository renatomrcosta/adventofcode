package aoc2022.day9

import aoc2022.readFile
import aoc2022.splitOnLineBreaks
import kotlin.math.abs

private val testData = """
    R 4
    U 4
    L 3
    D 1
    R 4
    D 1
    L 5
    R 2
""".trimIndent()

private val testData2 = """
    R 5
    U 8
    L 8
    D 3
    R 17
    D 10
    L 25
    U 20
""".trimIndent()

fun main() {
    val input = readFile("day9.txt")
    part1(testData).run { require(this == 13) { "Result was $this" } }
    part1(input).run { println("Part1: $this") }

    part2(testData).run { require(this == 1) { "Result was $this" } }
    part2(testData2).run { require(this == 36) { "Result was $this" } }
    part2(input).run { println("Part2: $this") }
}

private fun part1(input: String): Int = move(input, 1)
private fun part2(input: String): Int = move(input, 9)

private fun move(input: String, knots: Int): Int {
    var headPosition = startingPoint
    var tailPositions = (1..knots).map { startingPoint }
    val lastTailLog = mutableSetOf(startingPoint)

    input.parseInput().forEach { (direction, steps) ->
        repeat(steps) {
            headPosition = direction.moveHead(headPosition)
            val newTails = mutableListOf<Position>()
            tailPositions.forEachIndexed { index, tail ->
                newTails.add(
                    moveTail(newTails.getOrNull(index - 1) ?: headPosition, tail)
                )
            }
            tailPositions = newTails
            lastTailLog.add(tailPositions.last())
        }
    }
    return lastTailLog.size
}

private fun String.parseInput() = this.splitOnLineBreaks()
    .map {
        val (direction, steps) = it.split(" ")
        direction.toDirection() to steps.toInt()
    }

private fun Position.isTouching(headPosition: Position): Boolean {
    val (x, y) = this
    val (hx, hy) = headPosition

    return abs(x - hx) <= 1 && abs(y - hy) <= 1
}

private enum class Direction(val value: String, val vector: Position) {
    Up(value = "U", vector = Position(0, 1)),
    Down(value = "D", vector = Position(0, -1)),
    Left(value = "L", vector = Position(-1, 0)),
    Right(value = "R", vector = Position(1, 0));

    fun moveHead(headPosition: Position): Position {
        val (vx, vy) = vector
        val (x, y) = headPosition
        return x + vx to y + vy
    }
}

// Ugly af, don't care
private fun moveTail(headPosition: Position, tailPosition: Position): Position {
    val (headX, headY) = headPosition
    val (tailX, tailY) = tailPosition
    return when {
        tailPosition.isTouching(headPosition) -> tailPosition

        headX == tailX -> {
            // they are in the same row, diagonal has changed
            if (headY > tailY) {
                tailX to tailY + 1
            } else {
                tailX to tailY - 1
            }
        }

        headY == tailY -> {
            // they are in the same col, row has changed
            if (headX > tailX) {
                tailX + 1 to tailY
            } else {
                tailX - 1 to tailY
            }
        }

        else -> {
            // move towards the diagonal
            if (headX > tailX) {
                tailX + 1
            } else {
                tailX - 1
            } to if (headY > tailY) {
                tailY + 1
            } else {
                tailY - 1
            }
        }
    }
}

private fun String.toDirection() = Direction.values().first { it.value == this }

typealias Position = Pair<Int, Int>

val startingPoint = 0 to 0