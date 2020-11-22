package aoc2019

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

    // println(listOf(434567, 123456, 123434, 122345).map { it.hasOnlyAdjacentPairs() })
    // println(listOf(112233, 124444, 111122, 333354, 331321).map { it.hasAdjacentDigits() })
    // println(listOf(111233, 123444, 111232, 343334, 999119).map { it.hasAdjacentDigits() })
    // val test1 = 111232
    // println(test1.hasAdjacentDigits())
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

    // As long as there's a group of 2 adjacent, the rest do not matter
    val groupOfDigits = digits.groupInOrder()
    return groupOfDigits.any { it.size == 2 }
}

private fun <T> List<T>.groupInOrder(): List<List<T>> {
    val groupedItems = mutableListOf<MutableList<T>>()

    var previousItem = this[0]
    groupedItems.add(mutableListOf(previousItem)) // Initialize the list at index 0

    for(index in 1 until this.size) {
        val currentItem = this[index]
        if(currentItem == previousItem) {
            groupedItems.last().add(currentItem)
        } else {
            groupedItems.add(mutableListOf(currentItem))
        }
        previousItem = currentItem
    }

    return groupedItems.toList()
}

private fun Int.hasSixDigits(): Boolean =
    this.toString().length == 6

