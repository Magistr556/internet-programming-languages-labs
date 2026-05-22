package com.githubReport

fun main() {
    printBanner()

    val client = GitHubClient()
    val reportGenerator = CsvReportGenerator()

    while (true) {
        val username = readUsername() ?: break

        try {
            println()
            print("Поиск репозиториев пользователя '$username'... ")

            val repos = client.fetchRepositories(username)

            if (repos.isEmpty()) {
                println("  У пользователя '$username' нет публичных репозиториев.\n")
            }

            println("${repos.size} репозиториев")

            // Вычисляение статистики
            val stats = client.calculateStats(username, repos)

            // статистика в консоль
            printStats(stats)

            // CSV-отчёт
            val filePath = reportGenerator.generateReport(username, repos, stats)
            println("Файл сохранён: $filePath")

        } catch (e: GitHubException) {
            println("✗")
            println()
            println("!!! Ошибка: ${e.message}")
        } catch (e: Exception) {
            println("✗")
            println()
            println("!!! Oшибка: ${e.message}")
        }
    }
}

/**
 * Читает имя пользователя из консоли с валидацией
 */
fun readUsername(): String? {
    while (true) {
        print("\n  Введите имя пользователя GitHub: ")
        val input = readLine()?.trim()

        when {
            input.isNullOrEmpty() -> println("!!! Имя пользователя не может быть пустым. Попробуйте снова.")
            input.equals("exit", ignoreCase = true) || input.equals("quit", ignoreCase = true) -> return null
            input.length > 39 -> println("!!! Имя пользователя слишком длинное (макс. 39 символов).")
            !input.matches(Regex("[a-zA-Z0-9-]+")) -> println("!!! Имя пользователя содержит недопустимые символы (допустимы: a-z, 0-9, -).")
            else -> return input
        }
    }
}

/**
 * Выводит агрегированную статистику в консоль
 */
fun printStats(stats: UserStats) {
    println()
    println("  ┌──────────────────────────────────────────────────┐")
    println("  │  Статистика: @${stats.username.padEnd(35)}│")
    println("  ├──────────────────────────────────────────────────┤")
    println("  │  Репозиториев:       ${stats.totalRepos.toString().padEnd(28)}│")
    println("  │  Всего звёзд:        ${stats.totalStars.toString().padEnd(28)}│")
    println("  │  Всего форков:       ${stats.totalForks.toString().padEnd(28)}│")
    println("  │  Среднее звёзд:      ${"%.2f".format(stats.averageStars).padEnd(28)}│")
    println("  │  Среднее форков:     ${"%.2f".format(stats.averageForks).padEnd(28)}│")
    println("  │  Основной язык:      ${stats.mostUsedLanguage.padEnd(28)}│")
    if (stats.topRepo != null) {
        val topRepoStr = "${stats.topRepo.name} (${stats.topRepo.stargazersCount})"
        println("  │  Топ репозиторий:    ${topRepoStr.take(28).padEnd(28)}│")
    }
    println("  └──────────────────────────────────────────────────┘")
}


fun printBanner() {
    println("  Для выхода введите 'exit' или 'quit'.")
}
