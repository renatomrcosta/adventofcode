package aoc2019

import aoc2019.PathDirection.*
import kotlin.math.absoluteValue

typealias Coordinate = Pair<Int, Int>

// Controls the operation of the Paths (Direction + Action)
sealed class PathDirection(val value: Direction) {
    object Up : PathDirection(Direction.Up) {
        fun operate(coordinate: Coordinate): Coordinate =
            coordinate.copy(second = coordinate.second + 1)
    }

    object Down : PathDirection(Direction.Down) {
        fun operate(coordinate: Coordinate): Coordinate =
            coordinate.copy(second = coordinate.second - 1)
    }

    object Left : PathDirection(Direction.Left) {
        fun operate(coordinate: Coordinate): Coordinate =
            coordinate.copy(first = coordinate.first - 1)
    }

    object Right : PathDirection(Direction.Right) {
        fun operate(coordinate: Coordinate): Coordinate =
            coordinate.copy(first = coordinate.first + 1)
    }

    enum class Direction(val value: String) {
        Up("U"),
        Down("D"),
        Left("L"),
        Right("R")
    }

    companion object {
        fun ofString(str: String): PathDirection {
            val direction = Direction.values()
                .find { it.value.toUpperCase() == str.toUpperCase() } ?: error("Invalid Direction")
            return when (direction) {
                Direction.Up -> Up
                Direction.Down -> Down
                Direction.Left -> Left
                Direction.Right -> Right
            }
        }
    }
}

// Describes a specific step / Path (like R88)
data class Path(val direction: PathDirection, val stepAmount: Int)

// Panel grid that will contain state. Theoretically can have more than 2 wires
class Grid(
    private val paths: List<List<Path>>
) {
    private val state: GridState by lazy {
        initState()
    }
    val manhattanDistance: Int by lazy {
        doTraversal()
        processManhattanDistance()
    }

    private fun initState(): GridState = GridState(
        wires = mutableListOf<Wire>().apply {
            paths.forEachIndexed { index, _ ->
                add(Wire(id = index))
            }
        }.toList()
    )

    // For each wire, traverse the wire's path to fill up the state.
    private fun doTraversal() {
        paths.forEachIndexed { index, pathList ->
            state.traverseWire(wireId = index, wirePathList = pathList)
        }
    }

    //Takes the intersection of the wires and checks the closest collision
    private fun processManhattanDistance(): Int {
        val intersections = intersectCoordinates()
        return intersections.map { it.calculateManhattanDistances() }.minOrNull()
            ?: error("no values found for manhattan distance")
    }

    private fun intersectCoordinates(): Set<Coordinate> {
        val wire1 = state.wires[0] // TODO IF MORE WIRES ARE CONSIDERED, HAVE TO CHANGE THIS
        val wire2 = state.wires[1]
        val intersections =
            wire1.wireCoordinates.intersect(wire2.wireCoordinates)
                .filterNot { it.first == 0 && it.second == 0 }
                .toSet() // Position 0,0 does not count

        if (intersections.isEmpty()) error("there were no intersections in the given coordinates")

        return intersections
    }
}

data class GridState(
    val wires: List<Wire> = emptyList()
) {
    fun getWireById(id: Int) = wires.find { it.id == id } ?: error("wire with id $id not found")

    // Each wire will have to traverse its path from origin
    fun traverseWire(wireId: Int, wirePathList: List<Path>) {
        val wire = getWireById(wireId)
        var currentCoordinate = INITIAL_COORDINATE
        for (path in wirePathList) {
            currentCoordinate = wire.traversePath(currentCoordinate, path)
        }
        //Debug information
        // println(wire.wireCoordinates)
    }

    companion object {
        private val INITIAL_COORDINATE = Coordinate(0, 0)
    }
}

data class Wire(val id: Int) {
    var wireCoordinates: MutableList<Coordinate> = mutableListOf()
    fun traversePath(startingPoint: Coordinate, path: Path): Coordinate {
        // Write each individual coordinate that the wire passes through
        // L -> Minus on the X axis
        // R -> Plus on the X axis
        // U -> Plus on the Y axis
        // D -> Minus on the Y Axis
        // TODO - probably still shit
        var currentCoordinate = startingPoint
        when (path.direction) {
            Up -> {
                for (i in 0 until path.stepAmount) {
                    wireCoordinates.add(currentCoordinate)
                    currentCoordinate = Up.operate(currentCoordinate)
                }
            }
            Down -> {
                for (i in 0 until path.stepAmount) {
                    wireCoordinates.add(currentCoordinate)
                    currentCoordinate = Down.operate(currentCoordinate)
                }
            }
            Left -> {
                for (i in 0 until path.stepAmount) {
                    wireCoordinates.add(currentCoordinate)
                    currentCoordinate = Left.operate(currentCoordinate)
                }
            }
            Right -> {
                for (i in 0 until path.stepAmount) {
                    wireCoordinates.add(currentCoordinate)
                    currentCoordinate = Right.operate(currentCoordinate)
                }
            }
        }
        return currentCoordinate
    }
}

// Parses the Input text
fun parseInput(inputString: String) = inputString.split("\n")
    .filter { it.isNotEmpty() }
    .map { it.split(",") }
    .map { it.map { it.toPath() } }

// Accepts a format like R8, U888
private fun String.toPath(): Path {
    val direction = this.substring(0, 1)
    val stepAmount = this.substring(1, this.length).toInt()

    return Path(
        direction = PathDirection.ofString(direction),
        stepAmount = stepAmount
    )
}

private fun Coordinate.calculateManhattanDistances(): Int =
    this.first.absoluteValue + this.second.absoluteValue

fun main() {
    // Test Input 1
    val tst1 = listOf(
        listOf("R8", "U5", "L5", "D3").map { it.toPath() },
        listOf("U7", "R6", "D4", "L4").map { it.toPath() }
    )

    val tst2 = listOf(
        listOf("R75", "D30", "R83", "U83", "L12", "D49", "R71", "U7", "L72").map { it.toPath() },
        listOf("U62", "R66", "U55", "R34", "D71", "R55", "D58", "R83").map { it.toPath() },
    )

    val tst3 = listOf(
        listOf("R98", "U47", "R26", "D63", "R33", "U87", "L62", "D20", "R33", "U53", "R51").map { it.toPath() },
        listOf("U98", "R91", "D20", "R16", "D67", "R40", "U7", "R15", "U6", "R7").map { it.toPath() },
    )

    val resultInput = parseInput(
        readFile("day3.txt")
    )

    Grid(tst1).run {
        println("TST1 = ${this.manhattanDistance}")
    }
    Grid(tst2).run {
        println("TST2 = ${this.manhattanDistance}")
    }
    Grid(tst3).run {
        println("TST3 = ${this.manhattanDistance}")
    }
    Grid(resultInput).run {
        println("Result ${this.manhattanDistance}")
    }
}
