package aoc2020

// exemplifies the task at hand
private val testInputs = listOf(
    // "1-3 a: abcde",
    // "1-3 b: cdefg",
    // "2-9 c: ccccccccc",
    "8-11 l: qllllqllklhlvtl",
    "15-17 w: wwwwrswthgwhkwwrw"
)

// Small data representation of a password policy. It must be initialized with its necessary parameters
private data class PasswordPolicy(
    val password: String,
    val letter: String,
    val minLetterOccurrences: Int,
    val maxLetterOccurrences: Int,
) {
    init {
        check(minLetterOccurrences <= maxLetterOccurrences) { "minimum letter occurrences ($minLetterOccurrences) cannot be smaller than maximum number of letter occurences ($maxLetterOccurrences) " }
    }

    // Checks the password string for the number of occurrences of a given string. Could likely be also done with regex.
    val valid: Boolean by lazy {
        val letterCountMap = password.groupingBy { it.toString() }.eachCount()
        val targetLetterCount = letterCountMap[letter] ?: return@lazy false // letter not found in policy
        targetLetterCount in minLetterOccurrences..maxLetterOccurrences
    }
}

fun part1() {
    println("Part1 - Test Data")
    testInputs
        .map { it.mapToPasswordWithPolicy() }
        .filter { it.valid }
        .run {
            println("Total: ${this.size} | Valid passwords: $this")
        }

    println("Part1 - Real Data")
    getInputFromFile()
        .map { it.mapToPasswordWithPolicy() }
        .filter { it.valid }
        .run {
            println("Total: ${this.size} | Valid passwords: $this")
        }
}

// Reads the test input from file as a list of strings
private fun getInputFromFile(): List<String> =
    readFile("day2.txt")
        .split("\n")
        .filter { it.isNotEmpty() }

private val numberMatcherRegex = Regex("[0-9]+")

// Maps a string of the input format to a policy object
private fun String.mapToPasswordWithPolicy(): PasswordPolicy {
    val (policyString, password) = this.split(":")
    val letter = policyString.last().toString()
    val (minValue, maxValue) = numberMatcherRegex.findAll(policyString).toList()

    return PasswordPolicy(
        password = password.trim(),
        letter = letter,
        minLetterOccurrences = minValue.value.toInt(),
        maxLetterOccurrences = maxValue.value.toInt(),
    )
}

fun main() {
    part1()
}
