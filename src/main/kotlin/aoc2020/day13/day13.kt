package aoc2020.day13

import aoc2020.readFile
import aoc2020.splitOnLineBreaks

fun main() {
    testData.parseInput().let { (timestamp, busses) ->
        part1(timestamp, busses)
    }

    readFile("day13.txt").parseInput().let { (timestamp, busses) ->
        part1(timestamp, busses)
    }
}

private fun part1(timestamp: Int, busses: List<Bus>) {
    var nextBus: Bus? = null
    var currentTime = timestamp - 1

    while (nextBus == null) {
        currentTime++
        nextBus = busses.find { currentTime % it.id == 0 }
    }
    val result = (currentTime - timestamp) * nextBus.id
    println("Next Bus: $nextBus || Current Time: $currentTime  || Waiting Time: ${currentTime - timestamp}")
    println("Result : $result")
}

fun String.parseInput(): Pair<Int, List<Bus>> {
    val (timestamp, inputs) = this.splitOnLineBreaks()
    return Pair(
        timestamp.trim().toInt(),
        inputs.toBus()
    )
}

private fun String.toBus(): List<Bus> =
    this.split(",")
        .filter { it.isNotEmpty() }
        .mapNotNull {
            if (it == "x") null
            else Bus(it.toInt())
        }

data class Bus(val id: Int)

val testData = """
    939
    7,13,x,x,59,x,31,19
""".trimIndent()
