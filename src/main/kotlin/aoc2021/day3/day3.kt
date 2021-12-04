package aoc2021.day3

import aoc2021.readFile
import aoc2021.splitOnLineBreaks

private val rawTestInput = """
    00100
    11110
    10110
    10111
    10101
    01111
    00111
    11100
    10000
    11001
    00010
    01010
""".trimIndent()

fun main() {
    val testInput = rawTestInput.splitOnLineBreaks().map { it.trim() }
    val file = readFile("day3.txt").splitOnLineBreaks().map { it.trim() }

    println("PART1")
    check(calculatePart1(testInput) == 198)
    calculatePart1(file)

    println("----")
    println("PART2")
    check(calculatePart2(testInput) == 230)
    calculatePart2(file)
}

private fun buildLenghtwiseMap(input: Sequence<String>): Map<Int, List<String>> =
    buildMap<Int, MutableList<String>> {
        input.forEach {
            it.forEachIndexed { index, char ->
                putIfAbsent(index, mutableListOf())
                this[index]?.add(char.toString())
            }
        }
    }

private fun calculatePart1(input: Sequence<String>): Int {
    val lenghtwiseMap = buildLenghtwiseMap(input)

    var gammaString = ""
    var epsilonString = ""
    lenghtwiseMap.entries.forEach { (index, items) ->
        val zeroCount = items.count { it == "0" }
        val oneCount = items.count { it == "1" }

        if (zeroCount > oneCount) {
            gammaString += "0"
            epsilonString += "1"
        } else {
            gammaString += "1"
            epsilonString += "0"
        }
    }

    println("Strings: Gamma $gammaString | Epsilon $epsilonString")
    val result = gammaString.toInt(radix = 2) * epsilonString.toInt(radix = 2)

    println("Result: $result")
    return result
}

private fun calculatePart2(input: Sequence<String>): Int {
    val oxygen = findValueMatching(
        input = input,
        matchingChar = '1',
        opposingChar = '0',
    )

    val co2 = findValueMatching(
        input = input,
        matchingChar = '0',
        opposingChar = '1',
    )

    println("C02: $co2 | O2: $oxygen")
    val result = co2 * oxygen
    println("Result: $result")
    return result
}

fun findValueMatching(
    input: Sequence<String>,
    matchingChar: Char,
    opposingChar: Char,
): Int {
    var list = input.toMutableList()
    (1..input.count()).forEachIndexed() { index, _ ->
        val lenghtwiseList = buildLenghtwiseMap(list.asSequence())[index] ?: error("couldn't find list at index")

        val zeroCount = lenghtwiseList.count { it == "0" }
        val oneCount = lenghtwiseList.count { it == "1" }

        if (zeroCount <= oneCount) {
            list = list.filter { it[index] == matchingChar }.toMutableList()
        } else {
            list = list.filter { it[index] == opposingChar }.toMutableList()
        }

        if (list.size == 1) {
            println("Value found! ${list.first()}")
            return list.first().toInt(radix = 2).also { println("In decimal: $it") }
        }
    }
    error("no value found!")
}