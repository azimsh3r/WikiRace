package org.jetbrains.edu.kotlin

val forbiddenPrefixes = listOf(
    "File:",
    "Wikipedia:",
    "Help:",
    "Template:",
    "Category:",
    "Special:",
    "Portal:",
    "User:",
    "MediaWiki:",
    "Draft:",
    "TimedText:",
    "Module:",
    "Media:",
    "Template_talk:",
    "Talk:"
)

fun main(args: Array<String>) = Cli().run()
