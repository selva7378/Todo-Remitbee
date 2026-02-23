package com.example.todo.presentation.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    var selectedTodo by remember { mutableStateOf<Todo?>(null) }
    var showDialog by remember { mutableStateOf(false) }


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    selectedTodo = null
                    showDialog = true
                }
            ) {
                Icon(Icons.Default.Add, null)
            }
        }
    ) { paddingValues ->
        when (todoState) {
            is TodoUiState.Loading -> CircularProgressIndicator()
            is TodoUiState.Success -> TodoListScreen(
                todos = (todoState as TodoUiState.Success).todos,
                onCardClick = { todo ->
                    selectedTodo = todo
                    showDialog = true
                },
                onCheckBoxChange = { todo, isChecked ->
                    viewModel.updateTodo(todo.copy(isCompleted = isChecked))
                },
                Modifier.padding(paddingValues)
            )
        }

        if (showDialog) {
            TodoDialog(
                todo = selectedTodo,
                onDismiss = { showDialog = false },
                onConfirm = { title ->

                    if (selectedTodo == null) {
                        viewModel.addTodo(title)
                    } else {
                        viewModel.updateTodo(selectedTodo!!.copy(title = title))
                    }
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun TodoListScreen(
    todos: List<Todo>,
    onCardClick: (Todo) -> Unit,
    onCheckBoxChange: (Todo, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(todos) { todo ->
            TodoCard(
                todo = todo,
                onCheckBoxChange = onCheckBoxChange,
                onClick = {
                    onCardClick(todo)
                }
            )
        }
    }
}


@Composable
fun TodoCard(
    todo: Todo,
    onCheckBoxChange: (Todo, Boolean) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = {
            onClick()
        },
        shape = RoundedCornerShape(8.dp),
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = todo.isCompleted,
                onCheckedChange = { isChecked ->
                    onCheckBoxChange(todo, isChecked)
                }
            )

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            Text(
                text = todo.title,
                style = MaterialTheme.typography.bodyLarge,
                textDecoration = if (todo.isCompleted)
                    TextDecoration.LineThrough
                else
                    TextDecoration.None,
                color = if (todo.isCompleted)
                    MaterialTheme.colorScheme.onSurfaceVariant
                else
                    MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f),
            )

        }
    }
}



@Preview(showBackground = true)
@Composable
fun TodoScreenPreview() {
    ToDoTheme {
        TodoScreen()
    }
}