package com.githubReport

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.concurrent.TimeUnit


class GitHubClient {

    private val httpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val objectMapper: ObjectMapper = jacksonObjectMapper()

    companion object {
        private const val GITHUB_API_BASE = "https://api.github.com"
        private const val PER_PAGE = 100
    }

    /**
     * Получает список репозиториев для заданного пользователя.
     * Поддерживает пагинацию — загружает до 100 репозиториев на одну страницу.
     *
     * @param username имя пользователя GitHub
     * @return список репозиториев
     * @throws GitHubException если пользователь не найден или произошла ошибка API
     */
    fun fetchRepositories(username: String): List<Repository> {
        val allRepos = mutableListOf<Repository>()
        var page = 1

        while (true) {
            val url = "$GITHUB_API_BASE/users/$username/repos?per_page=$PER_PAGE&page=$page&sort=updated"

            val request = Request.Builder()
                .url(url)
                .header("Accept", "application/vnd.github.v3+json")
                .header("User-Agent", "GitHubReportGenerator/1.0")
                .build()

            val response = try {
                httpClient.newCall(request).execute()
            } catch (e: IOException) {
                throw GitHubException("Ошибка подключения к GitHub API: ${e.message}", e)
            }

            response.use { resp ->
                when (resp.code) {
                    200 -> {
                        val body = resp.body?.string()
                            ?: throw GitHubException("Пустой ответ от API")

                        val repos: List<Repository> = objectMapper.readValue(body)

                        if (repos.isEmpty()) return allRepos

                        allRepos.addAll(repos)

                        // Если получили меньше PER_PAGE — больше страниц нет
                        if (repos.size < PER_PAGE) return allRepos

                        page++
                    }
                    404 -> throw GitHubException("Пользователь '$username' не найден на GitHub")
                    403 -> throw GitHubException("Превышен лимит запросов к GitHub API. Попробуйте позже.")
                    401 -> throw GitHubException("Ошибка авторизации. Проверьте токен доступа.")
                    else -> throw GitHubException("Неожиданный ответ API: HTTP ${resp.code}")
                }
            }
        }
    }

    /**
     * Вычисляет агрегированную статистику по списку репозиториев
     */
    fun calculateStats(username: String, repos: List<Repository>): UserStats {
        if (repos.isEmpty()) {
            return UserStats(
                username = username,
                totalRepos = 0,
                totalStars = 0,
                totalForks = 0,
                averageStars = 0.0,
                averageForks = 0.0,
                mostUsedLanguage = "N/A",
                topRepo = null
            )
        }

        val totalStars = repos.sumOf { it.stargazersCount }
        val totalForks = repos.sumOf { it.forksCount }
        val averageStars = totalStars.toDouble() / repos.size
        val averageForks = totalForks.toDouble() / repos.size

        val mostUsedLanguage = repos
            .mapNotNull { it.language }
            .groupingBy { it }
            .eachCount()
            .maxByOrNull { it.value }
            ?.key ?: "N/A"

        val topRepo = repos.maxByOrNull { it.stargazersCount }

        return UserStats(
            username = username,
            totalRepos = repos.size,
            totalStars = totalStars,
            totalForks = totalForks,
            averageStars = averageStars,
            averageForks = averageForks,
            mostUsedLanguage = mostUsedLanguage,
            topRepo = topRepo
        )
    }
}

/**
 * Исключение для ошибок GitHub API
 */
class GitHubException(message: String, cause: Throwable? = null) : Exception(message, cause)
