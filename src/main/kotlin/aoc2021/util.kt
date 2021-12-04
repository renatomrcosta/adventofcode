package aoc2021

import kotlin.system.measureTimeMillis

internal fun readFile(path: String): String = {}.javaClass.getResource(path)?.readText() ?: error("File not found")
internal fun String.splitOnLineBreaks(): Sequence<String> = this.splitToSequence("\n")
internal fun String.splitOnBlankLines(): Sequence<String> = this.splitToSequence(Regex("\\n[\\n]+", setOf(RegexOption.MULTILINE)))
internal fun withExecutionTime(block: () -> Unit) = measureTimeMillis(block).run { println("Executed in ${this}ms") }
