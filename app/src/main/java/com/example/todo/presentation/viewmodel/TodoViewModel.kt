package com.example.todo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.domain.model.Todo
import com.example.todo.domain.repository.TodoRepository
import com.example.todo.presentation.state.TodoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TodoViewModel @Inject constructor(
    private val todoRepository: TodoRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<TodoUiState>(TodoUiState.Loading)
    val uiState = _uiState.asStateFlow()


    init {
        viewModelScope.launch {
            todoRepository.getTodos().collect { todos ->
                _uiState.value = TodoUiState.Success(todos)
            }
        }
    }

    fun addTodo(title: String) {
        viewModelScope.launch {
            todoRepository.insert(Todo(title = title, isCompleted = false))
        }
    }

    fun updateTodo(todo: Todo) {
        viewModelScope.launch {
            todoRepository.update(todo)

        }
    }

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            todoRepository.delete(todo)
        }
    }


}