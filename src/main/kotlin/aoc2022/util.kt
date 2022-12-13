package aoc2022

import kotlin.system.measureTimeMillis

internal fun readFile(path: String): String = {}.javaClass.getResource(path)?.readText() ?: error("File not found")
internal fun String.splitOnLineBreaks(): Sequence<String> = this.splitToSequence("\n")
internal fun String.splitOnBlankLines(): Sequence<String> =
    this.splitToSequence(Regex("\\n[\\n]+", setOf(RegexOption.MULTILINE)))

internal inline fun withExecutionTime(block: () -> Unit) =
    measureTimeMillis(block).run { println("Executed in ${this}ms") }

internal fun List<List<Any?>>.prettyPrint() {
    println("$$$$$$$$$$$$$$$$$$$$$$$$")
    this.map { it.reduce { acc, s -> "$acc$s" } }.forEach { println(it) }
    println("$$$$$$$$$$$$$$$$$$$$$$$$")
}

fun <T> List<List<T>>.transpose(): List<List<T?>> {
    val rowSize = this.size
    val colSize = this.first().size
    val transposed = MutableList(colSize) { MutableList<T?>(rowSize) { null } }

    for (i in 0 until rowSize) {
        for (j in 0 until colSize) {
            transposed[j][i] = this[i][j]
        }
    }
    return transposed
}

fun Any?.println() = println(this)

fun kingsMoveOf(pair: Pair<Int, Int>) = buildList {
    val (x, y) = pair
    add(x - 1 to y)
    add(x + 1 to y)
    add(x to y - 1)
    add(x to y + 1)
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

/*** Graph Implementation stolen from
 * https://www.atomiccommits.io/dijkstras-algorithm-in-kotlin
 * https://github.com/alexhwoods/alexhwoods.com/blob/master/kotlin-algorithms/src/main/kotlin/com/alexhwoods/graphs/datastructures/Graph.kt
 * Big thanks, my dude!
 */

data class Graph<T> private constructor(
    val vertices: Set<T>,
    val edges: Map<T, Set<T>>,
    val weights: Map<Pair<T, T>, Int>,
) {
    constructor(weights: Map<Pair<T, T>, Int>) : this(
        vertices = weights.keys.toList().getUniqueValuesFromPairs(),
        edges = weights.keys
            .groupBy { it.first }
            .mapValues { it.value.getUniqueValuesFromPairs { x -> x !== it.key } }
            .withDefault { emptySet() },
        weights = weights
    )

    companion object {
        private fun <T> List<Pair<T, T>>.getUniqueValuesFromPairs(): Set<T> = this
            .map { (a, b) -> listOf(a, b) }
            .flatten()
            .toSet()

        private fun <T> List<Pair<T, T>>.getUniqueValuesFromPairs(predicate: (T) -> Boolean): Set<T> = this
            .map { (a, b) -> listOf(a, b) }
            .flatten()
            .filter(predicate)
            .toSet()
    }
}
