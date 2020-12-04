package aoc2020.day4

import aoc2020.readFile

fun main() {
    // If you wanna see just the part1 approach, check this file's commits
    println("Part1 - Test Data")
    parseDocuments(PART1_TEST_DATA)
        .filter { it.isValidPassport() }
        .forEach { println(it) }


    println("Part 2 - TestData")
    parseDocuments(PART2_TEST_DATA)
        .filter { it.isValidPassport() }
        .forEach { println(it) }

    println("Part1 && 2- Real data")
    val fileInput = readFile("day4.txt")
    parseDocuments(fileInput)
        .filter { it.isValidPassport() }
        .also {
            println("Total valid passports: ${it.size}")
        }
        .forEach { println(it) }
}


fun interface Validator {
    fun isValid(input: String): Boolean
}

data class Document(val props: Map<String, String>) {

    // For part1, passports are valid when:
    // All fields are accounted for. cid is optional
    fun isValidPassport(): Boolean {
        if (!props.keys.containsAll(requiredFieldValidationRules.keys)) return false

        return props.entries
            .filter { !optionalFields.contains(it.key) } // Ignore CID
            .all {
                requiredFieldValidationRules[it.key]?.isValid(it.value) ?: false
            }
    }

    companion object {
        private val optionalFields = listOf("cid")
        private val requiredFieldValidationRules: Map<String, Validator> =
            mapOf(
                "byr" to Validator {
                    // byr (Birth Year) - four digits; at least 1920 and at most 2002.
                    it.length == 4 && it.toInt() in 1920..2012
                },
                "iyr" to Validator {
                    // iyr (Issue Year) - four digits; at least 2010 and at most 2020.
                    it.length == 4 && it.toInt() in 2010..2020
                },
                "eyr" to Validator {
                    //eyr (Expiration Year) - four digits; at least 2020 and at most 2030
                    it.length == 4 && it.toInt() in 2020..2030
                },
                "hgt" to Validator {
                    //hgt (Height) - a number followed by either cm or in:
                    // If cm, the number must be at least 150 and at most 193.
                    // If in, the number must be at least 59 and at most 76.
                    when {
                        it.endsWith("cm") -> it.replace("cm", "").toInt() in 150..193
                        it.endsWith("in") -> it.replace("in", "").toInt() in 59..76
                        else -> false // Invalid height modifier
                    }
                },
                "hcl" to Validator {
                    //hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
                    it.matches(Regex("[#][0-9a-f]{6}"))
                },
                "ecl" to Validator {
                    //ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
                    validEyeColors.contains(it)
                },
                "pid" to Validator {
                    //pid (Passport ID) - a nine-digit number, including leading zeroes.
                    it.matches(Regex("[0-9]{9}"))
                },
            )
        private val validEyeColors = listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
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




private val PART1_TEST_DATA = """
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

private val PART2_TEST_DATA = """
    pid:087499704 hgt:74in ecl:grn iyr:2012 eyr:2030 byr:1980
    hcl:#623a2f

    eyr:2029 ecl:blu cid:129 byr:1989
    iyr:2014 pid:896056539 hcl:#a97842 hgt:165cm

    hcl:#888785
    hgt:164cm byr:2001 iyr:2015 cid:88
    pid:545766238 ecl:hzl
    eyr:2022

    iyr:2010 hgt:158cm hcl:#b6652a ecl:blu byr:1944 eyr:2021 pid:093154719
""".trimIndent()
