package aoc2020

val testData = listOf(
    "..##.......",
    "#...#...#..",
    ".#....#..#.",
    "..#.#...#.#",
    ".#...##..#.",
    "..#.##.....",
    ".#.#.#....#",
    ".#........#",
    "#.##...#...",
    "#...##....#",
    ".#..#...#.#",
)

fun main() {
    println("Part1 - Test data")
    countTraversal(testData)
    println("Part1 - Real data")
    countTraversal(getInputFromFile())
}

private fun getInputFromFile(): List<String> =
    readFile("day3.txt")
        .split("\n")
        .filter { it.isNotEmpty() }

private fun countTraversal(traversalData: List<String>) {
    var xCoordinate = 0
    var treeSquare = 0

    traversalData.forEach { row ->
        if (isPositionTree(row, xCoordinate)) {
            treeSquare++
        }
        xCoordinate += 3
    }

    println("Tree Squares = $treeSquare")
}

private fun isPositionTree(row: String, xCoordinate: Int): Boolean {
    // X loops infinitely. Get proper loop
    val absoluteCoordinate = getAbsoluteCoordinate(row.length, xCoordinate)
    return row[absoluteCoordinate].toString() == "#"
}

private fun getAbsoluteCoordinate(rowSize: Int, xCoordinate: Int): Int = xCoordinate % rowSize
