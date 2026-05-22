package com.todo.backend.controller

import com.todo.backend.dto.TodoCreateRequest
import com.todo.backend.dto.TodoResponse
import com.todo.backend.dto.TodoUpdateRequest
import com.todo.backend.service.TodoService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/todos")
@CrossOrigin(origins = ["http://localhost:3000"]) // разрешаем запросы от React dev server
class TodoController(private val todoService: TodoService) {

    // GET /todos — получить все задачи
    @GetMapping
    fun getAll(): ResponseEntity<List<TodoResponse>> =
        ResponseEntity.ok(todoService.getAll())

    // GET /todos/{id} — получить одну задачу
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<TodoResponse> =
        ResponseEntity.ok(todoService.getById(id))

    // POST /todos — создать задачу
    @PostMapping
    fun create(@Valid @RequestBody request: TodoCreateRequest): ResponseEntity<TodoResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(todoService.create(request))

    // PATCH /todos/{id} — частично обновить задачу
    @PatchMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody request: TodoUpdateRequest
    ): ResponseEntity<TodoResponse> =
        ResponseEntity.ok(todoService.update(id, request))

    // DELETE /todos/{id} — удалить одну задачу
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        todoService.delete(id)
        return ResponseEntity.noContent().build()
    }

    // DELETE /todos — удалить все задачи
    @DeleteMapping
    fun deleteAll(): ResponseEntity<Void> {
        todoService.deleteAll()
        return ResponseEntity.noContent().build()
    }
}
