package aoc2020.day10

import aoc2020.readFile
import aoc2020.splitOnLineBreaks

fun main() {
    part1()
}

private fun part1() {
    println("Part 1 - Test Data")
    testData.parseInput().run {
        val resultList = findJoltDifferences(this.toMutableList())
        println(resultList.groupingBy { it }.eachCount())
    }
    readFile("day10-test.txt").parseInput().run {
        val resultList = findJoltDifferences(this.toMutableList())
        println(resultList.groupingBy { it }.eachCount())
    }
    println("Part1 - Real Data")
    readFile("day10.txt").parseInput().run {
        val resultList = findJoltDifferences(this.toMutableList())
        val result = resultList.groupingBy { it }.eachCount()
        println(result)
    }
}

fun findJoltDifferences(input: MutableList<Int>): List<Int> {
    val result = mutableListOf<Int>()
    var currentJolts = 0

    while (input.isNotEmpty()) {
        //gets all possible matches for this jolts, and gets the one with the least diff.
        val selectedJolt =
            input.filter { it.canConnect(currentJolts) }.minOrNull()
                ?: error("cannot connect any adapters") // Can probably be reduced to a simple iterator
        val diff = selectedJolt - currentJolts
        currentJolts += diff
        result.add(diff) // adds to mutable result list
        input.remove(selectedJolt) // removes from search
        println("difference between $currentJolts and $selectedJolt = $diff")
        println("current jolts = $currentJolts")
    }
    // Device addition
    result.add(3)
    currentJolts + 3
    println("Max jolt size = $currentJolts")
    return result
}

fun Int.canConnect(jolts: Int): Boolean = (jolts..jolts + 3).contains(this)

fun String.parseInput(): List<Int> =
    this.splitOnLineBreaks()
        .filter { it.isNotEmpty() }
        .map { it.toInt() }
        .sorted()

val testData = """
    16
    10
    15
    5
    1
    11
    7
    19
    6
    12
    4
    
""".trimIndent()
