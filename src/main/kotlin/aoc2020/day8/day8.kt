package aoc2020.day8

import aoc2020.readFile
import java.util.concurrent.atomic.AtomicInteger

fun main() {
    println("Part1 - Test Data")
    Computer(parseInput(testData)).run {
        // val finalValue = operate()
        // println("Final Value - $finalValue")
    }

    val input = readFile("day8.txt")
    println("Part1 - Real Data")
    Computer(parseInput(input)).run {
        val finalValue = operate()
        println("Final Value - $finalValue")
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
    private val accumulator = AtomicInteger(0)
    private val instructionIndex = AtomicInteger(0)

    fun operate(): Int {
        while (instructionIndex.get() <= instructions.lastIndex) {
            operate(instructions[instructionIndex.get()])
        }
        return accumulator.get()
    }

    private fun operate(instruction: Instruction): Unit = with(instruction) {
        if (instruction.hasBeenExecuted) {
            error("Instruction has executed before. Accumulator value $accumulator \n ${this@Computer}")
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
