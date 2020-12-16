package aoc2020.day16

import aoc2020.readFile
import aoc2020.splitOnBlankLines
import aoc2020.splitOnLineBreaks

fun main() {
    testData.toTicket().run {
        findAllFullyInvalidItemsInNearbyTickets().run {
            println("Count: ${this.size} || Invalids: $this")
            println("Sum of invalids: ${this.sum()}")
        }
    }
    readFile("day16.txt").toTicket().run {
        findAllFullyInvalidItemsInNearbyTickets().run {
            println("Count: ${this.size} || Invalids: $this")
            println("Sum of invalids: ${this.sum()}")
        }
    }

    testData2.toTicket().cloneTicketWithoutFullyInvalidNearbyTickets().run {
        val potentialIndices = mapConditionToPotentialIndices()
        mapConditionToOnlyPossibleIndex(potentialIndices).also { println(it) }
    }

    readFile("day16.txt").toTicket().cloneTicketWithoutFullyInvalidNearbyTickets().run {
        val potentialIndices = mapConditionToPotentialIndices()
        val cleanedUpIndices = mapConditionToOnlyPossibleIndex(potentialIndices).also { println(it) }
        val results = cleanedUpIndices
            .filter { it.key.startsWith("departure") }
            .also { println(it) }
            .map { (_, index) ->
                ticketParameters[index].toLong()
            }
        println("Results = $results")
        println("Multiplied results = ${results.reduce { acc, i -> acc * i }}")
    }
}

data class Ticket(
    val conditions: Map<String, List<IntRange>>,
    val ticketParameters: List<Int>,
    val nearbyTicketParameters: List<List<Int>>,
) {

    private val flatConditions by lazy { this.nearbyTicketParameters.flatten() }

    fun findAllFullyInvalidItemsInNearbyTickets() =
        flatConditions.filter { !isInAnyValidCondition(it) }

    private fun isInAnyValidCondition(input: Int): Boolean {
        for (range in conditions.values.flatten()) {
            if (range.contains(input)) return true
        }
        return false
    }

    fun cloneTicketWithoutFullyInvalidNearbyTickets(): Ticket {
        val cleanNearbyTickets = nearbyTicketParameters.filter { list ->
            list.forEach {
                if (!isInAnyValidCondition(it)) {
                    return@filter false
                }
            }
            return@filter true
        }

        return this.copy(nearbyTicketParameters = cleanNearbyTickets)
    }

    // Map always the only possible index for each validator
    fun mapConditionToOnlyPossibleIndex(input: Map<String, MutableList<Int>>): Map<String, Int> {
        val result = mutableMapOf<String, Int>()
        val mutableInput = input.toMutableMap()

        while (mutableInput.isNotEmpty()) {
            val (key, list) = mutableInput.entries.find { it.value.size == 1 } ?: error("No single index entry found")

            val value = list.first()
            result[key] = value

            mutableInput.remove(key)
            mutableInput.values.forEach { it.remove(value) }
        }

        return result
    }

    // Each validation rule can have more than one possible row
    fun mapConditionToPotentialIndices(): Map<String, MutableList<Int>> {
        val result = mutableMapOf<String, MutableList<Int>>()

        conditions.forEach { (key, ranges) ->
            // Match every condition to every index in our range of nearby tickets and pick whichever is 100% valid
            var index = 0
            itemLoop@ while (index <= nearbyTicketParameters[0].lastIndex) {
                var isValid = true
                tickets@ for (nearbyTicket in nearbyTicketParameters) {
                    val item = nearbyTicket[index]
                    isValid = validate(item, ranges)
                    if (!isValid) break@tickets
                }
                if (isValid) {
                    result.putIfAbsent(key, mutableListOf())
                    result[key]?.add(index) ?: error("should never occur")
                }
                index++
            }
        }

        return result
    }

    private fun validate(value: Int, ranges: List<IntRange>): Boolean =
        ranges.any { it.contains(value) }
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

val testData2 = """
    class: 0-1 or 4-19
    row: 0-5 or 8-19
    seat: 0-13 or 16-19

    your ticket:
    11,12,13

    nearby tickets:
    3,9,18
    15,1,5
    5,14,9
    
""".trimIndent()
