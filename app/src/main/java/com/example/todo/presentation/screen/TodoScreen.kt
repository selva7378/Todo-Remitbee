package com.example.todo.presentation.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.todo.domain.model.Todo
import com.example.todo.presentation.state.TodoUiState
import com.example.todo.presentation.theme.ToDoTheme
import com.example.todo.presentation.viewmodel.TodoViewModel


@Composable
fun TodoScreen(
    viewModel: TodoViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val todoState by viewModel.uiState.collectAsState()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { }
            ) {
                Icon(Icons.Default.Add, null)
            }
        }
    ) { paddingValues ->
        when (todoState) {
            is TodoUiState.Loading -> CircularProgressIndicator()
            is TodoUiState.Success -> TodoListScreen((todoState as TodoUiState.Success).todos,
                Modifier.padding(paddingValues))
        }
    }
}

@Composable
fun TodoListScreen(
    todos: List<Todo>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(todos) { todo ->
            TodoItem(todo = todo)
        }
    }
}

@Composable
fun TodoItem(
    todo: Todo,
    modifier: Modifier = Modifier
) {

}


@Preview(showBackground = true)
@Composable
fun TodoScreenPreview() {
    ToDoTheme {
        TodoScreen()
    }
}