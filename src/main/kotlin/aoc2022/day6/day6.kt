package aoc2022.day6

import aoc2022.readFile

private val tests = listOf(
    Triple("mjqjpqmgbljsphdztnvjfqwrcgsmlb", 7, 19),
    Triple("bvwbjplbgvbhsrlpgdmjqwftvncz", 5, 23),
    Triple("nppdvjthqldpwncqszvftbrmjlhg", 6, 23),
    Triple("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", 10, 29),
    Triple("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", 11, 26),
)

fun main() {
    val input = readFile("day6.txt")
    tests.forEach { (input, expected, _) -> require(part1(input) == expected) }
    part1(input).run { println("Part1: $this") }

    tests.forEach { (input, _, expected) -> require(part2(input) == expected) }
    part2(input).run { println("Part2: $this") }
}

private fun part1(input: String): Int = input.findIndexOfFirstCompleteUniqueGroup(groupSize = 4)

private fun part2(input: String): Int = input.findIndexOfFirstCompleteUniqueGroup(groupSize = 14)

private fun String.findIndexOfFirstCompleteUniqueGroup(groupSize: Int): Int {
    return groupSize + this.windowed(size = groupSize, step = 1)
        .indexOfFirst { group -> group.toSet().size == groupSize }
}

