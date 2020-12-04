package aoc2020.day4

import aoc2020.readFile

val testData = """
    ecl:gry pid:860033327 eyr:2020 hcl:#fffffd
    byr:1937 iyr:2017 cid:147 hgt:183cm

    iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884
    hcl:#cfa07d byr:1929

    hcl:#ae17e1 iyr:2013
    eyr:2024
    ecl:brn pid:760753108 byr:1931
    hgt:179cm

    hcl:#cfa07d eyr:2025 pid:166559648
    iyr:2011 ecl:brn hgt:59in
""".trimIndent()

data class Document(val props: Map<String, String>) {

    // For part1, passports are valid when:
    // All fields are accounted for. cid is optional
    fun isValidPassport(): Boolean = props.keys.containsAll(requiredFields)

    companion object {
        private val requiredFields = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")
    }
}

// Expects a string separating the key / value pairs with semi-colon (;)
// ecl:gry;pid:860033327;eyr:2020;hcl:#fffffd;byr:1937;iyr:2017;cid:147;hgt:183cm
// Outputs a Document data class with key / value pairs in the props property
fun String.toDocument(): Document {
    val propertyMap = this.split(";")
        .filter { it.isNotEmpty() }
        .map {
            val (key, value) = it.split(":")
            key to value
        }.toMap()

    return Document(props = propertyMap)
}

// Splits documents by empty / blank line, and converts to an easy to perse format"
// Outputs: ecl:gry;pid:860033327;eyr:2020;hcl:#fffffd;byr:1937;iyr:2017;cid:147;hgt:183cm
fun parseDocuments(input: String): List<Document> =
    input.split(Regex("\\n[\\n]+", setOf(RegexOption.MULTILINE)))
        .filter { it.isNotEmpty() }
        .map { it.replace(" ", ";") }
        .map { it.replace("\n", ";") }
        .map { it.toDocument() }

fun main() {
    println("Part1 - Test Data")
    parseDocuments(testData)
        .filter { it.isValidPassport() }
        .forEach { println(it) }

    println("Part1 - Real data")
    val fileInput = readFile("day4.txt")
    parseDocuments(fileInput)
        .filter { it.isValidPassport() }
        .also {
            println("Total valid passports: ${it.size}")
        }
        .forEach { println(it) }
}
