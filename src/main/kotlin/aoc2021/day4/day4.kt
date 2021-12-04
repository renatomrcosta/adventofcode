package aoc2021.day4

import aoc2021.readFile
import aoc2021.splitOnBlankLines
import aoc2021.splitOnLineBreaks

private val testInput = """
7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1

22 13 17 11  0
 8  2 23  4 24
21  9 14 16  7
 6 10  3 18  5
 1 12 20 15 19

 3 15  0  2 22
 9 18 13 17  5
19  8  7 25 23
20 11 10 24  4
14 21 16 12  6

14 21 17 24  4
10 16 15  9 19
18  8 23 26 20
22 11 13  6  5
 2  0 12  3  7
""".trimIndent()

fun main() {
    println("Part1")
    val file = readFile("day4.txt")
    testInput.parseInput().run { check(calculatePart1(this) == 4512) }

    val part1 =  calculatePart1(file.parseInput())
    println("Part1 Result $part1")

}

private fun calculatePart1(bingoInput: BingoInput): Int {
    do {
        val drawnNumber: Int = bingoInput.drawLog.removeFirst()
        with(bingoInput.boards) {
            //mark Number
            forEach { board ->
                board.forEach { row ->
                    row.find { it.number == drawnNumber }?.called = true
                }
            }

            // tries to find winner. If it does, gets the sum and bounces
            forEach { board ->
                if (board.isWinner()) {
                    val sumOfNotCalled = board.flatMap { row -> row.filter { !it.called } }.sumOf { it.number }
                    return sumOfNotCalled * drawnNumber
                }
            }
        }

    } while (true)
}

private fun Board.isWinner(): Boolean = (
        // Tries to find horizontal lines first
        this.find { row -> row.all { it.called } } ?:
        // Otherwise flips the metrix around and checks there for the winner
        buildLenghtwiseMap(this).let { it.values.find { column -> column.all { it.called } } }
        ) != null

private data class BingoInput(val drawLog: ArrayDeque<Int>, val boards: List<Board>)
private data class BingoNumber(val number: Int, var called: Boolean = false)

private typealias Board = List<List<BingoNumber>>

private fun String.parseInput(): BingoInput {
    val input = this.splitOnBlankLines()
    val drawLog = input.first()
        .split(",")
        .map { it.trim().toInt() }

//    println(drawLog)

    val boards = input.drop(1)
        .map { row ->
            row.splitOnLineBreaks()
                .map { items ->
                    items.splitToSequence(" ")
                        .filter { it.isNotBlank() }
                        .map { it.trim() }
                        .map { it.toInt() }
                        .map { BingoNumber(it) }
                        .toList()
                }
                .toList()
        }.toList()
//    println(boards)

    return BingoInput(drawLog = ArrayDeque(drawLog), boards = boards)
}

private fun <T> buildLenghtwiseMap(input: List<List<T>>): Map<Int, List<T>> =
    buildMap<Int, MutableList<T>> {
        input.forEach {
            it.forEachIndexed { index, value ->
                putIfAbsent(index, mutableListOf())
                this[index]?.add(value)
            }
        }
    }