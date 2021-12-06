package aoc2021.day6

import aoc2021.readFile
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.transform

private val testInput = "3,4,3,1,2"

suspend fun main() {
    val file = readFile("day6.txt")
    println("part1")

    check(calculatePart1(testInput.parse()) == 5934)
    val part1 = calculatePart1(file.parse())
    println("Part1: $part1")
}

private suspend fun calculatePart1(input: List<Lanternfish>): Int {
    var flow = input.asFlow()

    repeat(80) {
        flow = flow.transform {
            val spawn = it.spawn()
            emit(it)
            spawn?.let { emit(spawn) }
        }
    }
    return flow.count().also { println("Count=$it") }
}


private fun String.parse() = this
    .splitToSequence(",")
    .map { it.toInt() }
    .map { Lanternfish(timer = it) }
    .toList()

private class Lanternfish(var timer: Int) {
    fun spawn(): Lanternfish? {
        return if (timer == 0) {
            this.timer = 6
            Lanternfish(timer = 8)
        } else {
            this.timer -= 1
            null
        }
    }
}