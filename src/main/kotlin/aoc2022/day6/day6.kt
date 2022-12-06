package aoc2022.day6

import aoc2022.readFile

private val tests = listOf(
    Triple("mjqjpqmgbljsphdztnvjfqwrcgsmlb", 7, 0),
    Triple("bvwbjplbgvbhsrlpgdmjqwftvncz", 5, 0),
    Triple("nppdvjthqldpwncqszvftbrmjlhg", 6, 0),
    Triple("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", 10, 0),
    Triple("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", 11, 0),
)

fun main() {
    val input = readFile("day6.txt")
    tests.forEach { (input, expected, _) -> require(part1(input) == expected) }
    part1(input).run { println("Part1: $this") }
}

private fun part1(input: String): Int {
    val indexOfPacketStart = input.parseInput().indexOfFirst { group ->
        group.toSet().size == 4
    }
    return indexOfPacketStart + 4
}

private fun String.parseInput() = this.windowed(size = 4, step = 1)