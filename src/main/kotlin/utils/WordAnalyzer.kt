package org.example.utils

object WordAnalyzer {
    fun findTopNWords(wordFrequency: Map<String, Int>, topN: Int = 10): List<Pair<String, Int>> {
        return wordFrequency.entries
            .sortedByDescending { it.value }
            .take(topN)
            .map { it.key to it.value }
    }

    fun consoleWriteTopNWords(topWords: List<Pair<String, Int>>, totalWords: Int) {
        if (topWords.isEmpty()) {
            return
        }
        topWords.forEach { (word, count) ->
            val percentage = (count.toDouble() / totalWords) * 100
            println("${word.capitalize()}: $count (Popularity: ${String.format("%.2f", percentage)}%)")
        }
    }

}