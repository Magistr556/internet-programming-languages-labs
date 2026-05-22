package com.githubReport

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty


@JsonIgnoreProperties(ignoreUnknown = true)
data class Repository(
    @JsonProperty("name")
    val name: String,

    @JsonProperty("stargazers_count")
    val stargazersCount: Int,

    @JsonProperty("forks_count")
    val forksCount: Int,

    @JsonProperty("language")
    val language: String?,

    @JsonProperty("updated_at")
    val updatedAt: String
)

/**
 * Агрегированная статистика по всем репозиториям пользователя
 */
data class UserStats(
    val username: String,
    val totalRepos: Int,
    val totalStars: Int,
    val totalForks: Int,
    val averageStars: Double,
    val averageForks: Double,
    val mostUsedLanguage: String,
    val topRepo: Repository?
)
