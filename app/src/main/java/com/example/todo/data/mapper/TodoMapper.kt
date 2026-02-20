package com.example.todo.data.mapper

import com.example.todo.data.local.entity.TodoEntity
import com.example.todo.domain.model.Todo

fun TodoEntity.toDomain(): Todo =
    Todo(
        id,
        title,
        isCompleted
    )

fun Todo.toEntity(): TodoEntity =
    TodoEntity(
        id,
        title,
        isCompleted
    )