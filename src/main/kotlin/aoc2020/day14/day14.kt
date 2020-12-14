package aoc2020.day14

import aoc2020.readFile
import aoc2020.splitOnLineBreaks

fun main() {
    parseInput(testData).run {
        println(values.sum())
    }

    parseInput(readFile("day14.txt")).run { println(values.sum()) }
}

fun parseInput(input: String): MutableMap<Int, Long> {
    val result = mutableMapOf<Int, Long>()
    var mask = ""
    input.splitOnLineBreaks().filter { it.isNotEmpty() }.forEach { line ->
        if (isLineMask(line)) {
            mask = line.split("=").last().trim()
        } else {
            val memAddress = getMemoryAddress(line)
            val number = getNumber(line)
            val resulting = applyMask(mask, number)
            result[memAddress] = resulting
        }
    }

    return result
}

fun getMemoryAddress(line: String): Int = line.substringAfter("[").substringBefore("]").trim().toInt()

fun getNumber(line: String): Int = line.split("=").last().trim().toInt()

fun applyMask(mask: String, number: Int): Long {
    var bNumber = Integer.toBinaryString(number)
    bNumber = bNumber.padStart(mask.length, '0')

    mask.forEachIndexed { index, character ->
        if (character == '1' || character == '0') {
            bNumber = bNumber.replaceRange(index, index + 1, character.toString())
        }
    }
    return bNumber.toLong(2)
}

fun isLineMask(line: String): Boolean =
    line.startsWith("mask")

val testData = """
    mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
    mem[8] = 11
    mem[7] = 101
    mem[8] = 0
    
""".trimIndent()
