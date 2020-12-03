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
    val fileInputData = getInputFromFile()
    println("Part1 - Test data")
    countTraversal(testData, rightStep = 3)
    println("Part1 - Real data")
    countTraversal(fileInputData, rightStep = 3)

    println("Part2 - Test data")
    listOf(
        countTraversal(testData, rightStep = 1),
        countTraversal(testData, rightStep = 3),
        countTraversal(testData, rightStep = 5),
        countTraversal(testData, rightStep = 7),
        countTraversal(testData, rightStep = 1, downStep = 2),
    ).reduce { acc, i -> acc * i }.run { println("Multiplied value $this") }

    println("part2 - Real data")
    listOf(
        countTraversal(fileInputData, rightStep = 1),
        countTraversal(fileInputData, rightStep = 3),
        countTraversal(fileInputData, rightStep = 5),
        countTraversal(fileInputData, rightStep = 7),
        countTraversal(fileInputData, rightStep = 1, downStep = 2),
    ).reduce { acc, i -> acc * i }.run { println("Multiplied value $this") }
}

private fun getInputFromFile(): List<String> =
    readFile("day3.txt")
        .split("\n")
        .filter { it.isNotEmpty() }

private fun countTraversal(traversalData: List<String>, rightStep: Int, downStep: Int = 1): Int {
    var xCoordinate = 0
    var treeSquareCount = 0

    for (i in traversalData.indices step downStep) {
        val row = traversalData[i]
        if (isPositionTree(row, xCoordinate)) {
            treeSquareCount++
        }
        xCoordinate += rightStep
    }

    println("Tree Squares = $treeSquareCount")
    return treeSquareCount
}

private fun isPositionTree(row: String, xCoordinate: Int): Boolean {
    // X loops infinitely. Get proper loop
    val absoluteCoordinate = getAbsoluteCoordinate(row.length, xCoordinate)
    return row[absoluteCoordinate].toString() == "#"
}

private fun getAbsoluteCoordinate(rowSize: Int, xCoordinate: Int): Int = xCoordinate % rowSize
