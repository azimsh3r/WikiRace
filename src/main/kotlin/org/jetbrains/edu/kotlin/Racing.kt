package org.jetbrains.edu.kotlin

import org.jetbrains.edu.kotlin.wikirace.WikiPath
import org.jetbrains.edu.kotlin.wikirace.WikiRacer
import org.jsoup.Jsoup
import java.io.IOException
import java.util.concurrent.*

class Racing(private val maxThreads: Int) : WikiRacer {
    override fun race(startPage: String, destinationPage: String, searchDepth: Int): WikiPath {
        val visitedSet = ConcurrentHashMap.newKeySet<String>()
        val queue = ConcurrentLinkedDeque<WikiPath>()

        visitedSet.add(startPage)
        queue.addLast(WikiPath(listOf(startPage), 0))

        val executor = Executors.newFixedThreadPool(maxThreads)
        val semaphore = Semaphore(maxThreads)
        val futures = mutableListOf<Future<*>>()

        while (queue.isNotEmpty()) {
            val currentNode = queue.removeFirst()

            if (currentNode.path.last() == destinationPage) {
                return currentNode
            } else if (currentNode.steps >= searchDepth) {
                continue
            }

            val references = getReferences(currentNode.path.last())

            references.forEach { adj ->
                if (!visitedSet.contains(adj)) {
                    semaphore.acquire()
                    futures.add(executor.submit {
                        if (visitedSet.add(adj)) {
                            val childPath = currentNode.path.toMutableList()
                            childPath.add(adj)
                            queue.addLast(WikiPath(childPath, currentNode.steps + 1))
                        }
                        semaphore.release()
                    })
                }
            }

            futures.forEach { it.get() }
            futures.clear()
        }
        executor.shutdown()

        return WikiPath.NOT_FOUND
    }

    override fun getReferences(page: String): List<String> = try {
        val list = Jsoup.connect(page)
            .get()
            .select("[href^=/wiki/]")
            .asSequence()
            .map { it.attr("href") }
            .filterNot {
                it.contains(Regex("(?<=/wiki/)(?!(${forbiddenPrefixes.joinToString("|")}):)[^:]+(?=:)"))
            }
            .map { "https://en.wikipedia.org$it" }
            .toList()
        val set: MutableSet<String> = mutableSetOf()
        list.map { set.add(it) }

        set.remove("https://en.wikipedia.org/wiki/Main_Page")
        set.remove(page)
        set.toList()
    } catch (e: IOException) {
        println("Network error: ${e.message}")
        emptyList()
    } catch (e: IllegalArgumentException) {
        println("Invalid argument: ${e.message}")
        emptyList()
    }
}

fun main() {
    val race = Racing(1)
    val list = race.getReferences("https://en.wikipedia.org/wiki/AVL_tree")
    list.map { println(it) }
}
