package aoc2021.day15

import aoc2021.cartesianOf
import aoc2021.kingsMoveOf
import aoc2021.readFile
import aoc2021.splitOnLineBreaks

private val testData = """
    1163751742
    1381373672
    2136511328
    3694931569
    7463417111
    1319128137
    1359912421
    3125421639
    1293138521
    2311944581
""".trimIndent()

fun main() {
    val file = readFile("day15.txt")
    calculatePart1(testData.parse()).run { check(this == 40) { "Value was $this" } }
    calculatePart1(file.parse()).run { println("Part1: $this") }
}

private fun calculatePart1(input: List<List<Int>>): Int {
    // build graph
    val map = buildMap(input = input, adjacencyFunction = ::kingsMoveOf)
    val start = Coordinate(0, 0)
    val end = Coordinate(input.lastIndex, input.first().lastIndex)

    val tree = dijkstra(Graph(weights = map), start)

    return shortestPath(shortestPathTree = tree, start = start, end = end)
        .drop(1)
        .sumOf { (x, y) -> input[x][y] }
}

private fun buildMap(
    input: List<List<Int>>,
    adjacencyFunction: (Int, Int) -> List<Pair<Int, Int>>
): Map<Pair<Coordinate, Coordinate>, Int> = buildMap {
    cartesianOf((0..input.lastIndex), (0..input.first().lastIndex))
        .map { (x, y) -> Coordinate(x, y) to adjacencyFunction(x, y) }
        .forEach { (coordinate, adjacentCoordinates) ->
            adjacentCoordinates.mapNotNull { (aX, aY) ->
                input.getOrNull(aX)?.getOrNull(aY)?.let { value ->
                    Coordinate(aX, aY) to value
                }
            }.forEach { (adjacentCoordinate, adjacentValue) ->
                put(coordinate to adjacentCoordinate, adjacentValue)
            }
        }
}

private data class Coordinate(val x: Int, val y: Int)

private fun String.parse(): List<List<Int>> = this.splitOnLineBreaks()
    .map { it.map { it.digitToInt() } }
    .toList()

/*** Graph Implementation and Dijkstra algorithm shameleslly stolen from
 * https://www.atomiccommits.io/dijkstras-algorithm-in-kotlin
 * https://github.com/alexhwoods/alexhwoods.com/blob/master/kotlin-algorithms/src/main/kotlin/com/alexhwoods/graphs/datastructures/Graph.kt
 * Big thanks, my dude!
 */

private data class Graph<T>(
    val vertices: Set<T>,
    val edges: Map<T, Set<T>>,
    val weights: Map<Pair<T, T>, Int>
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

private fun <T> dijkstra(graph: Graph<T>, start: T): Map<T, T?> {
    val S: MutableSet<T> = mutableSetOf() // a subset of vertices, for which we know the true distance

    val delta = graph.vertices.map { it to Int.MAX_VALUE }.toMap().toMutableMap()
    delta[start] = 0

    val previous: MutableMap<T, T?> = graph.vertices.map { it to null }.toMap().toMutableMap()

    while (S != graph.vertices) {
        val v: T = delta
            .filter { !S.contains(it.key) }
            .minByOrNull { it.value }!!
            .key

        graph.edges.getValue(v).minus(S).forEach { neighbor ->
            val newPath = delta.getValue(v) + graph.weights.getValue(Pair(v, neighbor))

            if (newPath < delta.getValue(neighbor)) {
                delta[neighbor] = newPath
                previous[neighbor] = v
            }
        }

        S.add(v)
    }

    return previous.toMap()
}

private fun <T> shortestPath(shortestPathTree: Map<T, T?>, start: T, end: T): List<T> {
    fun pathTo(start: T, end: T): List<T> {
        if (shortestPathTree[end] == null) return listOf(end)
        return listOf(pathTo(start, shortestPathTree[end]!!), listOf(end)).flatten()
    }

    return pathTo(start, end)
}
