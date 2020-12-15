package aoc2020.day15

import aoc2020.withExecutionTime

fun main() {
    withExecutionTime {
        // Game(testData).play(2020).also { println(it.last()) }
        // Game(input).play(2020).also { println(it.last()) }

        // Game(testData).play(30_000_000).also { println(it.last()) }
        Game(input).play(30_000_000).also { println(it.last()) }
    }
}

data class Game(val input: List<Int>) {
    private val numbers = mutableListOf(-10, *input.toTypedArray()) // I initialize with an offset to ignore "Turn 0"

    private val lastIndexMap = numbers
        .dropLast(1)
        .mapIndexed { index, item -> item to index }.toMap()
        .toMutableMap() // Index all but the first item processed }

    fun play(turns: Int): List<Int> {
        repeat((input.size until turns).count()) {
            val currentItem = numbers.last()
            val itemIndex = lastIndexMap.getOrDefault(currentItem, -1)
            lastIndexMap[currentItem] = numbers.lastIndex

            if (itemIndex >= 0) {
                numbers.add(numbers.lastIndex - itemIndex)
            } else {
                numbers.add(0)
            }
        }

        return numbers
    }
}

val testData = listOf(0, 3, 6)
val input = listOf(9, 3, 1, 0, 8, 4)
