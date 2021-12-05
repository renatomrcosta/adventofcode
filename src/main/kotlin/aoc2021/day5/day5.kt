package aoc2021.day5

import aoc2021.readFile
import aoc2021.splitOnLineBreaks

private val testInput = """
    0,9 -> 5,9
    8,0 -> 0,8
    9,4 -> 3,4
    2,2 -> 2,1
    7,0 -> 7,4
    6,4 -> 2,0
    0,9 -> 2,9
    3,4 -> 1,4
    0,0 -> 8,8
    5,5 -> 8,2
""".trimIndent()

fun main() {
    println("Part1")
    val file = readFile("day5.txt")
    testInput.parse().run { check(calculatePart1(this) == 5) }
    val part1 = calculatePart1(file.parse())
    println("Part1 = $part1")

    println("----")
    println("Part2")
    testInput.parse().run { check(calculatePart2(this) == 12) }
    val part2 = calculatePart2(file.parse())
    println("Part2 = $part2")
}


private fun calculatePart1(input: List<Pair<IntProgression, IntProgression>>): Int {
    val board = createBoard(input)

    input
        // only vertical and horizontal
        .filter { (x, y) -> x.first == x.last || y.first == y.last }
        .forEach { (xRange, yRange) ->
            val pointsInLine = cartesianOf(xRange, yRange)
            pointsInLine.forEach { (x, y) ->
                board[x][y] += 1
            }
        }

    return board.flatten().count { it >= 2 }
}

private fun calculatePart2(input: List<Pair<IntProgression, IntProgression>>): Int {
    val board = createBoard(input)

    input
        .forEach { (xRange, yRange) ->
            val pointsInLine = pointsOf(xRange, yRange)
            pointsInLine.forEach { (x, y) ->
                board[x][y] += 1
            }
        }

    return board.flatten().count { it >= 2 }
}

private fun createBoard(input: List<Pair<IntProgression, IntProgression>>): List<MutableList<Int>> {
    val xSize = input.maxOf { (x, _) -> if (x.first > x.last) x.first else x.last }
    val ySize = input.maxOf { (_, y) -> if (y.first > y.last) y.first else y.last }
    return List(xSize + 1) { MutableList(ySize + 1) { 0 } }
}

private fun cartesianOf(xRange: IntProgression, yRange: IntProgression): List<Pair<Int, Int>> = buildList {
    xRange.forEach { x ->
        yRange.forEach { y ->
            add(x to y)
        }
    }
}

private fun pointsOf(xRange: IntProgression, yRange: IntProgression): List<Pair<Int, Int>> = buildList {
    if (xRange.first == xRange.last || yRange.first == yRange.last) {
        addAll(cartesianOf(xRange, yRange))
    } else {
        addAll(xRange.zip(yRange))
    }
}


private fun String.parse() = this.splitOnLineBreaks()
    .map {
        val (leftSide, rightSide) = it.split("->").map { it.trim() }
        val (x1, y1) = leftSide.split(",").map { it.toInt() }
        val (x2, y2) = rightSide.split(",").map { it.toInt() }

        val xRange = if (x1 <= x2) (x1..x2) else (x1 downTo x2)
        val yRange = if (y1 <= y2) (y1..y2) else (y1 downTo y2)

        xRange to yRange
    }
    .toList()
