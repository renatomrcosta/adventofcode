package aoc2020.day7

import aoc2020.readFile

fun main() {
    // part1()
    part2()
}

private fun part2() {
    println("Part 2 - Test Data")
    val bagInput = "shiny gold bag"
    parseInput(testData).run {
        val result = getChildrenAmount(bagInput, this).also { println(it) }
        assert(result - 1 == 32)
    }
    parseInput(testData2).run {
        val result = getChildrenAmount(bagInput, this).also { println(it) }
        assert(result - 1 == 126)
    }

    println("Part 2 - Input Data")
    parseInput(readFile("day7.txt")).run {
        getChildrenAmount(bagInput, this).also { println(it-1) }
    }
}

fun getChildrenAmount(
    bag: String,
    input: Map<String, List<BagRule>>,
): Int {
    val entry = input[bag] ?: error("no rule defined for key $bag")

    if (entry.isEmpty()) return 1

    return entry.map {
        getChildrenAmount(it.heldBag, input) * it.maxQuantity
    }.sum() + 1
}

private fun part1() {
    val bagInput = "shiny gold bag"
    println("Part 1 - test Data")
    parseInput(testData).run {
        val testOutput = mutableListOf<String>()
        getAllContainers(bagInput, this, testOutput)
        println(testOutput)
        println("${testOutput.size}")
    }

    println("Part1 - Input Data")
    parseInput(readFile("day7.txt")).run {
        val testOutput = mutableListOf<String>()
        getAllContainers(bagInput, this, testOutput)
        println(testOutput)
        println("${testOutput.size}")
    }
}

fun getAllContainers(
    bag: String,
    input: Map<String, List<BagRule>>,
    output: MutableList<String>,
) {
    val containers = getAllContainersByString(bag, input).filterNot {
        output.contains(it)
    }
    output.addAll(containers)

    containers.forEach { container ->
        getAllContainers(container, input, output)
    }
}

fun getAllContainersByString(
    bag: String,
    input: Map<String, List<BagRule>>,
): Set<String> =
    input.filter { entry ->
        entry.value.any { rule ->
            rule.heldBag == bag
        }
    }.keys

fun parseInput(input: String) =
    input.split("\n")
        .filter { it.isNotEmpty() }
        .map { it.replace(".", "") }
        .map { it.replace("bags", "bag") }
        .map { it.split("contain") }
        .map { (first, second) ->
            first.trim() to
                second.split(",")
                    .map { it.trim() }
                    .mapNotNull { it.toBagRule() }
        }.toMap()

data class BagRule(val heldBag: String, val maxQuantity: Int)

val NUMBER_REGEX = Regex("[0-9]+")
val TEXT_REGEX = Regex("[^0-9]+")
fun String.toBagRule(): BagRule? =
    if (this != "no other bag") {
        val quantity = NUMBER_REGEX.find(this)?.value?.toInt() ?: error("value not found: $this")
        val bagText = TEXT_REGEX.find(this)?.value?.trim() ?: error("bag not found: $this")
        BagRule(heldBag = bagText, maxQuantity = quantity)
    } else null

val testData = """
    light red bags contain 1 bright white bag, 2 muted yellow bags.
    dark orange bags contain 3 bright white bags, 4 muted yellow bags.
    bright white bags contain 1 shiny gold bag.
    muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.
    shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.
    dark olive bags contain 3 faded blue bags, 4 dotted black bags.
    vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.
    faded blue bags contain no other bags.
    dotted black bags contain no other bags.
    
""".trimIndent()

val testData2 = """
    shiny gold bags contain 2 dark red bags.
    dark red bags contain 2 dark orange bags.
    dark orange bags contain 2 dark yellow bags.
    dark yellow bags contain 2 dark green bags.
    dark green bags contain 2 dark blue bags.
    dark blue bags contain 2 dark violet bags.
    dark violet bags contain no other bags.
    
""".trimIndent()
