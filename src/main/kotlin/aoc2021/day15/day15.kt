package aoc2021.day15

import aoc2021.cartesianOf
import aoc2021.kingsMoveOf
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
    // For each value, create a graph with adjacent coordinates
    // Implement dijkstra
    calculatePart1(testData.parse())
}

private fun calculatePart1(input: List<List<Int>>) {
    // build graph
    val graph = buildGraph(input = input, adjacencyFunction = ::kingsMoveOf)
    val start = (0 to 0).let { (x, y) -> Pathway(coordinate = x to y, risk = input[x][y]) }
    val end =
        (input.lastIndex to input.first().lastIndex).let { (x, y) -> Pathway(coordinate = x to y, risk = input[x][y]) }

    traverseShortestPath(graph = graph, start = start, end = end)
//    println(graph)
}

private fun traverseShortestPath(
    graph: Graph<Pathway>,
    start: Pathway,
    end: Pathway,
): MutableList<Pathway> {
    /***
    Let the node at which we are starting at be called the initial node. Let the distance of node Y be the distance from the initial node to Y.Dijkstra's algorithm will initially start with infinite distances and will try to improve them step by step.
    - Mark all nodes unvisited. Create a set of all the unvisited nodes called the unvisited set.
    - Assign to every node a tentative distance value: set it to zero for our initial node and to infinity for all other nodes. The tentative distance of a node v is the length of the shortest path discovered so far between the node v and the starting node. Since initially no path is known to any other vertex than the source itself (which is a path of length zero), all other tentative distances are initially set to infinity. Set the initial node as current.[15]
    - For the current node, consider all of its unvisited neighbors and calculate their tentative distances through the current node. Compare the newly calculated tentative distance to the current assigned value and assign the smaller one. For example, if the current node A is marked with a distance of 6, and the edge connecting it with a neighbor B has length 2, then the distance to B through A will be 6 + 2 = 8. If B was previously marked with a distance greater than 8 then change it to 8. Otherwise, the current value will be kept.
    - When we are done considering all of the unvisited neighbors of the current node, mark the current node as visited and remove it from the unvisited set. A visited node will never be checked again.
    - If the destination node has been marked visited (when planning a route between two specific nodes) or if the smallest tentative distance among the nodes in the unvisited set is infinity (when planning a complete traversal; occurs when there is no connection between the initial node and remaining unvisited nodes), then stop. The algorithm has finished.
    - Otherwise, select the unvisited node that is marked with the smallest tentative distance, set it as the new current node, and go back to step 3.
    - When planning a route, it is actually not necessary to wait until the destination node is "visited" as above: the algorithm can stop once the destination node has the smallest tentative distance among all "unvisited" nodes (and thus could be selected as the next "current").
     */
    // TODO incomplete
    val notVisited = graph.adjacencyMap.keys.toMutableSet()
    val distances = graph.adjacencyMap.keys.toSet().associateWith { Int.MAX_VALUE }.toMutableMap()
        .apply { this[start] = 0 }

    val path = mutableListOf<Pathway>()
    var currentNode = start

    while (notVisited.isNotEmpty() && notVisited.contains(end)) {
        graph.adjacencyMap.getValue(currentNode).filter { it in notVisited }.onEach { adjacent ->
            val totalRisk = adjacent.risk
            if (distances.getValue(adjacent) > totalRisk) {
                distances[adjacent] = totalRisk
            }
        }.forEach { adjacent ->
            notVisited.remove(adjacent)
        }
        if (!notVisited.contains(end)) {
            println("End visited")
            break
        }
        if (notVisited.minOf { it.risk } == Integer.MAX_VALUE) {
            error("No pathway to end result")
        }
        currentNode = notVisited.minByOrNull { it.risk }?.also {
            path.add(currentNode)
        } ?: error("whut?")
    }
    println("Path $path")
    return path
}

private fun buildGraph(input: List<List<Int>>, adjacencyFunction: (Int, Int) -> List<Pair<Int, Int>>) =
    Graph<Pathway>().apply {
        cartesianOf((0..input.lastIndex), (0..input.first().lastIndex))
            .map { it to adjacencyFunction(it.first, it.second) }
            .forEach { (coordinate, adjacentCoordinates) ->
                adjacentCoordinates.mapNotNull { (aX, aY) ->
                    input.getOrNull(aX)?.getOrNull(aY)?.let { value ->
                        (aX to aY) to value
                    }
                }.forEach { (adjacentCoordinate, adjacentValue) ->
                    val coordinateValue = input[coordinate.first][coordinate.second]
                    this@apply.addEdge(
                        sourceVertex = Pathway(coordinate = coordinate, risk = coordinateValue),
                        destinationVertex = Pathway(coordinate = adjacentCoordinate, risk = adjacentValue)
                    )
                }
            }
    }

private fun String.parse(): List<List<Int>> = this.splitOnLineBreaks()
    .map { it.map { it.digitToInt() } }
    .toList()

// Stolen from https://developerlife.com/2018/08/16/algorithms-in-kotlin-5/
private data class Graph<T>(val adjacencyMap: HashMap<T, HashSet<T>> = HashMap()) {
    fun addEdge(sourceVertex: T, destinationVertex: T) {
        // Add edge to source vertex / node.
        adjacencyMap
            .computeIfAbsent(sourceVertex) { HashSet() }
            .add(destinationVertex)
        // Add edge to destination vertex / node.
        adjacencyMap
            .computeIfAbsent(destinationVertex) { HashSet() }
            .add(sourceVertex)
    }

    override fun toString(): String = StringBuffer().apply {
        for (key in adjacencyMap.keys) {
            append("$key -> ")
            append(adjacencyMap[key]?.joinToString(", ", "[", "]\n"))
        }
    }.toString()
}

private data class Pathway(val coordinate: Pair<Int, Int>, val risk: Int)
