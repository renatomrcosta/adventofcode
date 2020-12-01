package aoc2020

private const val DESIRED_SUM = 2020

/*
    Find two entries in a list that sum up to a constant (2020). Then return their multiplied value
*/
fun main() {
    // val testData = listOf(1721, 979, 366, 299, 675, 1456)
    // findPairEqualToConstantOrNull(testData).run {
    //     println(this)
    //     assert(this?.first == 1721 && this.second == 299)
    //     println("Result ${obtainResult(this!!)}")
    // }

    val input = loadInput()
    val pair = findPairEqualToConstantOrNull(input) ?: error("pair not found")
    println(pair)
    println("Result: ${obtainResult(pair)}")
}

fun loadInput(): List<Int> = readFile("day1.txt")
    .split("\n")
    .filter { it.isNotEmpty() }
    .map { it.toInt() }

fun obtainResult(pair: Pair<Int, Int>) = pair.first * pair.second

fun findPairEqualToConstantOrNull(input: List<Int>): Pair<Int, Int>? {
    for (i in input.indices) {
        for (j in input.subList(i + 1, input.lastIndex).indices) {
            val firstItem = input[i]
            val secondItem = input[j]
            if (firstItem + secondItem == DESIRED_SUM) return Pair(firstItem, secondItem)
        }
    }
    return null
}
