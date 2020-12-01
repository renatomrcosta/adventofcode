package aoc2020

import kotlin.system.measureTimeMillis

fun readFile(path: String): String = {}.javaClass.getResource(path).readText()
fun withExecutionTime(block: () -> Unit) = measureTimeMillis(block).run { println("Executed in ${this}ms") }
