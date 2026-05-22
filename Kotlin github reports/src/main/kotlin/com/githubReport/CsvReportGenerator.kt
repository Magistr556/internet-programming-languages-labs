package com.githubReport

import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Генератор CSV-отчетов по репозиториям GitHub
 * Формирует файл, корректно открываемый в Excel и других табличных редакторах
 */
class CsvReportGenerator {

    companion object {
        private val DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")

        // CSV-заголовки столбцов
        private val CSV_HEADERS = listOf(
            "Название репозитория",
            "Количество звезд",
            "Количество форков",
            "Язык программирования",
            "Дата последнего обновления"
        )
    }

    /**
     * Формирует и сохраняет CSV-отчет
     *
     * @param username   имя пользователя GitHub
     * @param repos      список репозиториев
     * @param stats      агрегированная статистика
     * @param outputDir  директория для сохранения файла (по умолчанию — текущая)
     * @return путь к созданному файлу
     */
    fun generateReport(
        username: String,
        repos: List<Repository>,
        stats: UserStats,
        outputDir: String = "."
    ): String {
        val timestamp = LocalDateTime.now().format(DATE_FORMATTER)
        val fileName = "github_report_${username}_$timestamp.csv"
        val filePath = "$outputDir${File.separator}$fileName"

        val outputFile = File(filePath)
        outputFile.parentFile?.mkdirs()

        PrintWriter(FileWriter(outputFile, Charsets.UTF_8)).use { writer ->
            // BOM для корректного отображения кириллицы в Excel
            writer.print('\uFEFF')

            // --- Метаданные отчёта ---
            writer.println("# GitHub Report Generator")
            writer.println("# Пользователь:,${escapeCsvField(username)}")
            writer.println("# Дата генерации:,${LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"))}")
            writer.println("# Всего репозиториев:,${stats.totalRepos}")
            writer.println()

            // --- Заголовок таблицы ---
            writer.println(CSV_HEADERS.joinToString(",") { escapeCsvField(it) })

            // --- Строки данных ---
            repos.forEach { repo ->
                val row = listOf(
                    repo.name,
                    repo.stargazersCount.toString(),
                    repo.forksCount.toString(),
                    repo.language ?: "N/A",
                    formatDate(repo.updatedAt)
                )
                writer.println(row.joinToString(",") { escapeCsvField(it) })
            }

            // --- Агрегированные показатели ---
            writer.println()
            writer.println("# АГРЕГИРОВАННАЯ СТАТИСТИКА")
            writer.println("Показатель,Значение")
            writer.println("Всего репозиториев,${stats.totalRepos}")
            writer.println("Суммарное количество звёзд,${stats.totalStars}")
            writer.println("Суммарное количество форков,${stats.totalForks}")
            writer.println("Среднее количество звёзд,${String.format("%.2f", stats.averageStars)}")
            writer.println("Среднее количество форков,${String.format("%.2f", stats.averageForks)}")
            writer.println("Наиболее используемый язык,${escapeCsvField(stats.mostUsedLanguage)}")
            if (stats.topRepo != null) {
                writer.println("Самый популярный репозиторий,${escapeCsvField(stats.topRepo.name)} (${stats.topRepo.stargazersCount} ⭐)")
            }
        }

        return filePath
    }

    /**
     * Экранирует спецсимволы в значении CSV-поля.
     * Если значение содержит запятую, кавычку или перевод строки — оборачивает в кавычки.
     */
    private fun escapeCsvField(value: String): String {
        return if (value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("#")) {
            "\"${value.replace("\"", "\"\"")}\""
        } else {
            value
        }
    }

    /**
     * Конвертирует дату из формата ISO 8601 в читаемый вид
     * Пример: "2024-03-15T10:30:00Z" → "15.03.2024 10:30"
     */
    private fun formatDate(isoDate: String): String {
        return try {
            val input = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
            val output = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
            val date = LocalDateTime.parse(isoDate, input)
            date.format(output)
        } catch (e: Exception) {
            isoDate // возвращаем оригинал при ошибке парсинга
        }
    }
}
