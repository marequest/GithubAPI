package service

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.example.models.GithubRepositories
import org.example.models.RepositoryFiles

/**
 * Service for communicating with GitHub API.
 * @author Marko Jovicic
 */
class GitHubService(private val client: HttpClient) {

    /**
     * @return All repos containing Java language.
     */
    suspend fun searchForJavaRepositories(): GithubRepositories {
        return client.get("https://api.github.com/search/repositories?q=language:java").body()
    }

    /**
     * @return Map of language name and used percentage.
     */
    suspend fun fetchLanguagePercentages(fullName: String): Map<String, Double> {
        val languages: Map<String, Int> = client.get("https://api.github.com/repos/$fullName/languages").body()
        val totalBytesCount = languages.values.sum()
        return languages.mapValues { (_, bytesCount) -> (bytesCount.toDouble() / totalBytesCount) * 100 }
    }

    /**
     * @return All files in repository.
     */
    suspend fun fetchFilesInRepository(fullName: String, branch: String): RepositoryFiles {
        return client.get("https://api.github.com/repos/$fullName/git/trees/$branch?recursive=1").body()
    }
}
