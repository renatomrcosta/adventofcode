package aoc2020.day15

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        // Game(testData).play(2020).also { println(it.last()) }
        // Game(input).play(2020).also { println(it.last()) }
        //
        // Game(testData).play(30_000_000).also { println(it.last()) }
        Game(input).play(30_000_000).also { println(it.last()) }
    }
}

data class Game(val input: List<Int>) {
    private var inputTurnCount = input.size
    private var numbers = mutableListOf(*input.reversed().toTypedArray(), -10)

    private val flow = flow {
        while (true) {
            val currentItem = numbers.first()
            val itemIndex = numbers.drop(1).indexOf(currentItem)

            if (itemIndex >= 0) {
                emit(itemIndex + 1)
            } else {
                emit(0)
            }
        }
    }

    suspend fun play(turns: Int): List<Int> {
        flow
            .take(turns - inputTurnCount)
            .collect {
                numbers.add(0, it)
                if (numbers.lastIndex % 100_000 == 0) println("${numbers.lastIndex} turns played")
            }

        return numbers.reversed()
    }
}

val testData = listOf(0, 3, 6)
val input = listOf(9, 3, 1, 0, 8, 4)
