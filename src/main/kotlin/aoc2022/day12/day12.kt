package aoc2022.day12

import aoc2022.readFile
import aoc2022.splitOnLineBreaks
import java.util.PriorityQueue

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

private fun part1(input: String): Int {
    val (start, end, heights) = input.parse()

    val graph = Graph(
        vertices = heights.keys.toSet(),
        edges = heights.keys.associateWith { position ->
            position.directional().filter { it in heights.keys }
                .filter { heights[it]!! <= heights[position]!! + 1 }.toSet()
        },
    )

    println("START $start | END $end")
    println("Edges ${graph.edges}")

    return traverse(start, end, graph)
}


private fun traverse(start: Coordinate, end: Coordinate, graph: Graph<Coordinate>): Int {
    val prioQueue = PriorityQueue<Pair<Coordinate, Int>>(compareBy { it.second })
    val traversed = mutableListOf<Coordinate>()

    prioQueue.add(start to 0)

    while (prioQueue.isNotEmpty()) {
        val (currentNode, cost) = prioQueue.poll()
        if (currentNode !in traversed) {
            traversed.add(currentNode)
            val edges = graph.edges[currentNode].orEmpty()
            if (end in edges) {
                println("FOUND END $traversed")
                return cost + 1
            }
            prioQueue.addAll(edges.map { it to cost + 1 })
        }
    }
    error("no ending found!")
}

private data class Graph<T>(
    val vertices: Set<T>,
    val edges: Map<T, Set<T>>,
)

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
    'E' -> heightMap.size
    else -> heightMap.indexOf(this)
}

private val heightMap = ('a'..'z').toList()
private typealias Coordinate = Pair<Int, Int>
