package org.jetbrains.edu.kotlin.wikirace

/**
 * @property path The list of all links followed in the search path, including the destination page.
 */
data class WikiPath(val path: List<String>, var steps: Int) {
    companion object {
        val NOT_FOUND = WikiPath(emptyList(), 0)
    }
}
