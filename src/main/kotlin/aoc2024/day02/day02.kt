package aoc2024.day02

import aoc2024.extractNumbers
import aoc2024.readFile
import aoc2024.splitOnLineBreaks
import kotlin.math.abs

private val testData = """
    7 6 4 2 1
    1 2 7 8 9
    9 7 6 2 1
    1 3 2 4 5
    8 6 4 4 1
    1 3 6 7 9
""".trimIndent()

fun main() {
    val input = readFile("day02.txt")
    part1(testData).run { require(this == 2L) }
    part1(input).run { println("Part1: $this") }

//        part2(testData).run{ require(this == TEST_EXPECTED_RESULT_PART2) }
//        part2(input).run { println("Part2: $this") }

}

fun part1(input: String): Long {
    val reports = input.parse()

    val validReports = reports.filter { report ->
        (increasing(report)|| decreasing(report) ) && withinRange(report)
    }
    println(validReports)

    return validReports.size.toLong()
}

fun interface Predicate {
    operator fun invoke(line: List<Long>): Boolean
}

val decreasing = Predicate { line ->
    line.forEachIndexed { index, item ->
        if (index != line.lastIndex && item <= line[index + 1]) {
            return@Predicate false
        }
    }
    true
}
val increasing = Predicate { line ->
    line.forEachIndexed { index, item ->
        if (index != line.lastIndex && item >= line[index + 1]) {
            return@Predicate false
        }
    }
    true
}
val withinRange = Predicate { line ->
    line.forEachIndexed { index, item ->
        if(index != line.lastIndex) {
            val diff = abs(item - line[index + 1])
            if (diff < 1 || diff > 3) {
                return@Predicate false
            }
        }
    }
    true
}

fun String.parse(): List<List<Long>> =
    this.splitOnLineBreaks()
        .map { it.extractNumbers() }
        .toList()