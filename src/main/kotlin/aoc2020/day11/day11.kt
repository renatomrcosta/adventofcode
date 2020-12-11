package aoc2020.day11

import aoc2020.splitOnLineBreaks
import aoc2020.withExecutionTime

fun main() {

    testData.parseInput().run {
        withExecutionTime {
            runProcessing(this)
        }
    }
    // readFile("day11.txt").parseInput().run {
    //     withExecutionTime {
    //         runProcessing(this)
    //     }
    // }
}

private fun runProcessing(input: List<MutableList<String>>) {
    var rules = input
    val iterationCounterList = mutableListOf(0)
    do {
        rules = applyRules(rules)
        prettyPrint(rules)
        val count = rules.flatten().count { it == "#" }
        iterationCounterList.add(count)
        val (first, last) = iterationCounterList.takeLast(2)
    } while (first != last)
    println(rules)
    println(iterationCounterList.last())
}

fun prettyPrint(rules: List<MutableList<String>>) {
    println("--------")
    rules.map { it.reduce { acc, s -> acc + s } }.forEach { println(it) }
    println("--------")
}

fun applyRules(input: List<MutableList<String>>) =
    input.mapIndexed { rowIndex, row ->
        row.mapIndexed { valueIndex, value ->
            if (value != ".") {
                if (value == "L") {
                    // if (countOccupiedAdjacentSeats(valueIndex, rowIndex, input) == 0) "#"
                    if (countOccupiedRowSeats(valueIndex, rowIndex, input) == 0) "#"
                    else value
                } else {
                    // if (countOccupiedAdjacentSeats(valueIndex, rowIndex, input) >= 4) "L"
                    if (countOccupiedRowSeats(valueIndex, rowIndex, input) >= 5) "L"
                    else value
                }
            } else value
        }.toMutableList()
    }

fun countOccupiedRowSeats(
    cellIndex: Int,
    rowIndex: Int,
    input: List<List<String>>,
): Int {
    val rowSize = input.size
    val cellSize = input[0].size

    var counter = 0
    // Define ranges
    val ranges = listOf(
        Triple("left", "cell", (cellIndex - 1 downTo 0)), // from cell go left in row
        Triple("right", "cell", (cellIndex + 1 until cellSize)), // from cell to right in row
        Triple("up", "row", (rowIndex - 1 downTo 0)), // from row go up
        Triple("down", "row", (rowIndex + 1 until rowSize)), // from row go down
    )
    // mix and match the other 4 directions
    val diagonals = listOf(
        ranges.first { it.first == "left" }.third to ranges.first { it.first == "up" }.third,
        ranges.first { it.first == "left" }.third to ranges.first { it.first == "down" }.third,
        ranges.first { it.first == "right" }.third to ranges.first { it.first == "up" }.third,
        ranges.first { it.first == "right" }.third to ranges.first { it.first == "down" }.third,
    )

    for ((_, type, range) in ranges) {
        for (i in range) {
            val item = if (type == "cell") input[rowIndex][i] else input[i][cellIndex]
            when (item) {
                "#" -> {
                    counter++
                    break
                }
                "L" -> break
            }
        }
    }

    for ((cellRange, rowRange) in diagonals) {
        cellFor@ for (cellRangeIndex in cellRange) {
            rowFor@ for (rowRangeIndex in rowRange) {
                if (rowRangeIndex == cellRangeIndex) { // God I was lazy, and wanted this to be over. Forgive me
                    val item = input[rowRangeIndex][cellRangeIndex]
                    when (item) {
                        "#" -> {
                            counter++
                            break@cellFor
                        }
                        "L" -> break@cellFor
                        else -> break@rowFor
                    }
                }
            }
        }
    }

    return counter
}

fun countOccupiedAdjacentSeats(
    cellIndex: Int,
    rowIndex: Int,
    input: List<List<String>>,
): Int {
    var counter = 0
    val rowRange = ((rowIndex - 1)..(rowIndex + 1))
    val cellRange = ((cellIndex - 1)..(cellIndex + 1))

    rowRange.forEach { rowRangeIndex ->
        cellRange.forEach { cellRangeIndex ->
            if (!(rowRangeIndex == rowIndex && cellRangeIndex == cellIndex)) {
                val row = input.getOrNull(rowRangeIndex)
                val value = row?.getOrNull(cellRangeIndex)
                if (value == "#") {
                    counter++
                }
            }
        }
    }
    return counter
}

fun String.parseInput() =
    this.splitOnLineBreaks()
        .filter { it.isNotEmpty() }
        .map { it.trim() }
        .map { row ->
            row.map {
                it.toString()
            }.toMutableList()
        }

val testData = """
    L.LL.LL.LL
    LLLLLLL.LL
    L.L.L..L..
    LLLL.LL.LL
    L.LL.LL.LL
    L.LLLLL.LL
    ..L.L.....
    LLLLLLLLLL
    L.LLLLLL.L
    L.LLLLL.LL

""".trimIndent()
