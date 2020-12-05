package aoc2020.day5

import aoc2020.readFile

fun main() {
    testData.forEach { (input, result) ->
        assert(input.toSeat() == result)
    }

    // println("Part 1 - Real Data")
    // val inputList = getInputsFromFile()
    //
    // // Get biggest ID
    // inputList
    //     .map { it.toSeat() }
    //     .maxByOrNull { it.id }
    //     .run { println(this) }
}

private fun String.toSeat(): Seat {
    TODO()
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

private class Seat(val row: Int, val column: Int) {
    val id: Int
        get() = (row * 8) + 5
}
