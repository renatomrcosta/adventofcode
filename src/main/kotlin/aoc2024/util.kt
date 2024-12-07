package aoc2024

import kotlin.system.measureTimeMillis

private val NUM_REGEX = Regex("[0-9]+")
fun readFile(path: String): String = {}.javaClass.getResource(path)?.readText() ?: error("File not found")
fun String.splitOnLineBreaks(): Sequence<String> = this.splitToSequence("\n")
fun String.splitOnBlankLines(): Sequence<String> =
    this.splitToSequence(Regex("\\n[\\n]+", setOf(RegexOption.MULTILINE)))

inline fun withExecutionTime(block: () -> Unit) =
    measureTimeMillis(block).run { println("Executed in ${this}ms") }

fun Any?.println() = println(this)

fun String.extractNumbers(): List<Long> {
    return NUM_REGEX.findAll(this).flatMap { it.groupValues }.map { it.toLong() }.toList()
}