package aoc2020.day2

import aoc2020.readFile

// exemplifies the task at hand
private val testInputs = listOf(
    "1-3 a: abcde",
    "1-3 b: cdefg",
    "2-9 c: ccccccccc",
)

// generic data representation of a password policy.
// the indices are different in the policies, so I only enforce the valid function and the password and letter parameters
private sealed class GenericPasswordPolicy {
    abstract val password: String
    abstract val letter: String
    abstract fun isPasswordValid(): Boolean
}

private data class TobogganPasswordPolicy(
    override val password: String,
    override val letter: String,
    val initialIndex: Int,
    val finalIndex: Int,
) : GenericPasswordPolicy() {
    override fun isPasswordValid(): Boolean {
        val initialLetter = password.getOrNull(initialIndex - 1)?.toString() ?: error("invalid initial index")
        val finalLetter = password.getOrNull(finalIndex - 1)?.toString() ?: error("invalid final index")

        return (initialLetter == letter) xor (finalLetter == letter)
    }
}

private data class SledRentalPasswordPolicy(
    override val password: String,
    override val letter: String,
    val minLetterOccurrences: Int,
    val maxLetterOccurrences: Int,
) : GenericPasswordPolicy() {
    init {
        check(minLetterOccurrences <= maxLetterOccurrences) { "minimum letter occurrences ($minLetterOccurrences) cannot be smaller than maximum number of letter occurences ($maxLetterOccurrences) " }
    }

    override fun isPasswordValid(): Boolean {
        val letterCountMap = password.groupingBy { it.toString() }.eachCount()
        val targetLetterCount = letterCountMap[letter] ?: return false // letter not found in policy
        return targetLetterCount in minLetterOccurrences..maxLetterOccurrences
    }
}

// Reads the test input from file as a list of strings
private fun getInputFromFile(): List<String> =
    readFile("day2.txt")
        .split("\n")
        .filter { it.isNotEmpty() }

private val numberMatcherRegex = Regex("[0-9]+")

// Maps a string of the input format to a policy object, depending on the desired subtype of GenericPasswordPolicy
private inline fun <reified T : GenericPasswordPolicy> String.mapToPasswordWithPolicy(): T {
    val (policyString, password) = this.split(":")
    val letter = policyString.last().toString()
    val (minValue, maxValue) = numberMatcherRegex.findAll(policyString).toList()

    return when (T::class) {
        TobogganPasswordPolicy::class -> TobogganPasswordPolicy(
            password = password.trim(),
            letter = letter,
            initialIndex = minValue.value.toInt(),
            finalIndex = maxValue.value.toInt(),
        )
        SledRentalPasswordPolicy::class -> SledRentalPasswordPolicy(
            password = password.trim(),
            letter = letter,
            minLetterOccurrences = minValue.value.toInt(),
            maxLetterOccurrences = maxValue.value.toInt(),
        )
        else -> error("Invalid policy type")
    } as T
}

fun part1() {
    println("Part1 - Test Data")
    testInputs
        .map { it.mapToPasswordWithPolicy<SledRentalPasswordPolicy>() }
        .filter { it.isPasswordValid() }
        .run {
            println("Total: ${this.size} | Valid passwords: $this")
        }

    println("Part1 - Real Data")
    getInputFromFile()
        .map { it.mapToPasswordWithPolicy<SledRentalPasswordPolicy>() }
        .filter { it.isPasswordValid() }
        .run {
            println("Total: ${this.size} | Valid passwords: $this")
        }
}

fun part2() {
    println("Part2 - Test Data")
    testInputs
        .map { it.mapToPasswordWithPolicy<TobogganPasswordPolicy>() }
        .filter { it.isPasswordValid() }
        .run {
            println("Total: ${this.size} | Valid passwords: $this")
        }

    println("Part2 - Real Data")
    getInputFromFile()
        .map { it.mapToPasswordWithPolicy<TobogganPasswordPolicy>() }
        .filter { it.isPasswordValid() }
        .run {
            println("Total: ${this.size} | Valid passwords: $this")
        }
}

fun main() {
    part1()
    part2()
}
