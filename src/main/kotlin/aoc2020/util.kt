package aoc2020

import kotlin.system.measureTimeMillis

internal fun readFile(path: String): String = {}.javaClass.getResource(path).readText()
internal fun withExecutionTime(block: () -> Unit) = measureTimeMillis(block).run { println("Executed in ${this}ms") }
internal fun String.splitOnBlankLines(): List<String> = this.split(Regex("\\n[\\n]+", setOf(RegexOption.MULTILINE)))
internal fun String.splitOnLineBreaks(): List<String> = this.split("\n")
