package com.todo.backend.dto

import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

// Request DTO — используется при создании новой задачи
data class TodoCreateRequest(
    @field:NotBlank(message = "Название задачи не может быть пустым")
    val name: String,
    val description: String = ""
)

// Request DTO — используется при обновлении задачи (все поля опциональны)
data class TodoUpdateRequest(
    val name: String? = null,
    val description: String? = null,
    val completed: Boolean? = null,
    val deleted: Boolean? = null
)

// Response DTO — возвращается клиенту
data class TodoResponse(
    val id: Long,
    val name: String,
    val description: String,
    val completed: Boolean,
    val deleted: Boolean,
    val createdAt: LocalDateTime
)
