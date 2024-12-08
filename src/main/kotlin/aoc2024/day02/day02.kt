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

    part2(testData).run { require(this == 4L) }
    part2(input).run { println("Part2: $this") }

}

fun part1(input: String): Long {
    val reports = input.parse()

    val validReports = reports.filter { report ->
        (increasing(report) || decreasing(report)) && withinRange(report)
    }
//    println(validReports)

    return validReports.size.toLong()
}

fun part2(input: String): Long {
    val reports = input.parse()

    val validReports = reports.mapNotNull { report ->
        val sortFn = when {
            report[0] < report[1] || report[1] < report[2] || report[2] < report[3] -> increasing2
            report[0] > report[1] || report[1] > report[2] || report[2] > report[3] -> decreasing2
            else -> error("wtf $report")
        }
        val sortResult = sortFn(report)
        val withinRangeResult = withinRange2(report)

        if (sortResult == Predicate2.Result.Success && withinRangeResult == Predicate2.Result.Success) {
            return@mapNotNull report
        }

        val newline = when {
            (sortResult is Predicate2.Result.Failure) -> {
                report.toMutableList().apply { removeAt(sortResult.badIndex) }
            }
            withinRangeResult is Predicate2.Result.Failure -> {
                report.toMutableList().apply { removeAt(withinRangeResult.badIndex) }
            }


            else -> error("invalid result somehow")
        }


        val sortFn2 = when {
            newline[0] < newline[1] || newline[1] < newline[2]  || newline[2] < newline[3]-> increasing2
            newline[0] > newline[1] || newline[1] > newline[2]  || newline[2] > newline[3]-> decreasing2
            else -> error("wtf $newline")
        }
        val sortResult2 = sortFn2(newline)
        val withinRangeResult2 = withinRange2(newline)

        if (sortResult2 == Predicate2.Result.Success && withinRangeResult2 == Predicate2.Result.Success) {
            return@mapNotNull report
        }
        null
    }

    println(validReports)

    return validReports.size.toLong()
}

fun interface Predicate {
    operator fun invoke(line: List<Long>): Boolean
}

fun interface Predicate2 {
    operator fun invoke(line: List<Long>): Result
    sealed interface Result {
        data object Success : Result
        data class Failure(val badIndex: Int) : Result

    }
}

val decreasing = Predicate { line ->
    line.forEachIndexed { index, item ->
        if (index != line.lastIndex && item < line[index + 1]) {
            return@Predicate false
        }
    }
    true
}
val increasing = Predicate { line ->
    line.forEachIndexed { index, item ->
        if (index != line.lastIndex && item > line[index + 1]) {
            return@Predicate false
        }
    }
    true
}
val withinRange = Predicate { line ->
    line.forEachIndexed { index, item ->
        if (index != line.lastIndex) {
            val diff = abs(item - line[index + 1])
            if (diff < 1 || diff > 3) {
                return@Predicate false
            }
        }
    }
    true
}

val decreasing2 = Predicate2 { line ->
    line.forEachIndexed { index, item ->
        if (index != line.lastIndex && item <= line[index + 1]) {
            return@Predicate2 Predicate2.Result.Failure(badIndex = index)
        }
    }
    Predicate2.Result.Success
}
val increasing2 = Predicate2 { line ->
    line.forEachIndexed { index, item ->
        if (index != line.lastIndex && item >= line[index + 1]) {
            return@Predicate2 Predicate2.Result.Failure(badIndex = index)
        }
    }
    Predicate2.Result.Success
}
val withinRange2 = Predicate2 { line ->
    line.forEachIndexed { index, item ->
        if (index != line.lastIndex) {
            val diff = abs(item - line[index + 1])
            if (diff < 1 || diff > 3) {
                return@Predicate2 Predicate2.Result.Failure(badIndex = index)
            }
        }
    }
    Predicate2.Result.Success
}

fun String.parse(): List<List<Long>> =
    this.splitOnLineBreaks()
        .map { it.extractNumbers() }
        .toList()