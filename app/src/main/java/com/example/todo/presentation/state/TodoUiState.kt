package com.example.todo.presentation.state

import com.example.todo.domain.model.Todo


sealed class TodoUiState {
    object Loading : TodoUiState()
    data class Success(val todos: List<Todo>) : TodoUiState()
}