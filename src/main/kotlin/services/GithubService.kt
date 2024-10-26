package service

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.example.models.GitHubRepo
import org.example.models.TreeResponse

class GitHubService(private val client: HttpClient) {

    suspend fun searchForJavaRepositories(): GitHubRepo {
        return client.get("https://api.github.com/search/repositories?q=language:java").body()
    }

    suspend fun fetchLanguagePercentages(fullName: String): Map<String, Double> {
        val languages: Map<String, Int> = client.get("https://api.github.com/repos/$fullName/languages").body()
        val totalBytesCount = languages.values.sum()
        return languages.mapValues { (language, bytesCount) -> (bytesCount.toDouble() / totalBytesCount) * 100 }
    }

    suspend fun fetchFilesInRepository(fullName: String, branch: String): TreeResponse {
        return client.get("https://api.github.com/repos/$fullName/git/trees/$branch?recursive=1").body()
    }
}
