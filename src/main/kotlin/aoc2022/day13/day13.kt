package aoc2022.day13

import aoc2022.readFile
import aoc2022.splitOnBlankLines
import aoc2022.splitOnLineBreaks
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.fasterxml.jackson.module.kotlin.readValue

private val objectMapper = ObjectMapper().registerModule(
    kotlinModule {}
)

fun main() {
    val input = readFile("day13.txt")
    part1(testInput).run { require(this == 13) { println("p1 was $this") } }
    part1(input).run { println("Part1: $this") }
}

private fun part1(input: String): Int {
    val pairs = input.parse().toList()
    val result = pairs.mapIndexed { index, pair -> index + 1 to pair.inOrder() }.toMap()
    println(result)
    return result.filterValues { it }.keys.sum()
}

private fun Pair<Any?, Any?>.inOrder(): Boolean = let { (left, right) ->
    requireNotNull(left)
    requireNotNull(right)

    when {
        left is Int && right is Int -> {
            return left <= right
        }

        left is List<*> && right is List<*> -> {
            left.zip(right).all { it.inOrder() } && left.size <= right.size
        }

        left is Int && right is List<*> -> {
            listOf(left).zip(right).all { it.inOrder() }
        }

        right is Int && left is List<*> -> {
            left.zip(listOf(right)).all { it.inOrder() }
        }

        else -> {
            error("what the fuck just went on? $left $right")
        }
    }
}

private fun String.parse() = this.splitOnBlankLines()
    .map {
        val (left, right) = it.splitOnLineBreaks().toList()
        objectMapper.readValue<List<Any>>(left) to objectMapper.readValue<List<Any>>(right)
    }

private val testInput = """
    [1,1,3,1,1]
    [1,1,5,1,1]

    [[1],[2,3,4]]
    [[1],4]

    [9]
    [[8,7,6]]

    [[4,10],4,4]
    [[4,10],4,4,4]

    [7,7,7,7]
    [7,7,7]

    []
    [3]

    [[[]]]
    [[]]

    [1,[2,[3,[4,[5,6,7]]]],8,9]
    [1,[2,[3,[4,[5,6,0]]]],8,9]
""".trimIndent()