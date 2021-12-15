package aoc2021

import kotlin.system.measureTimeMillis

internal fun readFile(path: String): String = {}.javaClass.getResource(path)?.readText() ?: error("File not found")
internal fun String.splitOnLineBreaks(): Sequence<String> = this.splitToSequence("\n")
internal fun String.splitOnBlankLines(): Sequence<String> =
    this.splitToSequence(Regex("\\n[\\n]+", setOf(RegexOption.MULTILINE)))

internal inline fun withExecutionTime(block: () -> Unit) =
    measureTimeMillis(block).run { println("Executed in ${this}ms") }

internal fun prettyPrint(rules: List<List<Any>>) {
    println("$$$$$$$$$$$$$$$$$$$$$$$$")
    rules.map { it.reduce { acc, s -> "$acc$s" } }.forEach { println(it) }
    println("$$$$$$$$$$$$$$$$$$$$$$$$")
}


fun kingsMoveOf(x: Int, y: Int) = buildList {
    add(x - 1 to y)
    add(x + 1 to y)
    add(x to y - 1)
    add(x to y + 1)
}

fun kingsMoveOf(xRange: IntProgression, yRange: IntProgression) = xRange.zip(yRange)

fun cartesianOf(xRange: IntProgression, yRange: IntProgression): List<Pair<Int, Int>> = buildList {
    xRange.forEach { x ->
        yRange.forEach { y ->
            add(x to y)
        }
    }
}