package aoc2020.day15

import aoc2020.withExecutionTime
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking

fun main() {
    withExecutionTime {
        runBlocking {
            Game(testData).play(2020).also { println(it.last()) }
            Game(input).play(2020).also { println(it.last()) }

            Game(testData).play(30_000_000).also { println(it.last()) }
            Game(input).play(30_000_000).also { println(it.last()) }
        }
    }
}

data class Game(val input: List<Int>) {
    private var inputTurnCount = input.size
    private val numbers = mutableListOf(-10, *input.toTypedArray())
    private val lastIndexMap = numbers.dropLast(1).mapIndexed { index, item -> item to index }.toMap().toMutableMap()

    private val flow = flow {
        while (true) {
            val currentItem = numbers.last()
            val itemIndex = lastIndexMap.getOrDefault(currentItem, -1)
            lastIndexMap[currentItem] = numbers.lastIndex

            if (itemIndex >= 0) {
                emit(numbers.lastIndex - itemIndex)
            } else {
                emit(0)
            }
        }
    }

    suspend fun play(turns: Int): List<Int> {
        flow
            .take(turns - inputTurnCount)
            .collect {
                numbers.add(it)
                if (numbers.lastIndex % 1_000_000 == 0) println("${numbers.lastIndex} turns played")
            }
        return numbers
    }
}

val testData = listOf(0, 3, 6)
val input = listOf(9, 3, 1, 0, 8, 4)
