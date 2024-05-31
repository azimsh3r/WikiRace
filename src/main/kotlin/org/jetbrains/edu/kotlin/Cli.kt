package org.jetbrains.edu.kotlin

class Cli {
    private var searchDepth: Int = 0
    private var maxThreads: Int = 1
    private var start: String? = null
    private var final: String? = null

    fun run() {
        while (searchDepth < 1) {
            print("Depth of search: ")
            searchDepth = readln().toInt()
            if (searchDepth < 1) {
                println("The value is invalid")
            }
        }

        while (maxThreads < 1) {
            print("Max number of threads: ")
            maxThreads = readlnOrNull()?.toInt() ?: maxThreads
            if (maxThreads < 1) {
                println("The value is invalid")
            }
        }

        println("Start: ")
        start = readlnOrNull() ?: "any article here"

        println("Final: ")
        final = readlnOrNull() ?: "any article here"

        val racing = Racing(maxThreads)

        racing.race(start!!, final!!, searchDepth)
    }
}
