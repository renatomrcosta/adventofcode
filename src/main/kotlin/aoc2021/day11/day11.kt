package aoc2021.day11

import aoc2021.prettyPrint
import aoc2021.readFile
import aoc2021.splitOnLineBreaks

private val testData = """
    5483143223
    2745854711
    5264556173
    6141336146
    6357385478
    4167524645
    2176841721
    6882881134
    4846848554
    5283751526
""".trimIndent()

private val realData = """
    6227618536
    2368158384
    5385414113
    4556757523
    6746486724
    4881323884
    4648263744
    4871332872
    4724128228
    4316512167
""".trimIndent()

fun main() {
    calculatePart1(testData.parse()).run { check(this == 1656) }
    calculatePart1(realData.parse()).run { println("Part1: $this") }
}

private fun calculatePart1(input: List<List<Octopus>>): Int {
    repeat(100) {
        doStep(input)
//        println("Step $it")
//        prettyPrint(input)
    }
    return input.flatten().sumOf { it.shineCount }
}

private fun doStep(input: List<List<Octopus>>) {
    // Add 1 to the energy of all octopi
    // When all are energized, take 9s, energize their neighbors, then rinse
    // At the end, >= 9 goes to zero

    input.forEachIndexed { i, row ->
        row.forEachIndexed { j, octopus ->
            octopus.step()
        }
    }

    input.flatten().filter { it.energyLevel > 9 }.forEach { energizeAdjacent(input, it) }

    input.forEachIndexed { i, row ->
        row.forEachIndexed { j, octopus ->
            if (octopus.energyLevel > 9) octopus.reset()
        }
    }

}

private fun energizeAdjacent(input: List<List<Octopus>>, octopus: Octopus) {
    val shiners = mutableListOf<Octopus>()
    val adjacents = input.adjacent(octopus).filterNot { it.hasShined }

    adjacents.forEach { adjacent ->
        adjacent.step()
        if (adjacent.energyLevel == 10) {
            shiners.add(adjacent)
        }
    }
    shiners.forEach { shiner -> energizeAdjacent(input, shiner) }
}

private fun String.parse() = this.splitOnLineBreaks()
    .map { it.map { it.digitToInt() } }
    .mapIndexed { i, row -> row.mapIndexed { j, num -> Octopus(energyLevel = num, position = Position(i, j)) } }
    .toList()

private data class Position(val x: Int, val y: Int)

private fun List<List<Octopus>>.adjacent(octopus: Octopus): List<Octopus> {
    val cartesian = with(octopus.position) {
        val xRange = (x - 1..x + 1)
        val yRange = (y - 1..y + 1)

        cartesianOf(xRange, yRange).filterNot { it == x to y }
    }
    return cartesian.mapNotNull { (x, y) -> this@adjacent.getOrNull(x)?.getOrNull(y) }
}

private data class Octopus(
    var energyLevel: Int,
    val position: Position,
    var hasShined: Boolean = false,
) {
    var shineCount: Int = 0
        private set

    fun step() {
        energyLevel++
        if (energyLevel == 10) {
            hasShined = true
            shineCount++
        }
    }
    fun reset() {
        hasShined = false
        energyLevel = 0
    }

    override fun toString(): String {
        return energyLevel.toString()
    }
}

private fun cartesianOf(xRange: IntProgression, yRange: IntProgression): List<Pair<Int, Int>> = buildList {
    xRange.forEach { x ->
        yRange.forEach { y ->
            add(x to y)
        }
    }
}