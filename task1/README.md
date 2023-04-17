### Task #1 (15 points)

You probably know about the game “Wikipedia race”, where from a random wiki page you need to find the page,
e.g. the [Kotlin](https://en.wikipedia.org/wiki/Kotlin_(programming_language)) page with the minimum number of clicks. 
You can click on any links on the page.
It is interesting to play this game, but it is even more interesting to win! 
In this task, you need to implement a breadth-first search to find the shortest path between pages.

Your solution should be a console app with the following arguments:
- Search depth - how many transitions from the start page are accepted. Don't forget to check if the depth > 0 and inform user about an incorrect input.
- Number of processors - how many processors to use (the default value is one). Don't forget to check if the number of processors > 0 and inform user about an incorrect input.
- The optional title of the article - if passed, then start searching from this page, otherwise from a random one, e.g. for the title `Comparison of programming languages` the start page will be `https://en.wikipedia.org/wiki/Comparison_of_programming_languages`. You don't need to generate random page names to start, just take a random element from a pre-prepared list with the articles names.

Please, use the [Clikt](https://ajalt.github.io/clikt/) or any other library to handle the command-line arguments.

The output of the app is a sequence of pages in the found path or a message that no path was 
found with the search depth. 
Consider also displaying progress so that the user knows that the application has not frozen.

To extract links from a wiki page, you need to extract all internal (refer to any wiki page) links that are inside the `a` tag (`html.select("[href^=/wiki/]").map { it.attr("href") }`). To make the search faster, you need to exclude the following links when parsing:
```kotlin
val forbiddenTemplates = listOf(
    "File:",
    "Wikipedia:",
    "Help:",
    "Template:",
    "Category:",
    "Special:",
    "Portal:",
    "User:",
    "MediaWiki:",
    "Draft",
    "TimedText",
    "Module:",
    "Media:",
    "Template_talk:",
    "Talk:"
)
```
If at least one of the elements of this list is in the final list with the references, this link should not be taken into account.

The goal page for this task is the Kotlin page that is stored in the `KOTLIN_PAGE` constant. If the user starts the search from this page, the depth of the final path is 0. If the user starts with a page which refers to the Kotlin page, the epth of the final path is 1, etc. You can find several examples in the tests.

For this simple version, you can get 10 points. 
If you would like to get all 15 points, you need to use a multi-threaded search 
(in this case, you just need to find a path as soon as possible, not necessarily the shortest one).
