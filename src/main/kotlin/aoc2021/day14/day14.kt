package aoc2021.day14

import aoc2021.readFile
import aoc2021.splitOnBlankLines
import aoc2021.splitOnLineBreaks

private val testData = """
    NNCB

    CH -> B
    HH -> N
    CB -> H
    NH -> C
    HB -> C
    HC -> B
    HN -> C
    NN -> C
    BH -> H
    NC -> B
    NB -> B
    BN -> B
    BB -> N
    BC -> B
    CC -> N
    CN -> C
""".trimIndent()

fun main() {
    val file = readFile("day14.txt")
    calculatePart1(testData.parse()).run { check(this == 1588) { "What? $this" } }
    calculatePart1(file.parse()).run { println("Part1 $this") }

    calculatePart2(testData.parse(), 40).run { check(this == 2188189693529) { "What? $this" } }
    calculatePart2(testData.parse(), 10).run { check(this == 1588L) { "What? $this" } }

    calculatePart2(file.parse(), 10).run { check(this == 3342L) { "Result was $this" } }
    calculatePart2(file.parse(), 40).run { println("Part2 $this") }
}

private fun calculatePart1(input: Input): Int {
    val final = (1..10).fold(input.template) { acc: String, i: Int ->
        step(acc, input.rules)
    }
//    println("Final Polymer: $final")
    val sorting = final.toList().groupingBy { it }.eachCount()
        .map { (key, count) -> key to count }
        .sortedBy { (key, count) -> count }

    return sorting.last().second - sorting.first().second
}

private fun calculatePart2(input: Input, steps: Int): Long {
    val letterCounter =
        input.template.groupBy { it.toString() }.mapValues { (_, values) -> values.size.toLong() }.toMutableMap()
    val pairCounter =
        input.template.windowed(2).groupingBy { it }.eachCount().mapValues { it.value.toLong() }.toMutableMap()

    repeat(steps) {
        step2(letterCounter, pairCounter, input.rules)
    }

    println("LetterCounter: $letterCounter")
    return letterCounter.maxOf { (letter, count) -> count } - letterCounter.minOf { (letter, count) -> count }
}

private fun step(inputString: String, rules: Map<String, String>): String {
    val inputAsList = inputString.asSequence()

    val charsToInsert = inputString.windowedSequence(2).map { window ->
        rules[window]?.first() ?: error("aaa")
    }

    return zipWithDifferentSizes(inputAsList, charsToInsert).joinToString("")
}

private fun step2(
    letterCounter: MutableMap<String, Long>,
    pairCounter: MutableMap<String, Long>,
    rules: Map<String, String>
) {
    pairCounter.toList()
        .also { pairCounter.clear() }
        .forEach { (key, count) ->
            val letter = rules[key] ?: error("wah")
            val pair1 = key.first() + letter
            val pair2 = letter + key.last()
            pairCounter[pair1] = pairCounter.getOrDefault(pair1, 0L) + count
            pairCounter[pair2] = pairCounter.getOrDefault(pair2, 0L) + count
            letterCounter[letter] = letterCounter.getOrDefault(letter, 0L) + count
        }
}

private fun String.parse(): Input {
    val (template, rawRules) = this.splitOnBlankLines().toList()

    val rules = rawRules.splitOnLineBreaks()
        .map {
            val (key, transform) = it.split("->")
            key.trim() to transform.trim()
        }
        .toMap()

    return Input(
        template = template,
        rules = rules
    )
}

private fun <T> zipWithDifferentSizes(first: Sequence<T>, second: Sequence<T>) = sequence {
    val firstIterator = first.iterator()
    val secondIterator = second.iterator()
    while (firstIterator.hasNext() && secondIterator.hasNext()) {
        yield(firstIterator.next())
        yield(secondIterator.next())
    }

    yieldAll(firstIterator)
    yieldAll(secondIterator)
}.toList()

private data class Input(val template: String, val rules: Map<String, String>)