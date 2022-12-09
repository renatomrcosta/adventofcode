package aoc2022.day8

import aoc2022.readFile
import aoc2022.splitOnLineBreaks

private val testData = """
    30373
    25512
    65332
    33549
    35390
""".trimIndent()

fun main() {
    val input = readFile("day8.txt")
    part1(testData).run { require(this == 21) { println("result was $this") } }
    part1(input).run { println("Part1: $this") }

    part2(testData).run { require(this == 8) { println("oops $this")} }
    part2(input).run { println("Part2: $this") }
}

fun part1(input: String): Int {
    val matrix = input.parseInput()
    // aoc2022.prettyPrint(matrix)

    return matrix.mapIndexed { x, row ->
        row.indices.map { y -> (x to y).isVisible(matrix) }
    }.flatten().count { it }
}

fun part2(input: String): Int {
    val matrix = input.parseInput()

    return matrix.mapIndexed { x, row ->
        row.indices.map { y -> (x to y).scenicScore(matrix) }
    }.flatten().max()
}

private fun Pair<Int, Int>.scenicScore(matrix: Matrix): Int {
    val (x, y) = this
    val currentTreeHeight = matrix[x][y]

    when {
        // ignore edges
        x == 0 || y == 0 ||
            y == matrix.lastIndex ||
            x == matrix.first().lastIndex -> return 0
    }

    val ranges = listOf(
        Triple("left", "cell", (y - 1 downTo 0)), // from cell go left in row
        Triple("right", "cell", (y + 1..matrix.lastIndex)), // from cell to right in row
        Triple("up", "row", (x - 1 downTo 0)), // from row go up
        Triple("down", "row", (x + 1..matrix.first().lastIndex)), // from row go down
    )

    return ranges.map { (direction, type, range) ->
        // println("[$x|$y] [$currentTreeHeight] = verifying direction $direction")
        var score = 0
        for(index in range) {
            val item = if (type == "cell") matrix[x][index] else matrix[index][y]
            score++
            if(item >= currentTreeHeight) break
        }
        // println("[$x|$y] [$currentTreeHeight] = $direction result $score")
        score
    }.reduce { acc, score ->  acc * score}
}

private fun Pair<Int, Int>.isVisible(matrix: Matrix): Boolean {
    val (x, y) = this
    val currentTreeHeight = matrix[x][y]

    when {
        // ignore edges
        x == 0 || y == 0 ||
            y == matrix.lastIndex ||
            x == matrix.first().lastIndex -> return true
    }

    val ranges = listOf(
        Triple("left", "cell", (y - 1 downTo 0)), // from cell go left in row
        Triple("right", "cell", (y + 1..matrix.lastIndex)), // from cell to right in row
        Triple("up", "row", (x - 1 downTo 0)), // from row go up
        Triple("down", "row", (x + 1..matrix.first().lastIndex)), // from row go down
    )

    return ranges.any { (direction, type, range) ->
        // println("verifying direction $direction")
        range.all { index ->
            val item = if (type == "cell") matrix[x][index] else matrix[index][y]
            item < currentTreeHeight
        }
        // .also {println("Direction $direction result $it")  }
    }
}

private fun String.parseInput() = this.splitOnLineBreaks()
    .map { it.map { it.digitToInt() } }
    .toList()

typealias Matrix = List<List<Int>>

data class Direction(val xV: Int, val yV: Int)

// val directions = listOf(
//     "left" to Direction(-1, 0),
//     "right" to Direction(+1, 0),
//     "up" to Direction(0, -1),
//     "down" to Direction(0, +1),
// )
//
// return directions.map { (direction, vector) ->
//     generateSequence(
//         seedFunction = { x to y },
//         nextFunction = { (iX, iY) -> iX + vector.xV to iY + vector.yV }
//     )
//         .drop(1)
//         .takeWhile { (currentX, currentY) ->
//             val item = matrix?.getOrNull(currentX)?.getOrNull(currentY)
//             item != null && item < currentTreeHeight
//         }.toList().count()
// }.reduce { acc, score -> acc * score