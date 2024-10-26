package org.example.utils

object ClassAnalyzer {
    fun getClassNamesFromPath(classNames: List<String>): Map<String, Int> {
        val wordFrequency = mutableMapOf<String, Int>()

        classNames.forEach { className ->
            val words = className.split(Regex("(?=[A-Z])|_")).filter { it.isNotBlank() }
            words.forEach { word ->
                val lowerCaseWord = word.lowercase()
                wordFrequency[lowerCaseWord] = wordFrequency.getOrDefault(lowerCaseWord, 0) + 1
            }
        }

        return wordFrequency
    }
}