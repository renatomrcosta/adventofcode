package aoc2021.day8

import aoc2021.readFile
import aoc2021.splitOnLineBreaks

private val testInput = """
    be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
    edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
    fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
    fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
    aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
    fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
    dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
    bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
    egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
    gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce
""".trimIndent()

fun main() {
    val file = readFile("day8.txt")
    println("Part1")

    check(calculatePart1(testInput.parse()) == 26)
    val part1 = calculatePart1(file.parse())
    println("Part1 Result: $part1")

    println("----")
}

private fun calculatePart1(input: Pair<List<String>, List<String>>): Int {
    // how many times do digits 1, 4, 7, or 8 appear?
    return input.second.count { it.length in listOf(2, 3, 4, 7) }
}

private fun String.parse(): Pair<List<String>, List<String>> = this.splitOnLineBreaks()
    .map { line ->
        val (left, right) = line.split("|")
        left.split(" ") to right.split(" ")
    }
    .let {
        it.flatMap { it.first }.toList() to it.flatMap { it.second }.toList()
    }

