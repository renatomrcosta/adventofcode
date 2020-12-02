package aoc2020

private val testInputs = listOf(
    "1-3 a: abcde",
    "1-3 b: cdefg",
    "2-9 c: ccccccccc",
)

private class Policy(
    val letter: String,
    val minLetterOccurrences: Int,
    val maxLetterOccurrences: Int,
) {
    init {
        check(minLetterOccurrences <= maxLetterOccurrences) { "minimum letter occurrences ($minLetterOccurrences) cannot be smaller than maximum number of letter occurences ($maxLetterOccurrences) " }
    }

    fun isPasswordValid(password: String): Boolean = TODO()
}

fun part1() {
    println("Part1 - Test Data")
    val mappedTestInputs = testInputs
        .map { it.mapToPassword() to it.mapToPolicy() }.toMap()
    findValidPasswords(mappedTestInputs).run { println(this) }

    println("Part1 - Real Data")
    val mappedInputs = getInputFromFile()
        .map { it.mapToPassword() to it.mapToPolicy() }.toMap()
    findValidPasswords(mappedInputs).run { println(this) }
}

private fun getInputFromFile(): List<String> =
    readFile("day2.txt")
        .split("\n")
        .filter { it.isNotEmpty() }

private fun findValidPasswords(inputs: Map<String, Policy>): List<String> = inputs.entries
    .filter { (password, policy) ->
        policy.isPasswordValid(password)
    }.map { it.key }

private fun String.mapToPolicy(): Policy = TODO()
private fun String.mapToPassword(): String = TODO()

fun main() {
    part1()
}
