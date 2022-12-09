package aoc2022.day9

import aoc2022.readFile
import aoc2022.splitOnBlankLines
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

fun main() {
    val input = readFile("day9.txt")
    part1(testData).run { require(this == 13) { "Result was $this" } }
    part1(input).run { println("Part1: $this") }
}

private fun part1(input: String): Int {
    var headPosition = startingPoint
    var tailPosition = startingPoint

    val tailLog = mutableMapOf<Position, Int>(startingPoint to 1)
    input.parseInput().forEach { (direction, steps) ->
        repeat(steps) {
            headPosition = direction.moveHead(headPosition)
            tailPosition = direction.moveTail(headPosition, tailPosition)
            tailLog.addTailPositionCount(tailPosition)
        }
    }
    return tailLog.size
}

private fun MutableMap<Position, Int>.addTailPositionCount(tailPosition: Position) {
    if (!this.containsKey(tailPosition)) this[tailPosition] = 0
    this[tailPosition] = this[tailPosition]?.plus(1) ?: error("aaaa")
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

    fun moveTail(headPosition: Position, tailPosition: Position): Position {
        val (headX, headY) = headPosition
        val (tailX, tailY) = tailPosition
        return when {
            tailPosition.isTouching(headPosition) -> tailPosition

            headX == tailX -> {
                // they are in the same row, diagonal has changed
                if(headY > tailY) {
                    tailX  to tailY + 1
                } else {
                    tailX to tailY -1
                }
            }

            headY == tailY -> {
                // they are in the same col, row has changed
                if(headX > tailX) {
                    tailX + 1 to tailY
                } else {
                    tailX -1 to tailY
                }
            }

            else -> {
                // move towards the diagonal
                if(headX > tailX) { tailX + 1 } else { tailX -1 } to if(headY > tailY) { tailY + 1 } else { tailY -1 }
            }
        }
    }
}

private fun String.toDirection() = Direction.values().first { it.value == this }

typealias Position = Pair<Int, Int>

val startingPoint = 0 to 0