package aoc2020.day15

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking

fun main() {
    part1()
}

fun part1() = runBlocking {
    Game(testData).play(2020).also { println(it.last()) }
    Game(input).play(2020).also { println(it.last()) }
}

data class Game(val input: List<Int>) {
    private var inputTurnCount = input.size
    private var numbers = mutableListOf(-10, *input.toTypedArray())

    private val flow = flow {
        while (true) {
            val currentItem = numbers.last()
            val itemIndex = numbers.dropLast(1).lastIndexOf(currentItem)

            if (itemIndex >= 0) {
                emit(numbers.lastIndex - itemIndex)
            } else {
                emit(0)
            }
        }
    }

    suspend fun play(turns: Int): List<Int> {
        flow.take(turns - inputTurnCount).collect { numbers.add(it) }
        return numbers
    }
}

val testData = listOf(0, 3, 6)
val input = listOf(9, 3, 1, 0, 8, 4)
