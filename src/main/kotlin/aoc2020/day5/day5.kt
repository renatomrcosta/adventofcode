package aoc2020.day5

import aoc2020.readFile

fun main() {
    println("Part1 - Test Data assertion")
    testData.forEach { (input, result) ->
        assert(input.toSeat() == result)
    }

    println("Part 1 - Real Data")
    val inputList = getInputsFromFile()

    val seats = inputList
        .map { it.toSeat() }
        .sortedBy { it.id }
        .onEach { println(it) }

    // Part 1 - Get Biggest ID
    println("PART 1 - biggest seat ID: ${seats.last()}")

    println("PART 2 - Figure Out free seat")
    seats
        .groupBy { it.row }
        .filter { it.value.count() in 4..7 } // find the column that will contain less items per row, but not totally empty
        .entries.forEach { entryMap ->
            val emptyColumnsInRow = (PLANE_COL_RANGE).mapNotNull { column ->
                if (!entryMap.value.any { it.column == column }) {
                    column
                } else null
            }
            emptyColumnsInRow.map { emptyColumnsInRow ->
                Seat(row = entryMap.key, column = emptyColumnsInRow)
            }.forEach { println(it) }
        }
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
