package aoc2019

import kotlin.math.pow

private val PUZZLE_INPUT_RANGE = (136818..685979)

/*
It is a six-digit number.
The value is within the range given in your puzzle input.
Two adjacent digits are the same (like 22 in 122345).
Going from left to right, the digits never decrease; they only ever increase or stay the same (like 111123 or 135679).
 */

fun main() {
    val possiblePasswords = PUZZLE_INPUT_RANGE
        .filter { it.hasSixDigits() }
        .filter { it.hasAdjacentDigits() }
        .filter { it.neverDecreasesDigitsFromLeftToRight() }

    println(possiblePasswords.size)

    // println(listOf(434567, 123456, 123434, 122345).map { it.hasAdjacentDigits() })
}

private fun Int.neverDecreasesDigitsFromLeftToRight(): Boolean {
    if (!this.hasSixDigits()) error("Invalid input")

    val digits = this.extractDigits()
    var previousDigit = digits[0]
    for (i in 1 until 6) {
        val currentDigit = digits[i]
        if (currentDigit < previousDigit) return false
        previousDigit = currentDigit
    }
    return true
}

private fun Int.extractDigits(): List<Int> =
    this.toString()
        .map {
            Character.getNumericValue(it)
        }

private fun Int.hasAdjacentDigits(): Boolean {
    if (!this.hasSixDigits()) error("Invalid input")

    val digits = this.extractDigits()
    var previousDigit = digits[0]
    for (i in 1 until 6) {
        val currentDigit = digits[i]
        if (currentDigit == previousDigit) return true
        previousDigit = currentDigit
    }
    return false
}

private fun Int.hasSixDigits(): Boolean =
    this.toString().length == 6

