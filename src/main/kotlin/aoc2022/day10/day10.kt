package aoc2022.day10

import aoc2022.prettyPrint
import aoc2022.readFile
import aoc2022.splitOnLineBreaks

fun main() {
    val testData = readFile("day10-test.txt")
    val input = readFile("day10.txt")
    part1(testData).run { require(this == 13140L) { println("Part1 error: $this") } }
    part1(input).run { println("Part1: $this") }

    part2(testData)
    part2(input)
}

private fun part1(input: String): Long {
    val cpu = CPU().apply { operate(input) }
    val signalIndexes = 20L..220L step 40

    return signalIndexes.fold(0L) { acc, index ->
        val xAtLog = cpu.stateLog[index.toInt()] ?: error("what? $index")
        (xAtLog * index) + acc
    }
}

private fun part2(input: String) =
    CPU().apply { operate(input) }
        .pixelLog.values.chunked(40)
        .prettyPrint()

private data class CPU(
    var x: Long = 1L,
    var cycle: Int = 1,
) {
    val stateLog: MutableMap<Int, Long> = mutableMapOf(cycle to x)
    val pixelLog: MutableMap<Int, String> = mutableMapOf()

    fun operate(input: String) = input.parse().forEach(::operate)

    private fun operate(operation: Operation) = when (operation) {
        is Operation.ADDX, is Operation.NOOP -> {
            (1..operation.cycleLength).forEach {
                stateLog[cycle] = x
                drawPixel()

                if (it == operation.cycleLength) {
                    x += operation.accumulator
                }

                cycle++
            }
        }
    }

    private fun drawPixel() {
        val pixelWidth = (cycle - 1..cycle + 1)
        val row = (cycle / 40)
        pixelLog[cycle] = if ((row * 40) + x + 1 in pixelWidth) "#" else "."
    }
}

private sealed class Operation(
    val name: String,
    open val accumulator: Long,
    val cycleLength: Int,
) {
    object NOOP : Operation(name = "noop", accumulator = 0, cycleLength = 1)
    data class ADDX(override val accumulator: Long) :
        Operation(name = "addx", accumulator = accumulator, cycleLength = 2)
}

private fun String.parse() = this.splitOnLineBreaks()
    .map {
        val split = it.split(" ")
        val cmdName = split.first()
        val cmdX = (split.getOrNull(1))?.toLong()
        when (cmdName) {
            "addx" -> Operation.ADDX(accumulator = cmdX ?: 0L)
            "noop" -> Operation.NOOP
            else -> error("invalid command")
        }
    }

private val smallTest = """
    noop
    addx 3
    addx -5
""".trimIndent()
