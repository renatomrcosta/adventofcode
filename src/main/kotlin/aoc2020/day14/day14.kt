package aoc2020.day14

import aoc2020.readFile
import aoc2020.splitOnLineBreaks

fun main() {
    parseInput(testData).run { println(values.sum()) }

    parseInput(readFile("day14.txt")).run { println(values.sum()) }

    parseInputPart2(testData2).run { println(values.sum()) }

    parseInputPart2(readFile("day14.txt")).run { println(values.sum()) }
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

fun parseInputPart2(input: String): MutableMap<Long, Long> {
    val result = mutableMapOf<Long, Long>()
    var mask = ""
    input.splitOnLineBreaks().filter { it.isNotEmpty() }.forEach { line ->
        if (isLineMask(line)) {
            mask = line.split("=").last().trim()
        } else {
            val memAddress = getMemoryAddress(line)
            val number = getNumber(line)
            val resulting = applyMaskV2(mask = mask, address = memAddress)

            resulting.forEach {
                result[it] = number.toLong()
            }
        }
    }
    return result
}

fun applyMask(mask: String, number: Int): Long {
    var bNumber = getPaddedBinaryNumber(number, mask)

    mask.forEachIndexed { index, character ->
        if (character == '1' || character == '0') {
            bNumber = bNumber.replaceRange(index, index + 1, character.toString())
        }
    }
    return bNumber.toLong(2)
}

fun applyMaskV2(mask: String, address: Int): List<Long> {
    val result = mutableListOf<Long>()
    var bNumber = getPaddedBinaryNumber(address, mask)

    mask.forEachIndexed { index, character ->
        when (character) {
            '1' -> bNumber = bNumber.replaceRange(index, index + 1, "1")
            'X' -> bNumber = bNumber.replaceRange(index, index + 1, "X")
        }
    }

    addPossibleAddresses(bNumber, result)

    return result
}

private fun addPossibleAddresses(bNumber: String, result: MutableList<Long>) {
    if (bNumber.contains('X')) {
        val index = bNumber.indexOf('X')
        val item1 = bNumber.replaceRange(index, index + 1, "1")
        val item0 = bNumber.replaceRange(index, index + 1, "0")
        addPossibleAddresses(item1, result)
        addPossibleAddresses(item0, result)
    } else {
        result.add(bNumber.toLong(2))
    }
}

private fun getPaddedBinaryNumber(number: Int, mask: String): String {
    var bNumber = Integer.toBinaryString(number)
    bNumber = bNumber.padStart(mask.length, '0')
    return bNumber
}

fun getMemoryAddress(line: String): Int = line.substringAfter("[").substringBefore("]").trim().toInt()

fun getNumber(line: String): Int = line.split("=").last().trim().toInt()

fun isLineMask(line: String): Boolean =
    line.startsWith("mask")

val testData = """
    mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
    mem[8] = 11
    mem[7] = 101
    mem[8] = 0
    
""".trimIndent()

val testData2 = """
    mask = 000000000000000000000000000000X1001X
    mem[42] = 100
    mask = 00000000000000000000000000000000X0XX
    mem[26] = 1
    
""".trimIndent()
