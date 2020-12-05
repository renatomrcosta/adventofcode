package aoc2020.day5

import aoc2020.readFile

fun main() {
    println("Part1 - Test Data assertion")
    testData.forEach { (input, result) ->
        assert(input.toSeat() == result)
    }

    println("Part 1 - Real Data")
    val inputList = getInputsFromFile()

    // Get biggest ID
    inputList
        .map { it.toSeat() }
        .sortedBy { it.id }
        .onEach { println(it) }
        .maxByOrNull { it.id }
        .run { println("Result - $this") }
}

private val PLANE_ROW_RANGE = 0..127
private val PLANE_COL_RANGE = 0..7

// F -> Lower split row
// B -> Higher split row
// L -> Lower split col
// R -> Higher split col
private fun String.toSeat(): Seat {
    val row = chunkRanges(
        startingRange = PLANE_ROW_RANGE,
        input = this.substring(0..6),
        lowerLetter = 'F',
        higherLetter = 'B',
    )
    val col = chunkRanges(
        startingRange = PLANE_COL_RANGE,
        input = this.substring(7),
        lowerLetter = 'L',
        higherLetter = 'R',
    )

    return Seat(row, col)
}

fun chunkRanges(
    startingRange: IntRange,
    input: String,
    lowerLetter: Char,
    higherLetter: Char,
): Int {
    var seatRange = startingRange
    for (letter in input) {
        val (lowerChunk, higherChunk) = seatRange.chunked(seatRange.count() / 2)
        seatRange = when (letter) {
            lowerLetter -> {
                IntRange(lowerChunk.first(), lowerChunk.last())
            }
            higherLetter -> {
                IntRange(higherChunk.first(), higherChunk.last())
            }
            else -> error("Invalid input: $input with letter $letter. Expected either $lowerLetter or $higherLetter")
        }
    }
    return seatRange.first
}

private fun getInputsFromFile(): List<String> =
    readFile("day5.txt")
        .split("\n")
        .map { it.trim() }
        .filter { it.isNotEmpty() }

private val testData = listOf(
    "FBFBBFFRLR" to Seat(44, 5),
    "BFFFBBFRRR" to Seat(70, 7),
    "FFFBBBFRRR" to Seat(14, 7),
    "BBFFBBFRLL" to Seat(102, 4)

)

private data class Seat(val row: Int, val column: Int) {
    val id: Int
        get() = (row * 8) + column

    override fun toString(): String = "Seat(row=$row, column=$column, id=$id)"


}
