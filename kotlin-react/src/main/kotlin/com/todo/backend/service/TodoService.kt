package com.todo.backend.service

import com.todo.backend.dto.TodoCreateRequest
import com.todo.backend.dto.TodoResponse
import com.todo.backend.dto.TodoUpdateRequest
import com.todo.backend.model.Todo
import com.todo.backend.repository.TodoRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class TodoService(private val todoRepository: TodoRepository) {

    // Получить все задачи
    fun getAll(): List<TodoResponse> =
        todoRepository.findAll().map { it.toResponse() }

    // Получить одну задачу по ID
    fun getById(id: Long): TodoResponse =
        findOrThrow(id).toResponse()

    // Создать новую задачу
    fun create(request: TodoCreateRequest): TodoResponse {
        val todo = Todo(
            name = request.name.trim(),
            description = request.description.trim()
        )
        return todoRepository.save(todo).toResponse()
    }

    // Обновить задачу (PATCH — обновляются только переданные поля)
    fun update(id: Long, request: TodoUpdateRequest): TodoResponse {
        val todo = findOrThrow(id)

        request.name?.let { todo.name = it.trim() }
        request.description?.let { todo.description = it.trim() }
        request.completed?.let { todo.completed = it }
        request.deleted?.let { todo.deleted = it }

        return todoRepository.save(todo).toResponse()
    }

    // Удалить задачу физически из БД
    fun delete(id: Long) {
        findOrThrow(id) // проверяем, что существует
        todoRepository.deleteById(id)
    }

    // Удалить все задачи
    fun deleteAll() = todoRepository.deleteAll()

    // ---- helpers ----

    private fun findOrThrow(id: Long): Todo =
        todoRepository.findByIdOrNull(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Задача с id=$id не найдена")

    private fun Todo.toResponse() = TodoResponse(
        id = id,
        name = name,
        description = description,
        completed = completed,
        deleted = deleted,
        createdAt = createdAt
    )
}
