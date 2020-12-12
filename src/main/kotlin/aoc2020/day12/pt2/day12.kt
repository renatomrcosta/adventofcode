package aoc2020.day12.pt2

import aoc2020.readFile
import aoc2020.splitOnLineBreaks
import kotlin.math.absoluteValue

fun main() {
    println("Part 1 - Test Data")
    testData.parseInput().run {
        val ship = Ship()
        this.forEach { ship.navigate(it) }
        println("Ship finished at $ship")
        println("Manhattan at ${ship.calculateManhattanDistance()}")
    }

    readFile("day12.txt").parseInput().run {
        val ship = Ship()
        this.forEach { ship.navigate(it) }
        println("Ship finished at $ship")
        println("Manhattan at ${ship.calculateManhattanDistance()}")
    }
}

data class Ship(
    private var position: Position = initialPosition(),
    private var waypoint: Position = initialWaypointPosition(),
) {
    init {
        logNewPosition()
    }

    fun navigate(command: Command) {
        when (command.input) {
            "F" -> moveBoat(command)
            "N", "S", "E", "W" -> moveWaypoint(command)
            "L", "R" -> rotateWaypoint(command)
        }
        logNewPosition()
    }

    private fun rotateWaypoint(command: Command) {
        val currentWaypoints = waypoint.filter { it.value != 0 }
        waypoint.clear()
        when (command.input) {
            "L" -> {
                currentWaypoints.forEach { (key, value) ->
                    val destinationKey = key.left(command.distance)
                    waypoint[destinationKey] = value
                }
            }
            "R" -> {
                currentWaypoints.forEach { (key, value) ->
                    val destinationKey = key.right(command.distance)
                    waypoint[destinationKey] = value
                }
            }
            else -> error("invalid turn")
        }
    }

    private fun moveWaypoint(command: Command) {
        val direction = valueOfOrNull<Direction>(command.input) ?: error("Invalid Direction")
        waypoint.merge(direction, command.distance) { x, y -> x + y }
    }

    fun calculateManhattanDistance(): Int =
        (position[Direction.N]!! - position[Direction.S]!!).absoluteValue +
            (position[Direction.E]!! - position[Direction.W]!!).absoluteValue

    private fun moveBoat(command: Command) {
        waypoint.filter { it.value != 0 }.forEach { (key, value) ->
            position.merge(key, value * command.distance) { x, y -> x + y }
        }
    }

    private fun logNewPosition() {
        println("Boat position: $position | Waypoint: $waypoint")
    }
}

data class Command(val input: String, val distance: Int)
enum class Direction {
    N, S, E, W;

    fun right(distance: Int): Direction {
        val turns = distance / 90

        val sequence = generateSequence { traversal }.flatten()
        return sequence.dropWhile { it != this }.take(turns + 1).last()
    }

    fun left(distance: Int): Direction {
        val turns = distance / 90

        val sequence = generateSequence { traversal.reversed() }.flatten()
        return sequence.dropWhile { it != this }.take(turns + 1).last()
    }

    companion object {
        private val traversal = listOf(E, S, W, N)
    }
}
typealias Position = MutableMap<Direction, Int>

fun initialPosition() = mutableMapOf<Direction, Int>().apply {
    Direction.values().forEach { put(it, 0) }
}

fun initialWaypointPosition() = mutableMapOf(
    Direction.E to 10,
    Direction.N to 1,
)

inline fun <reified T : Enum<T>> valueOfOrNull(input: String): T? =
    enumValues<T>().find { it.name == input }

fun String.parseInput() = this.splitOnLineBreaks()
    .filter { it.isNotEmpty() }
    .map { it.trim().toCommand() }

private fun String.toCommand() =
    Command(
        input = this.substring(0, 1),
        distance = this.substring(1).toInt()
    )

val testData = """
    F10
    N3
    F7
    R90
    F11
    
""".trimIndent()
