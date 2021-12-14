package aoc2021.day13

import aoc2021.prettyPrint
import aoc2021.readFile
import aoc2021.splitOnBlankLines
import aoc2021.splitOnLineBreaks
import kotlin.math.abs


fun main() {
    val file = readFile("day13.txt")
//    calculatePart1(testData.parse()).run { check(this == 17) }
//    calculatePart1(testData2.parse())
//    calculatePart1(file.parse()).run { println("Part1: $this") }

//    println("Part2 Test")
//    calculatePart2(testData.parse())
//    println("Part2")
    calculatePart2(file.parse())
}

private fun calculatePart2(input: Input): Int {
    var board = input.board
    input.folds
        .forEach { instruction ->
            board = fold(board, instruction)
        }
    // Should be CEJKLUGJ
    prettyPrint(board)
    return board.flatten().count { it == "#" }
}


private fun calculatePart1(input: Input): Int {
    var board = input.board
    input.folds
        .take(1)
        .forEach { instruction ->
            board = fold(board, instruction)
        }
    return board.flatten().count { it == "#" }
}

private fun fold(
    board: MutableList<MutableList<String>>,
    fold: Pair<String, Int>
): MutableList<MutableList<String>> {
    val (direction, index) = fold
    val boardWidth = if (direction == "x") index else board.first().size
    val boardHeight = if (direction == "y") index else board.size
    val newBoard = MutableList(boardHeight) { MutableList(boardWidth) { "." } }

    (0 until boardHeight).forEach { i ->
        (0 until boardWidth).forEach { j ->
            if (newBoard[i][j] != "#") newBoard[i][j] = board.getOrNull(i)?.getOrNull(j) ?: "."
        }
    }

//
//    println("Board")
//    prettyPrint(board)

    if (direction == "x") {
        val folded = board.map { it.reversed().dropLast(index + 1).reversed() }
//        prettyPrint(folded)
        (0 until boardHeight).forEach { i ->
            (abs(boardWidth - folded.first().size) until boardWidth).forEach { j ->
                if (newBoard[i][j] != "#") newBoard[i][j] = folded.getOrNull(i)?.getOrNull(j) ?: "."
            }
        }
    } else {
        val folded = board.reversed().dropLast(index + 1)
//        prettyPrint(folded)
        (abs(boardHeight - folded.size) until boardHeight).forEach { i ->
            (0 until boardWidth).forEach { j ->
                if (newBoard[i][j] != "#") newBoard[i][j] = folded.getOrNull(i)?.getOrNull(j) ?: "."
            }
        }
    }

//    prettyPrint(newBoard)

    return newBoard

}

private fun String.parse(): Input {
    val (boardPart, foldingList) = this.splitOnBlankLines().toList()

    val coordinates = boardPart.splitOnLineBreaks()
        .map { row ->
            val (x, y) = row.split(",")
            x.toInt() to y.toInt()
        }
        .toList()

    val height = coordinates.maxOf { (_, y) -> y } + 1
    val width = coordinates.maxOf { (x, _) -> x } + 1

    val board = MutableList(height) { MutableList(width) { "." } }
    coordinates.forEach { (x, y) ->
        board[y][x] = "#"
    }

    val folds = foldingList.splitOnLineBreaks()
        .map { it.split("=") }
        .map { (left, right) -> left.last().toString() to right.toInt() }
        .toList()

    return Input(
        board = board,
        folds = folds,
    )
}

private data class Input(
    val board: MutableList<MutableList<String>>,
    val folds: List<Pair<String, Int>>,
)

private val testData = """
    6,10
    0,14
    9,10
    0,3
    10,4
    4,11
    6,0
    6,12
    4,1
    0,13
    10,12
    3,4
    3,0
    8,4
    1,10
    2,14
    8,10
    9,0

    fold along y=7
    fold along x=5
""".trimIndent()

private val testData2 = """
    6,10
    0,14
    9,10
    0,3
    10,4
    4,11
    6,0
    6,12
    4,1
    0,13
    10,12
    3,4
    3,0
    8,4
    1,10
    2,14
    8,10
    9,0

    fold along x=7
    fold along y=7
""".trimIndent()