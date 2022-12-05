package aoc2022.day5

import aoc2022.readFile
import aoc2022.splitOnBlankLines
import aoc2022.splitOnLineBreaks
import aoc2022.transpose

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

fun main() {
    val input = readFile("day5.txt")
    part1(testInput).run { require(this == "CMZ") { println("THIS $this") } }
    part1(input).run { println("Part1: $this") }

    part2(testInput).run { require(this == "MCD") { println("THIS $this") } }
    part2(input).run { println("Part2: $this") }
}

private fun part1(input: String): String =
    input.parseInput()
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

private fun part2(input: String): String =
    input.parseInput()
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

private fun String.parseInput(): Pair<List<ArrayDeque<Char>>, Sequence<Instruction>> {
    val (stack, moves) = this.splitOnBlankLines().toList()
    return stack.parseStackTransposing() to moves.parseMoves()
}

private fun String.parseStack(): List<ArrayDeque<Char>> = buildList {
    val inputRows = this@parseStack.splitOnLineBreaks().toList()
    val rowNumbersRow = inputRows.last()
    val stackIds = numberRegex.findAll(rowNumbersRow).map { it.value }.toList()

    repeat(stackIds.size) { this.add(ArrayDeque()) }

    inputRows.dropLast(1).reversed()
        .forEach { row ->
            stackIds.forEach { stackId ->
                val charAtColumn = row[rowNumbersRow.indexOf(stackId)]
                if (charAtColumn in letterRange) {
                    this[stackId.toInt() - 1].addLast(charAtColumn)
                }
            }
        }
}

private fun String.parseStackTransposing(): List<ArrayDeque<Char>> = buildList {
    this@parseStackTransposing.splitOnLineBreaks().toList().dropLast(1).map {
        it.chunked(size = 4)
    }.transpose()
        .onEach { this.add(ArrayDeque()) }
        .forEachIndexed() { index, row ->
            row.reversed().map { box ->
                if (!box.isNullOrBlank()) {
                    this[index].addLast(box[1])
                }
            }
        }
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
private val letterRange = ('A'..'Z')