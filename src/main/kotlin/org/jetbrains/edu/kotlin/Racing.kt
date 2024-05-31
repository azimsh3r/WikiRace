package org.jetbrains.edu.kotlin

import org.jetbrains.edu.kotlin.wikirace.WikiPath
import org.jetbrains.edu.kotlin.wikirace.WikiRacer
import org.jsoup.Jsoup
import java.util.concurrent.*

class Racing (private val maxThreads: Int) : WikiRacer {
    override fun race(startPage: String, destinationPage: String, searchDepth: Int): WikiPath {
        val start = WikiPath(listOf(startPage), 0)
        val visitedSet = ConcurrentHashMap.newKeySet<String>()
        val queue = ConcurrentLinkedDeque<WikiPath>()

        visitedSet.add(startPage)
        queue.addLast(start)

        val executor = Executors.newFixedThreadPool(maxThreads)
        val semaphore = Semaphore(maxThreads)
        val futures = mutableListOf<Future<*>>()

        try {
            while (queue.isNotEmpty()) {
                val currentNode = queue.removeFirst()

                if (currentNode.path.last() == destinationPage) {
                    return currentNode
                }

                if (currentNode.steps >= searchDepth)
                    continue

                val references = getReferences(currentNode.path.last())

                references.forEach { adj ->
                    if (!visitedSet.contains(adj)) {
                        semaphore.acquire()

                        futures.add(executor.submit {
                            try {
                                if (visitedSet.add(adj)) {
                                    val childPath = currentNode.path.toMutableList()
                                    childPath.add(adj)
                                    queue.addLast(WikiPath(childPath, currentNode.steps + 1))
                                }
                            } finally {
                                semaphore.release()
                            }
                        })
                    }
                }
                futures.forEach { it.get() }
                futures.clear()
            }
        } finally {
            executor.shutdown()
            executor.awaitTermination(10, TimeUnit.SECONDS)
        }

        return WikiPath.NOT_FOUND
    }
    override fun getReferences(page: String): List<String> {
        return try {
            val list = Jsoup.connect(page)
                .get()
                .select("[href^=/wiki/]")
                .asSequence()
                .map { it.attr("href") }
                .filterNot {it.contains(Regex("(?<=/wiki/)(?!(Wikipedia:|File:|Template:|Help:|Category:|Special:|Portal:|User:|MediaWiki:|Draft:|TimedText:|Module:|Media:|Template_talk:|Talk:):)[^:]+(?=:)"))}
                .map {"https://en.wikipedia.org${it}"}
                .toList()
            val set: MutableSet<String> = mutableSetOf()
            list.map { set.add(it) }

            set.remove("https://en.wikipedia.org/wiki/Main_Page")
            set.remove(page)
            set.toList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}

fun main () {
    val race = Racing(1)
    val list = race.getReferences("https://en.wikipedia.org/wiki/AVL_tree")
    list.map {println(it)}
}
