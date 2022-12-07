package aoc2022.day7

import aoc2022.readFile
import aoc2022.splitOnLineBreaks

private val testInput = """
    ${'$'} cd /
    ${'$'} ls
    dir a
    14848514 b.txt
    8504156 c.dat
    dir d
    ${'$'} cd a
    ${'$'} ls
    dir a
    29116 f
    2557 g
    62596 h.lst
    ${'$'} cd a
    ${'$'} ls
    584 i
    ${'$'} cd ..
    ${'$'} cd ..
    ${'$'} cd d
    ${'$'} ls
    4060174 j
    8033020 d.log
    5626152 d.ext
    7214296 k
""".trimIndent()

fun main() {
    val input = readFile("day7.txt")
    part1(testInput).run { require(this == 95437L) }
    part1(input).run { println("Part1 $this") }
}

fun part1(input: String): Long {
    val tree = input.parseInput()
    println(tree)
    return tree.findAllDirSizes().also { println(it) }.filter { it.second < 100_000 }.sumOf { it.second }
}

private fun String.parseInput(): NodeTree<Item> {
    val tree: NodeTree<Item> = NodeTree(value = Folder(name = "root"))
    var activeNode: NodeTree<Item> = tree

    this.splitOnLineBreaks()
        .drop(1) // drop root, hardcoded above
        .forEach { line ->
            println("Reading line $line")
            val args = line.split(" ")
            if (args.first() != "$") {
                // Filename to size
                if (args.first() == "dir") {
                    activeNode.children.add(NodeTree(value = Folder(name = args.last()), parentNode = activeNode))
                } else {
                    activeNode.children.add(
                        NodeTree(
                            value = File(name = args.last(), size = args.first().toLong()),
                            parentNode = activeNode
                        )
                    )
                }
            } else {
                // Command
                when (args[1]) {
                    "cd" -> {
                        if (args.last() == "..") {
                            // go back to the previous group}
                            activeNode = activeNode.parentNode ?: error("oopsie $args")
                        } else {
                            // add value to stack
                            activeNode = activeNode.children.find { (it.value as? Folder)?.name == args.last() }
                                ?: error("folder not found $args")
                        }
                    }

                    "ls" -> println("just lists the thing. ignored")
                }
            }
        }
    return tree
}

private fun NodeTree<Item>.findAllDirSizes(): List<Pair<Folder, Long>> = buildList {
    this@findAllDirSizes.preOrderTraversal { node ->
        if (node.value is Folder) {
            val size = node.calculateSize()
            add(node.value to size)
        }
    }
}

private data class NodeTree<T>(
    val value: T,
    val children: MutableList<NodeTree<T>> = mutableListOf(),
    val parentNode: NodeTree<Item>? = null,
) {
    override fun toString(): String {
        return "Nodes: $value | children: $children"
    }

    fun preOrderTraversal(action: (NodeTree<T>) -> Any) {
        action(this)
        children.forEach { it.preOrderTraversal(action) }
    }

    fun postOrderTraversal(action: (NodeTree<T>) -> Any) {
        children.forEach { it.postOrderTraversal(action) }
        action(this)
    }
}

private fun NodeTree<Item>.calculateSize(): Long = when (value) {
    is File -> value.size
    is Folder -> this.children.sumOf { it.calculateSize() }
}

private sealed class Item(open val name: String)
private data class Folder(override val name: String) : Item(name = name)
private data class File(override val name: String, val size: Long) : Item(name = name)
