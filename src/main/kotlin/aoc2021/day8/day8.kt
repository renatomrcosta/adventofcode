package aoc2021.day8

import aoc2021.readFile
import aoc2021.splitOnLineBreaks

private val shortInput = """
    acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf
""".trimIndent()
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
    println("Part2")
    check(calculatePart2(shortInput.parse()) == 5353)
    check(calculatePart2(testInput.parse()) == 61229)
    val part2 = calculatePart2(file.parse())
    println("Part2 Result: $part2")
}

private fun calculatePart1(input: List<Pair<List<String>, List<String>>>): Int {
    // how many times do digits 1, 4, 7, or 8 appear?
    return input.flatMap { it.second }.count { it.length in listOf(2, 3, 4, 7) }
}

private fun calculatePart2(inputs: List<Pair<List<String>, List<String>>>): Int {
    return inputs.map { (input, output) ->
        val translationMap = buildTranslationMap(input)
        println("------")
        println("TranslationMap: $translationMap")
        val translatedOutputs = output.map { it.toCharArray().sorted().joinToString("") }
            .also { println("Sorted outputs $it") }
            .map { translationMap[it] ?: error("value not found = $it") }

        println("Translated Outputs: $translatedOutputs")
        translatedOutputs.joinToString("").toInt()
    }.sum()
}

private fun buildTranslationMap(inputs: List<String>): Map<String, Int> {
    val oneConfiguration = inputs.first { it.length == 2 }
    val sevenConfiguration = inputs.first { it.length == 3 }
    val fourConfiguration = inputs.first { it.length == 4 }
    val eightConfiguration = inputs.first { it.length == 7 }

    // zero is 4 - 1  == bd
    // use to derive other numbers
    // d = is not in 8
    val fourMinusOne = fourConfiguration.filterNot { it in oneConfiguration }.toList()

    val zeroConfiguration =
        inputs
            .filter { it.length == 6 }
            .find { it.toList().containsAll(eightConfiguration.toList() - (fourMinusOne[0])) }
            ?: inputs
                .filter { it.length == 6 }
                .find { it.toList().containsAll(eightConfiguration.toList() - fourMinusOne[1]) }
            ?: error("what in tarnation")

    val nineConfiguration = inputs
        .first { it.length == 6 && it != zeroConfiguration && it.toList().containsAll(oneConfiguration.toList()) }

    val sixConfiguration =
        inputs.first { it.length == 6 && it != nineConfiguration && it != zeroConfiguration }

    val threeConfiguration = inputs.first { it.length == 5 && it.toList().containsAll(oneConfiguration.toList()) }

    val fiveConfiguration =
        inputs.first { it.length == 5 && it != threeConfiguration && it.toList().containsAll(fourMinusOne) }

    val twoConfiguration = inputs.first { it.length == 5 && it != threeConfiguration && it != fiveConfiguration }

    return mapOf(
        zeroConfiguration.toCharArray().sorted().joinToString("") to 0,
        oneConfiguration.toCharArray().sorted().joinToString("") to 1,
        twoConfiguration.toCharArray().sorted().joinToString("") to 2,
        threeConfiguration.toCharArray().sorted().joinToString("") to 3,
        fourConfiguration.toCharArray().sorted().joinToString("") to 4,
        fiveConfiguration.toCharArray().sorted().joinToString("") to 5,
        sixConfiguration.toCharArray().sorted().joinToString("") to 6,
        sevenConfiguration.toCharArray().sorted().joinToString("") to 7,
        eightConfiguration.toCharArray().sorted().joinToString("") to 8,
        nineConfiguration.toCharArray().sorted().joinToString("") to 9,
    )
}

private fun String.parse(): List<Pair<List<String>, List<String>>> = this.splitOnLineBreaks()
    .map { line -> line.split("|") }
    .map { (left, right) -> left.split(" ") to right.split(" ") }
    .map { (inputList, outputList) -> inputList.filter { it.isNotBlank() } to outputList.filter { it.isNotBlank() } }
    .toList()

