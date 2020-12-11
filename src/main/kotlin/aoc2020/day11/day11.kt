package aoc2020.day11

import aoc2020.readFile
import aoc2020.splitOnLineBreaks

fun main() {
    testData.parseInput().run {
        runProcessing(this)
    }
    readFile("day11.txt").parseInput().run {
        runProcessing(this)
    }


}

private fun runProcessing(input: List<MutableList<String>>) {
    var rules = input
    val iterationCounterList = mutableListOf(0)
    do {
        rules = applyRules(rules)
        val count = rules.flatten().count { it == "#" }
        iterationCounterList.add(count)
        val (first, last) = iterationCounterList.takeLast(2)
    } while (first != last)
    println(rules)
    println(iterationCounterList.last())
}

fun applyRules(input: List<MutableList<String>>) =
    input.mapIndexed { rowIndex, row ->
        row.mapIndexed { valueIndex, value ->
            if (value != ".") {
                if (value == "L") {
                    if (countOccupiedAdjacentSeats(valueIndex, rowIndex, input) == 0) "#"
                    else value
                } else {
                    if (countOccupiedAdjacentSeats(valueIndex, rowIndex, input) >= 4) "L"
                    else value
                }
            } else value
        }.toMutableList()
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
