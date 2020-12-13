package aoc2020.day13

import aoc2020.readFile
import aoc2020.splitOnLineBreaks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.exitProcess
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

val startTime = System.currentTimeMillis()

@ExperimentalTime
fun main() {
    testData.parseInput().let { (timestamp, busses) ->
        part1(timestamp, busses)
        println("Running test data")
        part2(busses)

    }

    readFile("day13.txt").parseInput().let { (timestamp, busses) ->
        part1(timestamp, busses)
        println("Running input data")
        part2(busses, 100_000_000_000_000L)
    }
}

@ExperimentalTime
private fun part2(busses: List<Bus>, startValue: Long = 0L): Unit =
    runBlocking(Dispatchers.IO) {
        val start = if(startValue == 0L) busses[0].id else getStartIteration(startValue, busses[0].id)
        for (iteration in (start..Long.MAX_VALUE) step busses[0].id) {
            launch {
                if (isValidSequence(busses, iteration)) {
                    println("Found ya: ${iteration + busses.last().offset}")

                    val duration = (System.currentTimeMillis() - startTime).toDuration(DurationUnit.MILLISECONDS)
                    println("Took me from startup: ${duration.inSeconds}")
                    exitProcess(0)
                }
            }
        }

        error("No iteration found")
    }

fun getStartIteration(startValue: Long, id: Long): Long {
    for(item in startValue..Long.MAX_VALUE) {
        if(item % id == 0L)
            return item
    }
    error("Not possible to start?")
}

suspend fun isValidSequence(busses: List<Bus>, iteration: Long): Boolean {
    busses.forEach { bus ->
        if ((iteration + bus.offset) % bus.id != 0L) return false
    }
    return true
}

private fun part1(timestamp: Long, busses: List<Bus>) {
    var nextBus: Bus? = null
    var currentTime = timestamp - 1L

    while (nextBus == null) {
        currentTime++
        nextBus = busses.find { currentTime % it.id == 0L }
    }
    val result = (currentTime - timestamp) * nextBus.id
    println("Next Bus: $nextBus || Current Time: $currentTime  || Waiting Time: ${currentTime - timestamp}")
    println("Result : $result")
}

fun String.parseInput(): Pair<Long, List<Bus>> {
    val (timestamp, inputs) = this.splitOnLineBreaks()
    return Pair(
        timestamp.trim().toLong(),
        inputs.toBus()
    )
}

private fun String.toBus(): List<Bus> =
    this.split(",")
        .filter { it.isNotEmpty() }
        .mapIndexedNotNull { index, item ->
            if (item == "x") null
            else Bus(item.toLong(), index.toLong())
        }

data class Bus(val id: Long, val offset: Long)

val testData = """
    939
    7,13,x,x,59,x,31,19
""".trimIndent()
