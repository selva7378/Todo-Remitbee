package com.example.todo.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

     var title by   mutableStateOf("")
         private set

    private val _uiState = MutableStateFlow<TodoUiState>(TodoUiState.Loading)
    val uiState = _uiState.asStateFlow()

    var selectedTodo by mutableStateOf<Todo?>(null)
        private set

    var showDialog by  mutableStateOf(false)
        private  set

    var isError by  mutableStateOf(false)
        private set

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

    fun onAddClick() {
        selectedTodo = null
        showDialog = true
    }

    fun dismissDialog() {
        showDialog = false
        selectedTodo = null
        title = ""
    }

    fun onTodoClick(todo: Todo) {
        selectedTodo = todo
        showDialog = true
        title = todo.title
    }

    fun saveTodo(title: String) {
        val currentTodo = selectedTodo
        if(currentTodo == null) {
            addTodo(title)
        } else {
            updateTodo(currentTodo.copy(title = title))
        }
        dismissDialog()
    }

    fun updateErrorStatus(bool: Boolean) {
        isError = bool
    }

    fun onTitleChange(title: String) {
        this.title = title
    }


}