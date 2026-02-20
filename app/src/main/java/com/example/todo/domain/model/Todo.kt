package com.example.todo.domain.model

data class Todo(
    val id: Int = 0,
    val title: String,
    val isCompleted: Boolean
)