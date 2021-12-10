package aoc2021.day10

import aoc2021.readFile
import aoc2021.splitOnLineBreaks
import java.util.*

private val testData = """
    [({(<(())[]>[[{[]{<()<>>
    [(()[<>])]({[<{<<[]>>(
    {([(<{}[<>[]}>{[]{[(<()>
    (((({<>}<{<{<>}{[]{[]{}
    [[<[([]))<([[{}[[()]]]
    [{[{({}]{}}([{[{{{}}([]
    {<[[]]>}<{[{[{[]{()[[[]
    [<(<(<(<{}))><([]([]()
    <{([([[(<>()){}]>(<<{{
    <{([{{}}[<[[[<>{}]]]>[]]
""".trimIndent()

fun main() {
    val file = readFile("day10.txt")
    println("Part1")
    check(calculatePart1(input = testData.parse()) == 26397L)
    calculatePart1(file.parse()).run { println("Part1: $this") }

    println("Part2")
    check(calculatePart2(testData.parse()) == 288957L)
    calculatePart2(file.parse()).run { println("Part2: $this") }

}

private fun String.parse() =
    this.splitOnLineBreaks()
        .toList()

private fun calculatePart1(input: List<String>): Long {
    return input.sumOf { it.getFirstIllegalCharPointsOrNull() ?: 0L }
}

private fun calculatePart2(input: List<String>): Long {
    val (incompleteLines, corruptedLines) = input.partitionOnCorruptedLines()
    val sortedResults = incompleteLines
        .map { it.getCompletionString().toTotalPoints() }
        .sorted()
    return sortedResults[sortedResults.size / 2]
}

private fun String.toTotalPoints(): Long =
    this.map { closingToPointMap[it] ?: error("noopoo") }.reduce { acc, score ->
        (acc * 5L) + score
    }

private fun String.getCompletionString(): String {
    val accumulatorStack = Stack<Char>()

    this.forEach { char ->
        if (char.isOpeningClosure()) accumulatorStack.push(char)
        else {
            if (char.matchesOpeningClosure(accumulatorStack.peek())) {
                accumulatorStack.pop()
            }
        }
    }
    return accumulatorStack.joinToString("").map { chunkClosures[it] ?: ' ' }.reversed().joinToString("")
}

private fun String.getFirstIllegalCharPointsOrNull(): Long? {
    val accumulatorStack = Stack<Char>()

    this.forEach { char ->
        if (char.isOpeningClosure()) accumulatorStack.push(char)
        else {
            val pop = accumulatorStack.pop() ?: ' '
            if (!char.matchesOpeningClosure(pop)) {
                return when (char) {
                    ')' -> 3L
                    ']' -> 57L
                    '}' -> 1197L
                    '>' -> 25137L
                    else -> error("invalid closure")
                }
            }
        }
    }
    return null
}

private fun List<String>.partitionOnCorruptedLines(): Pair<List<String>, List<String>> = this.partition {
    it.getFirstIllegalCharPointsOrNull()?.let { false } ?: true
}

private fun Char.matchesOpeningClosure(acc: Char): Boolean =
    this == chunkClosures[acc]

private fun Char.isOpeningClosure() = chunkClosures.containsKey(this)

private val chunkClosures = mapOf(
    '(' to ')',
    '[' to ']',
    '{' to '}',
    '<' to '>',
)
private val closingToPointMap = mapOf(
    ')' to 1L,
    ']' to 2L,
    '}' to 3L,
    '>' to 4L,
)