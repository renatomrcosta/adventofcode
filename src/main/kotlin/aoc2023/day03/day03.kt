package aoc2023.day03

import aoc2023.splitOnLineBreaks

private val testInput = """
    467..114..
    ...*......
    ..35..633.
    ......#...
    617*......
    .....+.58.
    ..592.....
    ......755.
    ...${'$'}.*....
    .664.598..
""".trimIndent()

fun main() {
    part1(testInput).run { require(this == 4361L) }
//    part1(readFile("day2.txt")).run { println("Part 1: $this") }

//    part2(testInput).run { require(this == 2286L) }
//    part2(readFile("day2.txt")).run { println("Part 2: $this") }
}

private fun part1(input: String): Long {
    val digitRegex = Regex("\\D+")
    // build a map of numbers vs indices
    input.splitOnLineBreaks().mapIndexed { row, line ->
        digitRegex.split(line)
            .filter { it.isNotEmpty() }
            // TODO why are empty lists going here?
            .map { number ->
                val index = line.indexOf(number)
                (row to number) to (index..<index + number.length)
            }
    }.toList()
        .also { println(it) }
    return 0L
}
