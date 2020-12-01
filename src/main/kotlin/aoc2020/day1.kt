package aoc2020

private const val DESIRED_SUM = 2020

/*
    Find two entries in a list that sum up to a constant (2020). Then return their multiplied value
*/
fun main() {
    println("PART1 - TEST DATA")
    val testData = listOf(1721, 979, 366, 299, 675, 1456)
    findPairEqualToConstantOrNull(testData)?.let {
        println(it)
        assert(it.first == 1721 && it.second == 299)
        val result = obtainResult(it)
        assert(result == 514579)
        println("Result $result")

    } ?: error("pair not found")

    println("PART1 - INPUT")
    val input = loadInput()
    val pair = findPairEqualToConstantOrNull(input) ?: error("pair not found")
    println(pair)
    println("Result: ${obtainResult(pair)}")

    println("PART2 - TEST DATA")
    findTripleEqualToConstantOrNull(testData)?.let {
        println(it)
        assert(it.first == 979 && it.second == 366 && it.third == 675)

        val result = obtainResult(it)
        assert(result == 241861950)
        println("Result $result")
    } ?: error("triple not found")

    println("PART2 - INPUT")
    val triple = findTripleEqualToConstantOrNull(input) ?: error("triple not found")
    println(triple)
    println("Triple result ${obtainResult(triple)}")
}

fun loadInput(): List<Int> = readFile("day1.txt")
    .split("\n")
    .filter { it.isNotEmpty() }
    .map { it.toInt() }

fun obtainResult(pair: Pair<Int, Int>) = pair.first * pair.second
fun obtainResult(triple: Triple<Int, Int, Int>) = triple.first * triple.second * triple.third

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

// I ran out of time trying to come up with a generic solution. Is ass, but should work
fun findTripleEqualToConstantOrNull(input: List<Int>): Triple<Int, Int, Int>? {
    input.forEachIndexed { i, item1 ->
        input.forEachIndexed { j, item2 ->
            input.forEachIndexed { k, item3 ->
                // Ignore same index
                if (!(i == j || i == k || j == k)) {
                    if (item1 + item2 + item3 == DESIRED_SUM) return Triple(item1, item2, item3)
                }
            }
        }
    }
    return null
}
