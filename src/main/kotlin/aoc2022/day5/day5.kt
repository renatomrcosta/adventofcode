package aoc2022.day5

import aoc2022.readFile
import aoc2022.splitOnBlankLines
import aoc2022.splitOnLineBreaks

private val testInput = """
    [D]    
[N] [C]    
[Z] [M] [P]
1   2   3

move 1 from 2 to 1
move 3 from 1 to 3
move 2 from 2 to 1
move 1 from 1 to 2
""".trimMargin()

fun testInputStacks() = listOf(
    ArrayDeque(listOf('Z', 'N')),
    ArrayDeque(listOf('M', 'C', 'D')),
    ArrayDeque(listOf('P')),
)

fun inputStacks() = listOf(
    ArrayDeque(listOf('Q', 'F', 'M', 'R', 'L', 'W', 'C', 'V')),
    ArrayDeque(listOf('D', 'Q', 'L')),
    ArrayDeque(listOf('P', 'S', 'R', 'G', 'W', 'C', 'N', 'B')),
    ArrayDeque(listOf('L', 'C', 'D', 'H', 'B', 'Q', 'G')),
    ArrayDeque(listOf('V', 'G', 'L', 'F', 'Z', 'S')),
    ArrayDeque(listOf('D', 'G', 'N', 'P')),
    ArrayDeque(listOf('D', 'Z', 'P', 'V', 'F', 'C', 'W')),
    ArrayDeque(listOf('C', 'P', 'D', 'M', 'S')),
    ArrayDeque(listOf('Z', 'N', 'W', 'T', 'V', 'M', 'P', 'C')),
)

fun main() {
    val input = readFile("day5.txt")
    part1(testInput, testInputStacks()).run { require(this == "CMZ") { println("THIS $this")} }
    part1(input, inputStacks()).run { println("Part1: $this") }
    //
    part2(testInput, testInputStacks()).run { require(this == "MCD") { println("THIS $this") } }
    part2(input, inputStacks()).run { println("Part2: $this") }
}

private fun part1(input: String, inputStacks: List<ArrayDeque<Char>>): String =
    input.parseInput(inputStacks)
        .let { (stacks, moves) ->
            println("Stacks before: $stacks")
            moves.forEach { move ->
                // println("Move: $move")  // Debug
                repeat(move.quantity) {
                    val char = stacks[move.from - 1].removeLast()
                    stacks[move.to - 1].addLast(char)
                }
                // println("Stacks after: $stacks") // Debug
            }
            println("Stacks after: $stacks")
            stacks.map { it.last() }.joinToString("")
        }

private fun part2(input: String, inputStacks: List<ArrayDeque<Char>>): String =
    input.parseInput(inputStacks)
        .let { (stacks, moves) ->
            println("Stacks before: $stacks")
            moves.forEach { move ->
                // println("Move: $move")  // Debug
                val chars = (1..move.quantity).map { stacks[move.from - 1].removeLast() }.reversed()
                stacks[move.to - 1].addAll(chars)
                // println("Stacks after: $stacks") // Debug
            }
            println("Stacks after: $stacks")
            stacks.map { it.last() }.joinToString("")
        }

private fun String.parseInput(stack: List<ArrayDeque<Char>>): Pair<List<ArrayDeque<Char>>, Sequence<Instruction>> {
    val moves = this.splitOnBlankLines().last()
    return stack to moves.parseMoves()
}

private fun String.parseStack() {
    this.splitOnLineBreaks().toList().dropLast(1).map { }
}

private fun String.parseMoves(): Sequence<Instruction> {
    return this.splitOnLineBreaks().map {
        val (quantity, from, to) = numberRegex.findAll(it).toList().map { it.value.toInt() }
        Instruction(quantity = quantity, from = from, to = to)
    }
}

private data class Instruction(
    val quantity: Int, val from: Int, val to: Int,
)

private val numberRegex = "[0-9]{1,2}".toRegex()
