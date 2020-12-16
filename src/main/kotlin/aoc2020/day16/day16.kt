package aoc2020.day16

import aoc2020.readFile
import aoc2020.splitOnBlankLines
import aoc2020.splitOnLineBreaks

fun main() {
    testData.toTicket().run {
        findAllFullyInvalidItemsInNearbyTickets().run {
            println("Invalids: $this")
            println("Sum of invalids: ${this.sum()}")
        }
    }
    readFile("day16.txt").toTicket().run {
        findAllFullyInvalidItemsInNearbyTickets().run {
            println("Invalids: $this")
            println("Sum of invalids: ${this.sum()}")
        }
    }
}

data class Ticket(
    val conditions: Map<String, List<IntRange>>,
    val ticketParameters: List<Int>,
    val nearbyTicketParameters: List<List<Int>>,
) {
    fun findAllFullyInvalidItemsInNearbyTickets() =
        this.nearbyTicketParameters.flatten().filter { !isInAnyValidCondition(it) }


    private fun isInAnyValidCondition(input: Int): Boolean {
        for (range in conditions.values.flatten()) {
            if (range.contains(input)) return true
        }
        return false
    }
}

fun String.toTicket(): Ticket {
    val blocks = this.splitOnBlankLines().filter { it.isNotEmpty() }
    return Ticket(
        conditions = buildConditionMap(blocks[0]),
        ticketParameters = buildTicketParameters(blocks[1]),
        nearbyTicketParameters = buildNearbyParameters(blocks[2])
    )
}

fun buildNearbyParameters(input: String): List<List<Int>> {
    val row = input.splitOnLineBreaks().drop(1).filter { it.isNotEmpty() }
    return row.map { parseParameterListRow(it) }
}

fun buildTicketParameters(input: String): List<Int> {
    val row = input.splitOnLineBreaks().last { it.isNotEmpty() }.trim()
    return parseParameterListRow(row)
}

fun buildConditionMap(input: String): Map<String, List<IntRange>> =
    input.splitOnLineBreaks().filter { it.isNotEmpty() }.map {
        val split = it.split(":")
        val key = split.first().trim()
        val ranges = split.last().split(" or ").map { strRanges ->
            val operators = strRanges.split("-")
            operators.first().trim().toInt()..operators.last().trim().toInt()
        }
        key to ranges
    }.toMap()

fun parseParameterListRow(row: String): List<Int> {
    return row.split(",").map { it.trim().toInt() }
}

val testData = """
    class: 1-3 or 5-7
    row: 6-11 or 33-44
    seat: 13-40 or 45-50

    your ticket:
    7,1,14

    nearby tickets:
    7,3,47
    40,4,50
    55,2,20
    38,6,12
""".trimIndent()
