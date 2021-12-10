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

}

private fun String.parse() =
    this.splitOnLineBreaks()
        .toList()

private fun calculatePart1(input: List<String>): Long {
    return input.sumOf { it.getFirstIllegalCharPointsOrNull() ?: 0L }
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

private fun List<String>.filterCorruptedLines(): Pair<List<String>, List<String>> = this.partition {
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