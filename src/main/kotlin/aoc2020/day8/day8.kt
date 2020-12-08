package aoc2020.day8

import aoc2020.readFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicInteger

fun main() {
    part1()
    part2()
}

fun part2() = runBlocking(Dispatchers.Default) {
    // Since we know that the premise is that only ONE change is necessary for solving the problem
    // we can run async operations on each jmp / nop operations and see if any of them pan out at the end.
    // We map errors to null and only come out with proper results.
    println("Part2 - BRUTE FOOOORCE")
    parseInput(readFile("day8.txt")).run {
        this@run
            .mapIndexed { index, instruction ->
                async {
                    try {
                        when (instruction.operation) {
                            "jmp" -> {
                                val computer = Computer(createNewInstructionList(index, instruction))
                                computer.operate()
                                computer.accumulator
                            }
                            "nop" -> {
                                val computer = Computer(createNewInstructionList(index, instruction))
                                computer.operate()
                                computer.accumulator
                            }
                            else -> null
                        }
                    } catch (e: Exception) {
                        null
                    }
                }
            }.mapNotNull { it.await() }
            .forEach {
                println("It ran successfully! Acc = $it")
            }
    }
}

private fun List<Instruction>.createNewInstructionList(
    index: Int,
    instruction: Instruction,
): MutableList<Instruction> {
    val newList = mutableListOf<Instruction>().apply { addAll(this.map { it.copy(hasBeenExecuted = false) }) }
    val newOperation = if (instruction.operation == "nop") "jmp" else "nop"
    newList[index] = instruction.copy(operation = newOperation)
    return newList
}

fun part1() {
    println("Part1 - Test Data")
    Computer(parseInput(testData)).run {
        operate()
        println("Final Value - $accumulator")
    }

    val input = readFile("day8.txt")
    println("Part1 - Real Data")
    Computer(parseInput(input)).run {
        operate()
        println("Final Va lue - $accumulator")
    }
}

fun parseInput(input: String): List<Instruction> =
    input.split("\n")
        .filter { it.isNotEmpty() }
        .map { it.toInstruction() }

fun String.toInstruction(): Instruction {
    val (operation, argument) = this.split(" ")
    return Instruction(
        operation = operation.trim(),
        argument = argument.trim().toInt()
    )
}

data class Computer(val instructions: List<Instruction>) {
    val accumulator = AtomicInteger(0)
    private val instructionIndex = AtomicInteger(0)

    fun operate() {
        while (instructionIndex.get() <= instructions.lastIndex) {
            operate(instructions[instructionIndex.get()])
        }
    }

    private fun operate(instruction: Instruction): Unit = with(instruction) {
        if (instruction.hasBeenExecuted) {
            error("Instruction has executed before. Accumulator value $accumulator\n instruction: $instruction \n${this@Computer}")
        }
        when (operation) {
            "nop" -> {
                instructionIndex.getAndAdd(1)
            }
            "acc" -> {
                accumulator.getAndAdd(argument)
                instructionIndex.getAndAdd(1)
            }
            "jmp" -> {
                instructionIndex.getAndAdd(argument)
            }
            else -> error("invalid operation")
        }
        hasBeenExecuted = true
    }
}

data class Instruction(val operation: String, val argument: Int, var hasBeenExecuted: Boolean = false)

val testData = """
    nop +0
    acc +1
    jmp +4
    acc +3
    jmp -3
    acc -99
    acc +1
    jmp -4
    acc +6
""".trimIndent()
