package aoc2022.day12

import aoc2022.println
import aoc2022.readFile
import aoc2022.splitOnLineBreaks
import kotlin.math.abs
import kotlin.math.min

private val testInput = """
    Sabqponm
    abcryxxl
    accszExk
    acctuvwj
    abdefghi
""".trimIndent()

fun main() {
    val input = readFile("day12.txt")
    part1(testInput).run { require(this == 31) { println("result was $this") } }
    println("real input")
    part1(input).run { println("Part1: $this") }
}

var minPaths = setOf<Coordinate>()

private fun part1(input: String): Int {
    val (start, end, heights) = input.parse()

    // heights.println()

    minPaths = setOf()
    traverse(start = start, end = end, heights = heights)

    return minPaths.size
}

private fun traverse(
    start: Coordinate,
    end: Coordinate,
    heights: Map<Coordinate, Int>,
    stepLog: MutableSet<Coordinate> = mutableSetOf(),
) {
    if(minPaths.isNotEmpty() && minPaths.size <= stepLog.size) return
    if (start == end) {
        minPaths = stepLog.toSet()
        println("EITA PEGA| stepcount: ${stepLog.size} $stepLog")

        // println("Translate: ${stepLog.size} ${stepLog.map {
        //     if(it == 0 to 0 || it == end) {'S'} else heightMap[heights[it]!! -1]
        // }}")
        return
    }
    stepLog.add(start)
    heights.filterKeys { it in start.directional() && it !in stepLog }
        .filterValues { it in (heights[start]!!..heights[start]!!+1) }
        .keys.sortedByDescending { (x, y) ->
            val (ex, ey) = end
            abs(x - ex) + abs(y - ey)
        }
        .take(2)
        .forEach { next ->
            traverse(next, end, heights, stepLog.toMutableSet())
        }
}

private fun Coordinate.directional(): Set<Coordinate> = let { (x, y) ->
    setOf(
        x - 1 to y,
        x + 1 to y,
        x to y - 1,
        x to y + 1,
    )
}

private fun String.parse(): Input {
    var startCoordinate = 0 to 0
    var endCoordinate = 0 to 0
    val heightMap = this.splitOnLineBreaks()
        .flatMapIndexed { x, row ->
            row.mapIndexed() { y, cell ->
                if (cell == 'S') startCoordinate = x to y
                if (cell == 'E') endCoordinate = x to y
                (x to y) to cell.toHeight()
            }
        }.toMap()
    return Input(start = startCoordinate, end = endCoordinate, heights = heightMap)
}

private data class Input(
    val start: Coordinate,
    val end: Coordinate,
    val heights: Map<Coordinate, Int>,
)

private fun Char.toHeight(): Int = when (this) {
    'S' -> 0
    'E' -> heightMap.size + 1
    else -> heightMap.indexOf(this) + 1
}

private val heightMap = ('a'..'z').toList()
private typealias Coordinate = Pair<Int, Int>
