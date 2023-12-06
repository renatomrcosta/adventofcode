package aoc2023.day03

import aoc2023.readFile
import aoc2023.splitOnLineBreaks
import aoc2023.surroundingIndices

private val testInput = """
    467..114..
    ...*......
    ..35..633.
    ......#...
    617*......
    .....+.58.
    ..592.....
    ......755.
    ...${'$'}.*....
    .664.598..
""".trimIndent()

fun main() {
    part1(testInput).run { require(this == 4361L) }
    part1(readFile("day3.txt")).run { println("Part 1: $this") }

    part2(testInput).run { require(this == 467835L) }
    part2(readFile("day3.txt")).run { println("Part 2: $this") }
}

private fun part2(input: String): Long {
    val digitRegex = Regex("\\D+")

    val lines = input.splitOnLineBreaks()

    val rowValueMapIndex = lines
        .mapIndexed { row, line ->
            // Got you, repeated number in the row motherfucker
            var startIndex = 0
            digitRegex.split(line)
                .filter { it.isNotEmpty() }
                .map { number ->
                    val index = line.indexOf(number, startIndex = startIndex)
                    startIndex = index + number.length
                    (number.toLong() to (row to index..<index + number.length))
                }
        }.filter { it.isNotEmpty() }
        .toList()
        .flatten()
        .also { println(it) }


    val gearIndices = lines.mapIndexed { rowIndex, row ->
        row.mapIndexedNotNull { charIndex, char ->
            if (char == '*') {
                val surroundingIndices = surroundingIndices(rowIndex, charIndex)

                val adjacentValues = rowValueMapIndex.filter { (_, coordinates) ->
                    val (valueRow, valueCols) = coordinates
                    val possibleCoordinates = valueCols.map { valueRow to it }
                    possibleCoordinates.any { it in surroundingIndices }
                }

                if (adjacentValues.size == 2) {
                    return@mapIndexedNotNull adjacentValues.map { it.first }.reduce { acc, l -> acc * l }
                }
            }
            null
        }
    }.flatten()
        .sum()

    return gearIndices
}

private fun part1(input: String): Long {
    val digitRegex = Regex("\\D+")
    // build a map of numbers vs indices
    val lines = input.splitOnLineBreaks()
    val rowValueMapIndex = lines
        .mapIndexed { row, line ->
            // Got you, repeated number in the row motherfucker
            var startIndex = 0
            digitRegex.split(line)
                .filter { it.isNotEmpty() }
                .map { number ->
                    val index = line.indexOf(number, startIndex = startIndex)
                    startIndex = index + number.length
                    (row to (number to index..<index + number.length))
                }
        }.filter { it.isNotEmpty() }
        .toList()
        .flatten()
        .also { println(it) }

    val symbolIndices = lines.mapIndexed { rowIndex, row ->
        row.mapIndexedNotNull { charIndex, char ->
            if (!char.isDigit() && char != '.') {
                rowIndex to charIndex
            } else {
                null
            }
        }
    }.filter { it.isNotEmpty() }
        .flatten()
        .map { (x, y) ->
            surroundingIndices(x, y)
        }
        .flatten()
        .toSet()
        .also { println(it) }

    return rowValueMapIndex.mapNotNull { (rowIndex, pair) ->
        val (number, cols) = pair
        cols.forEach { col ->
            if (symbolIndices.contains(rowIndex to col)) {
                return@mapNotNull number.toLong()
            }
        }
        null
    }.also { println(it) }.sum().also { println(it) }
}
