package aoc2023

import kotlin.system.measureTimeMillis

fun readFile(path: String): String = {}.javaClass.getResource(path)?.readText() ?: error("File not found")
fun String.splitOnLineBreaks(): Sequence<String> = this.splitToSequence("\n")
fun String.splitOnBlankLines(): Sequence<String> =
    this.splitToSequence(Regex("\\n[\\n]+", setOf(RegexOption.MULTILINE)))

inline fun withExecutionTime(block: () -> Unit) =
    measureTimeMillis(block).run { println("Executed in ${this}ms") }

fun Any?.println() = println(this)

fun surroundingIndices(x: Int, y: Int) = buildSet {
    add(x - 1 to y - 1)
    add(x - 1 to y)
    add(x - 1 to y + 1)
    add(x to y - 1)
//    add(x to y)
    add(x to y + 1)
    add(x + 1 to y - 1)
    add(x + 1 to y)
    add(x + 1 to y + 1)
}
