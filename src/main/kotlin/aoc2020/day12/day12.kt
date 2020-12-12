package aoc2020.day12

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
    private var direction: Direction = Direction.E,
) {
    init {
        logNewPosition()
    }

    fun navigate(command: Command) {
        changeDirection(command)
        moveBoat(command)
        logNewPosition()
    }

    fun calculateManhattanDistance(): Int =
        (position[Direction.N]!! - position[Direction.S]!!).absoluteValue +
            (position[Direction.E]!! - position[Direction.W]!!).absoluteValue

    private fun moveBoat(command: Command) {
        val direction = when (command.input) {
            "N", "S", "W", "E" -> Direction.valueOf(command.input)
            "L", "R" -> return
            else -> this.direction
        }
        position.merge(direction, command.distance) { x, y -> x + y }
    }

    private fun changeDirection(command: Command) {
        this.direction = when (command.input) {
            "L" -> this.direction.left(command.distance)
            "R" -> this.direction.right(command.distance)
            else -> this.direction
        }
    }

    private fun logNewPosition() {
        println("Direction $direction | position: $position")
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
        private val traversal = mutableListOf<Direction>().apply {
            add(E)
            add(S)
            add(W)
            add(N)
        }
    }
}
typealias Position = MutableMap<Direction, Int>

fun initialPosition() = mutableMapOf<Direction, Int>().apply {
    Direction.values().forEach { put(it, 0) }
}

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
