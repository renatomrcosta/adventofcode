package aoc2021.day15

import aoc2021.*

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

private val fullTestDataPT2 = """
    11637517422274862853338597396444961841755517295286
    13813736722492484783351359589446246169155735727126
    21365113283247622439435873354154698446526571955763
    36949315694715142671582625378269373648937148475914
    74634171118574528222968563933317967414442817852555
    13191281372421239248353234135946434524615754563572
    13599124212461123532357223464346833457545794456865
    31254216394236532741534764385264587549637569865174
    12931385212314249632342535174345364628545647573965
    23119445813422155692453326671356443778246755488935
    22748628533385973964449618417555172952866628316397
    24924847833513595894462461691557357271266846838237
    32476224394358733541546984465265719557637682166874
    47151426715826253782693736489371484759148259586125
    85745282229685639333179674144428178525553928963666
    24212392483532341359464345246157545635726865674683
    24611235323572234643468334575457944568656815567976
    42365327415347643852645875496375698651748671976285
    23142496323425351743453646285456475739656758684176
    34221556924533266713564437782467554889357866599146
    33859739644496184175551729528666283163977739427418
    35135958944624616915573572712668468382377957949348
    43587335415469844652657195576376821668748793277985
    58262537826937364893714847591482595861259361697236
    96856393331796741444281785255539289636664139174777
    35323413594643452461575456357268656746837976785794
    35722346434683345754579445686568155679767926678187
    53476438526458754963756986517486719762859782187396
    34253517434536462854564757396567586841767869795287
    45332667135644377824675548893578665991468977611257
    44961841755517295286662831639777394274188841538529
    46246169155735727126684683823779579493488168151459
    54698446526571955763768216687487932779859814388196
    69373648937148475914825958612593616972361472718347
    17967414442817852555392896366641391747775241285888
    46434524615754563572686567468379767857948187896815
    46833457545794456865681556797679266781878137789298
    64587549637569865174867197628597821873961893298417
    45364628545647573965675868417678697952878971816398
    56443778246755488935786659914689776112579188722368
    55172952866628316397773942741888415385299952649631
    57357271266846838237795794934881681514599279262561
    65719557637682166874879327798598143881961925499217
    71484759148259586125936169723614727183472583829458
    28178525553928963666413917477752412858886352396999
    57545635726865674683797678579481878968159298917926
    57944568656815567976792667818781377892989248891319
    75698651748671976285978218739618932984172914319528
    56475739656758684176786979528789718163989182927419
    67554889357866599146897761125791887223681299833479
""".trimIndent()


fun main() {
    val file = readFile("day15.txt")
    calculate(testData.parse()).run { check(this == 40) { "Value was $this" } }
    calculate(file.parse()).run { println("Part1: $this") }

    calculate(fullTestDataPT2.parse()).run { check(this == 315) }

    withExecutionTime {
        calculate(testData.parsePart2()).run {
            check(this == 315) { "$this" }
            println("heyyo")
        }
    }
    withExecutionTime {
        calculate(file.parsePart2()).run { println("Part2: $this") }
    }
}

private fun calculate(input: List<List<Int>>): Int {
    // build graph
    val map = buildMap(input = input, adjacencyFunction = ::kingsMoveOf)
    val start = Coordinate(0, 0)
    val end = Coordinate(input.lastIndex, input.first().lastIndex)

    val tree = dijkstra(Graph(weights = map), start, end)

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

private fun String.parsePart2(): List<List<Int>> {
    val originalInput = this.splitOnLineBreaks()
        .map { it.map { it.digitToInt() } }
        .toList()

    val height = originalInput.size
    val width = originalInput.first().size

    val resultHeight = height * 5
    val resultWidth = width * 5
    val resultInput = MutableList(resultHeight) { MutableList(resultWidth) { 0 } }

    // Maaaan, fuck this code
    (0 until height).forEach { i ->
        (0 until width).forEach { j ->
            val valueToBeDuped = originalInput[i][j]
            resultInput[i][j] = valueToBeDuped
            (1..4).forEach { leap ->
                val duped = valueToBeDuped.rollingSum(leap)
                resultInput[i + (height * leap)][j] = duped
                resultInput[i][j + (width * leap)] = duped
            }
        }
    }
    (height until resultHeight).forEach { i ->
        (0 until width).forEach { j ->
            val valueToBeDuped = resultInput[i][j]
            resultInput[i][j] = valueToBeDuped
            (1..4).forEach { leap ->
                val duped = valueToBeDuped.rollingSum(leap)
                resultInput[i][j + (width * leap)] = duped
            }
        }
    }
//    prettyPrint(resultInput)
    return resultInput
}

private fun Int.rollingSum(target: Int): Int {
    val sum = this + target
    return if (sum > 9) sum - 9 else sum
}

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

private fun <T> dijkstra(graph: Graph<T>, start: T, end: T): Map<T, T?> {
    val unvisited = graph.vertices.toMutableSet()

    val delta = graph.vertices.associateWith { Int.MAX_VALUE }.toMutableMap().apply {
        this[start] = 0
    }

    val previous: MutableMap<T, T?> = graph.vertices.associateWith { null }.toMutableMap()
    var current = start

    while (true) {
        graph.edges.getValue(current).asSequence().filter { it in unvisited }.forEach { neighbor ->
            val newPath = delta.getValue(current) + graph.weights.getValue(Pair(current, neighbor))

            if (newPath < delta.getValue(neighbor)) {
                delta[neighbor] = newPath
                previous[neighbor] = current
            }
        }
        unvisited.remove(current)
        if(current == end) break

        current = delta.asSequence().filter { it.key in unvisited }.minByOrNull { it.value }?.key ?: error("unable to proceed")
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
