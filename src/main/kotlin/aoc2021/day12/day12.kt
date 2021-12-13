package aoc2021.day12

import aoc2021.readFile
import aoc2021.splitOnLineBreaks
import java.util.*

fun main() {
    val file = readFile("day12.txt")
    calculatePart1(testData.parse()).run { check(this == 10) }
    calculatePart1(testDataLarger.parse()).run { check(this == 19) }
    calculatePart1(testDataXXL.parse()).run { check(this == 226) }
    calculatePart1(file.parse()).run { println("Part1 $this") }

    calculatePart2(testData.parse()).run { check(this == 36) }
    calculatePart2(testDataLarger.parse()).run { check(this == 103) }
    calculatePart2(testDataXXL.parse()).run { check(this == 3509) }
    calculatePart2(file.parse()).run { println("Part2 $this") }
}

private fun String.parse(): Graph<Cave> {
    val caveGraph = Graph<Cave>()

    this.splitOnLineBreaks()
        .map { line -> line.split("-") }
        .forEach { (root, dest) -> caveGraph.addEdge(Cave(root), Cave(dest)) }

    return caveGraph
}

private fun calculatePart1(caves: Graph<Cave>): Int {
    println("$caves")
    val startingPoint = caves.adjacencyMap.keys.first { it.value == "start" }
    val traversal = Stack<Cave>()
    val results = mutableListOf<List<Cave>>()

    traverse(caves = caves, cave = startingPoint, traversal = traversal, results = results)

    // How many paths through this cave system are there that visit small caves at most once?
    return results.size
}

private fun calculatePart2(caves: Graph<Cave>): Int {
    println("$caves")
    val startingPoint = caves.adjacencyMap.keys.first { it.value == "start" }
    val traversal = Stack<Cave>()
    val results = mutableListOf<List<Cave>>()

    traversePart2(caves = caves, cave = startingPoint, traversal = traversal, results = results)

    results.forEach { println(it) }
    return results.size
}

private fun traverse(caves: Graph<Cave>, cave: Cave, traversal: Stack<Cave>, results: MutableList<List<Cave>>) {
    traversal.push(cave)

    if (cave == Cave("end")) {
//        println("Finished: $traversal")
        results.add(traversal.toList())
        return
    }

    caves.adjacencyMap[cave]?.filterNot {
        it.type == CaveType.Small && traversal.contains(it)
    }?.forEach { connection ->
        traverse(caves = caves, cave = connection, traversal = traversal, results = results)
        traversal.pop()
    }
}

private fun traversePart2(caves: Graph<Cave>, cave: Cave, traversal: Stack<Cave>, results: MutableList<List<Cave>>) {
    // ignore returns to start
    if (cave.value == "start" && traversal.contains(cave)) return
    traversal.push(cave)

    if (cave == Cave("end")) {
//        println("Finished: $traversal")
        results.add(traversal.toList())
        return
    }

    val hasDoubleVisitToSmall = traversal.groupingBy { it }
        .eachCount()
        .any { (key, count) -> key.type == CaveType.Small && count >= 2 }

    caves.adjacencyMap[cave]
        ?.filterNot { it.value == "start" }
        ?.filterNot { if (hasDoubleVisitToSmall) it.type == CaveType.Small && traversal.contains(it) else false }
        ?.forEach { connection ->
            traversePart2(caves = caves, cave = connection, traversal = traversal, results = results)
            traversal.pop()
        }
}

private data class Cave(val value: String) {
    val type: CaveType = if (this.value.first().isUpperCase()) CaveType.Big else CaveType.Small
    override fun toString(): String = value
}

private enum class CaveType { Big, Small }

private val testData = """
    start-A
    start-b
    A-c
    A-b
    b-d
    A-end
    b-end
""".trimIndent()

private val testDataLarger = """
    dc-end
    HN-start
    start-kj
    dc-start
    dc-HN
    LN-dc
    HN-end
    kj-sa
    kj-HN
    kj-dc
""".trimIndent()

private val testDataXXL = """
    fs-end
    he-DX
    fs-he
    start-DX
    pj-DX
    end-zg
    zg-sl
    zg-pj
    pj-he
    RW-he
    fs-DX
    pj-RW
    zg-RW
    start-pj
    he-WI
    zg-he
    pj-fs
    start-RW
""".trimIndent()

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
