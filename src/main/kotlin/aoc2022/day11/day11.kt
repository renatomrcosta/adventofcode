package aoc2022.day11

fun main() {
    part1(testInput()).run { require(this == 10605L) { "Part1 error. Was $this" } }
    part1(realMonkeys()).run { println("Part1:  $this") }

    part2(testInput()).run { require(this == 2713310158L) { "Part2 error. Was $this" } }
    part2(realMonkeys()).run { println("Part2:  $this") }
}

private fun part1(input: List<Monkey>): Long {
    // println("Start up - $input")
    repeat(20) {
        input.forEach { monkey ->
            monkey.operate().forEach { (newMonkey, item) ->
                input[newMonkey].items.addAll(item)
            }
        }
        // println("End of round $it - $input")
    }
    return input.map { it.inspectionCount }.sortedByDescending { it }.take(2).fold(1L) { acc, count -> acc * count }
}

private fun part2(input: List<Monkey>): Long {
    // println("Start up - $input")
    repeat(1000) {
        input.forEach { monkey ->
            monkey.operate(worried = true).forEach { (newMonkey, item) ->
                input[newMonkey].items.addAll(item)
            }
        }
        println("End of round $it - $input")
    }
    return input.map { it.inspectionCount }.sortedByDescending { it }.take(2).fold(1L) { acc, count -> acc * count }
}

private fun testInput()= listOf(
    Monkey(
        id = 0,
        items = mutableListOf(79L, 98L),
        inspection = { this * 19L },
        decision = { this % 23L == 0L },
        trueMonkeyIndex = 2,
        falseMonkeyIndex = 3
    ),

    Monkey(
        id = 1,
        items = mutableListOf(54L, 65L, 75L, 74L),
        inspection = { this + 6 },
        decision = { this % 19L == 0L },
        trueMonkeyIndex = 2,
        falseMonkeyIndex = 0
    ),

    Monkey(
        id = 2,
        items = mutableListOf(79L, 60L, 97L),
        inspection = { this * this },
        decision = { this % 13L == 0L },
        trueMonkeyIndex = 1,
        falseMonkeyIndex = 3
    ),

    Monkey(
        id = 3,
        items = mutableListOf(74L),
        inspection = { -> this + 3 },
        decision = { -> this % 17L == 0L },
        trueMonkeyIndex = 0,
        falseMonkeyIndex = 1
    ),
)

private data class Monkey(
    val id: Int,
    val items: MutableList<Long>,
    val inspection: Long.() -> Long,
    val decision: Long.() -> Boolean,
    val trueMonkeyIndex: Int,
    val falseMonkeyIndex: Int,
    var inspectionCount: Long = 0,
) {

    fun operate(worried: Boolean = false): Map<Int, MutableList<Long>> = buildMap {
        items.map {
            inspectionCount++
            val newValue = if (worried) it.inspection() else (it.inspection() / 3)
            val newMonkey = newValue.decide()
            this.getOrPut(newMonkey) { mutableListOf() }.add(newValue)
        }
        items.removeAll { true }
    }

    private fun Long.decide() = if (decision()) trueMonkeyIndex else falseMonkeyIndex

    override fun toString(): String {
        return "[Monkey(id=$id, inspectionCount=$inspectionCount, items=$items]"
    }
}

private fun realMonkeys() = listOf(
    Monkey(
        id = 0,
        items = mutableListOf(54L, 53L),
        inspection = { this * 3L },
        decision = { this % 2L == 0L },
        trueMonkeyIndex = 2,
        falseMonkeyIndex = 6
    ),

    Monkey(
        id = 1,
        items = mutableListOf(95L, 88L, 75L, 81L, 91L, 67L, 65L, 84L),
        inspection = { this * 11 },
        decision = { this % 7L == 0L },
        trueMonkeyIndex = 3,
        falseMonkeyIndex = 4
    ),

    Monkey(
        id = 2,
        items = mutableListOf(76, 81, 50, 93, 96, 81, 83),
        inspection = { this + 6 },
        decision = { this % 3L == 0L },
        trueMonkeyIndex = 5,
        falseMonkeyIndex = 1
    ),

    Monkey(
        id = 3,
        items = mutableListOf(83, 85, 85, 63),
        inspection = { -> this + 4 },
        decision = { -> this % 11L == 0L },
        trueMonkeyIndex = 7,
        falseMonkeyIndex = 4
    ),
    Monkey(
        id = 4,
        items = mutableListOf(85, 52, 64),
        inspection = { -> this + 8 },
        decision = { -> this % 17L == 0L },
        trueMonkeyIndex = 0,
        falseMonkeyIndex = 7
    ),
    Monkey(
        id = 5,
        items = mutableListOf(57L),
        inspection = { -> this + 2 },
        decision = { -> this % 5L == 0L },
        trueMonkeyIndex = 1,
        falseMonkeyIndex = 3
    ),
    Monkey(
        id = 6,
        items = mutableListOf(60, 95, 76, 66, 91),
        inspection = { -> this * this },
        decision = { -> this % 13L == 0L },
        trueMonkeyIndex = 2,
        falseMonkeyIndex = 5
    ),
    Monkey(
        id = 7,
        items = mutableListOf(65, 84, 76, 72, 79, 65),
        inspection = { -> this + 5 },
        decision = { -> this % 19L == 0L },
        trueMonkeyIndex = 6,
        falseMonkeyIndex = 0
    ),
)