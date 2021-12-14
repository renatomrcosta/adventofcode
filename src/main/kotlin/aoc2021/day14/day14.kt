package aoc2021.day14

import aoc2021.readFile
import aoc2021.splitOnBlankLines
import aoc2021.splitOnLineBreaks
import java.util.*

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
    calculatePart1(testData.parse()).run { check(this == 1588) {"What? $this"} }
    calculatePart1(file.parse()).run { println("Part1 $this") }

}

private fun calculatePart1(input: Input): Int {
    val final = (1..10).fold(input.template) { acc: String, i: Int ->
        step(acc, input.rules)
//            .also { println("step $i $it") }
    }
//    println("Final Polymer: $final")

    val sorting = final.toList().groupingBy { it }.eachCount()
        .map { (key, count) -> key to count }
        .sortedBy { (key, count) -> count }

    return sorting.last().second - sorting.first().second
}

private fun step(inputString: String, rules: Map<String, String>): String {
    val inputAsList = inputString.toMutableList()

    val charsToInsert = inputString.windowed(2).map { window ->
        rules[window]?.first() ?: error("aaa")
    }

    return zipWithDifferentSizes(inputAsList, charsToInsert).joinToString("")
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

private fun <T> zipWithDifferentSizes(first: Iterable<T>, second: Iterable<T>) = sequence {
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