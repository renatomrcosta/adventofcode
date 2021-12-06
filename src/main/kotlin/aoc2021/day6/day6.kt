package aoc2021.day6

import aoc2021.readFile
import aoc2021.withExecutionTime
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.transform
import java.util.*

private val testInput = "3,4,3,1,2"

suspend fun main() {
    val file = readFile("day6.txt")
    println("part1")

    check(calculatePart1(testInput.parse()) == 5934)
    val part1 = calculatePart1(file.parse())
    println("Part1: $part1")

    println("part2")
    withExecutionTime {
        check(calculatePart2(testInput.parseInt(), 80) == 5934L)
        check(calculatePart2(testInput.parseInt(), 256) == 26984457539)
    }
    withExecutionTime {
        val part2 = calculatePart2(file.parseInt(), 256)
        println("Part2: $part2")
    }
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

private fun calculatePart2(input: List<Int>, days: Int): Long {
    val buckets = MutableList(9) { 0L }

    input.groupingBy { it }.eachCount().forEach { (index, count) ->
        buckets[index] = count.toLong()
    }
    repeat(days) {
        Collections.rotate(buckets, -1)
        // every item on the bucket #8 is a new spawn, and the same amount should go to bucket 6 (parent reset)
        buckets[6] += buckets[8]
    }
    return buckets.sum()
}


private fun String.parse() = this
    .splitToSequence(",")
    .map { it.toInt() }
    .map { Lanternfish(timer = it) }
    .toList()

private fun String.parseInt() = this
    .splitToSequence(",")
    .map { it.toInt() }
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