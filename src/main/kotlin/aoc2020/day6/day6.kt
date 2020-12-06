package aoc2020.day6

import aoc2020.readFile
import aoc2020.splitNewLines

fun main() {
    println("Part 1 - Test Data")
    parseInput(testData).run {
        val answerSum = sumOf { it.size }
        println("Group Reply Count = $answerSum")
        assert(answerSum == 11)
    }

    println("Part 1 - Input file")
    parseInput(readFile("day6.txt")).run {
        println("Group Reply Count = " + sumOf { it.size })
    }

    println("Part2 - Test Data")
    parseInputPart2(testData).run {
        println("Test part 2 - $this")
        val answerSum = sumOf { it.size }
        println("Part2 test - Group Reply Count = $answerSum")
        assert(answerSum == 6)
    }

    println("Part 2 - Input file")
    parseInputPart2(readFile("day6.txt")).run {
        println("Part2 answer - Group Reply Count = " + sumBy { it.size })
    }
}

fun parseInput(input: String): List<Set<Char>> =
    input
        .splitNewLines()
        .filter { it.isNotEmpty() }
        .map { it.replace("\n", "") }
        .map { it.toSet() }

fun parseInputPart2(input: String) = input.splitNewLines()
    .filter { it.isNotEmpty() }
    .map { it.trim() }
    .map { it.split("\n") }
    .map { it.toSet() }
    .map { set ->
        set.map { it.toSet() }
            .reduce { acc, s ->
                acc intersect s
            }
    }.filter { it.isNotEmpty() }

val testData = """
    abc

    a
    b
    c

    ab
    ac

    a
    a
    a
    a

    b
""".trimIndent()

val testData2 = """
    abc

    a
    b
    c

    ab
    ac

    a
    ac
    a
    ad

    b
    
    abc
    c
    ac
    
    xa
    ya
    za
    xxa
    bbbab
    ca
""".trimIndent()
