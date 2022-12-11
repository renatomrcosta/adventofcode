package aoc2022.day11

fun main() {
    part1(testInput()).run { require(this == 10605L) { "Part1 error. Was $this" } }
    part1(realMonkeys()).run { println("Part1:  $this") }

    part2(testInput()).run { require(this == 2713310158L) { "Part2 error. Was $this" } }
    part2(realMonkeys()).run { println("Part2:  $this") }
}

private fun part1(input: List<Monkey>): Long {
    repeat(20) {
        input.forEach { monkey ->
            monkey.operate { this / 3 }.forEach { (newMonkey, item) ->
                input[newMonkey].items.addAll(item)
            }
        }
    }
    return input.map { it.inspectionCount }.sortedByDescending { it }.take(2).reduce { acc, count -> acc * count }
}

private fun part2(input: List<Monkey>): Long {
    // Turns out, if you have primes, their multiplication nabs you their LCM (Least common multiple)
    // By doing so, we can apply a modulo division in the worry level, without affecting its functionality, and not letting it balloon to infinity
    val mod = input.map { it.divisor }.reduce { acc, item -> acc * item }
    repeat(10_000) {
        input.forEach { monkey ->
            monkey.operate { this % mod }.forEach { (newMonkey, item) ->
                input[newMonkey].items.addAll(item)
            }
        }
    }
    return input.map { it.inspectionCount }.sortedByDescending { it }.take(2).reduce { acc, count -> acc * count }
}

private data class Monkey(
    val id: Int,
    val items: MutableList<Long>,
    val inspection: Long.() -> Long,
    val trueMonkeyIndex: Int,
    val falseMonkeyIndex: Int,
    var inspectionCount: Long = 0,
    val divisor: Long,
) {

    fun operate(worryReducer: Long.() -> Long): Map<Int, MutableList<Long>> = buildMap {
        items.map {
            inspectionCount++
            val newValue = it.inspection().worryReducer()
            val newMonkey = newValue.decide()
            this.getOrPut(newMonkey) { mutableListOf() }.add(newValue)
        }
        items.removeAll { true }
    }

    private fun Long.decide() = if (this % divisor == 0L) trueMonkeyIndex else falseMonkeyIndex

    override fun toString(): String {
        return "[Monkey(id=$id, inspectionCount=$inspectionCount]"
    }
}

private fun realMonkeys() = listOf(
    Monkey(
        id = 0,
        items = mutableListOf(54L, 53L),
        inspection = { this * 3L },
        divisor = 2L,
        trueMonkeyIndex = 2,
        falseMonkeyIndex = 6,
    ),

    Monkey(
        id = 1,
        items = mutableListOf(95L, 88L, 75L, 81L, 91L, 67L, 65L, 84L),
        inspection = { this * 11 },
        divisor = 7L,
        trueMonkeyIndex = 3,
        falseMonkeyIndex = 4,
    ),

    Monkey(
        id = 2,
        items = mutableListOf(76, 81, 50, 93, 96, 81, 83),
        inspection = { this + 6 },
        divisor = 3L,
        trueMonkeyIndex = 5,
        falseMonkeyIndex = 1,
    ),

    Monkey(
        id = 3,
        items = mutableListOf(83, 85, 85, 63),
        inspection = { -> this + 4 },
        divisor = 11L,
        trueMonkeyIndex = 7,
        falseMonkeyIndex = 4,
    ),
    Monkey(
        id = 4,
        items = mutableListOf(85, 52, 64),
        inspection = { -> this + 8 },
        divisor = 17L,
        trueMonkeyIndex = 0,
        falseMonkeyIndex = 7,
    ),
    Monkey(
        id = 5,
        items = mutableListOf(57L),
        inspection = { -> this + 2 },
        divisor = 5L,
        trueMonkeyIndex = 1,
        falseMonkeyIndex = 3,
    ),
    Monkey(
        id = 6,
        items = mutableListOf(60, 95, 76, 66, 91),
        inspection = { -> this * this },
        divisor = 13L,
        trueMonkeyIndex = 2,
        falseMonkeyIndex = 5,
    ),
    Monkey(
        id = 7,
        items = mutableListOf(65, 84, 76, 72, 79, 65),
        inspection = { -> this + 5 },
        divisor = 19L,
        trueMonkeyIndex = 6,
        falseMonkeyIndex = 0,
    ),
)

private fun testInput() = listOf(
    Monkey(
        id = 0,
        items = mutableListOf(79L, 98L),
        inspection = { this * 19L },
        divisor = 23L,
        trueMonkeyIndex = 2,
        falseMonkeyIndex = 3
    ),

    Monkey(
        id = 1,
        items = mutableListOf(54L, 65L, 75L, 74L),
        inspection = { this + 6 },
        divisor = 19L,
        trueMonkeyIndex = 2,
        falseMonkeyIndex = 0
    ),

    Monkey(
        id = 2,
        items = mutableListOf(79L, 60L, 97L),
        inspection = { this * this },
        divisor = 13L,
        trueMonkeyIndex = 1,
        falseMonkeyIndex = 3
    ),

    Monkey(
        id = 3,
        items = mutableListOf(74L),
        inspection = { -> this + 3 },
        divisor = 17L,
        trueMonkeyIndex = 0,
        falseMonkeyIndex = 1
    ),
)