import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.example.utils.ClassAnalyzer
import org.example.utils.WordAnalyzer
import service.GitHubService

/**
 * You can put your classic github token here to avoid rate limiting.
 */
val token = ""

suspend fun main() {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        if (token.isNotEmpty()) {
            defaultRequest {
                header("Authorization", "Bearer $token")
            }
        }
    }
    val githubService = GitHubService(client)

    val allWordsFrequency = mutableMapOf<String, Int>()

    /**
     * You can tweak this value to receive more precise answer.
     */
    val topN = 12

    val response = githubService.searchForJavaRepositories()
    if (response.items.isNotEmpty()) {
        response.items.take(topN).forEach { repo ->
            val repoLanguagePercentages = githubService.fetchLanguagePercentages(repo.full_name)

            val mostUsedLanguage = repoLanguagePercentages.maxByOrNull { it.value }
            if (mostUsedLanguage?.key.equals("Java", ignoreCase = true)) {
                val branch = repo.default_branch ?: "master"
                println("Looking for .java files in https://api.github.com/repos/${repo.full_name}/git/trees/$branch?recursive=1")

                val repositoryData = githubService.fetchFilesInRepository(repo.full_name, branch)

                val javaFiles = repositoryData.tree.filter { it.type == "blob" && it.path.endsWith(".java") }
                    .map { it.path.substringAfterLast('/').substringBeforeLast('.') }

                val wordFrequency = ClassAnalyzer.getClassNamesFromPath(javaFiles)

                wordFrequency.forEach { (word, count) ->
                    allWordsFrequency[word] = allWordsFrequency.getOrDefault(word, 0) + count
                }

            } else {
                println("Skipping repository: ${repo.full_name} (Java is not the most used language)")
            }
        }
    } else {
        println("Failed response!")
    }

    println("\nPopularity of top words in class names in Java-based projects:")
    val totalCumulativeWords = allWordsFrequency.values.sum()
    val cumulativeTopWords = WordAnalyzer.findTopNWords(allWordsFrequency, 15)
    WordAnalyzer.consoleWriteTopNWords(cumulativeTopWords, totalCumulativeWords)

    println("\nYou have good chances to name your class correctly with word: ${cumulativeTopWords.first().first.capitalize()} :)")


    client.close()
}
