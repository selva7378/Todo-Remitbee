package com.example.todo.presentation.screen

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.todo.domain.model.Todo
import com.example.todo.presentation.state.TodoUiState
import com.example.todo.presentation.theme.TodoTheme
import com.example.todo.presentation.viewmodel.TodoViewModel


@Composable
fun TodoScreen(
    viewModel: TodoViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val todoState by viewModel.uiState.collectAsState()

    val context = LocalContext.current

    val notificationPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                viewModel.showTimePicker()
            }
        }


    Scaffold(
        floatingActionButton = {
            FloatingActionMenu(
                onReminderClick = {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                        when {
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) == PackageManager.PERMISSION_GRANTED -> {

                                viewModel.showTimePicker()
                            }

                            ActivityCompat.shouldShowRequestPermissionRationale(
                                context as Activity,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) -> {
                                // Show rationale dialog
                                viewModel.showPermissionDialog()
                            }

                            else -> {
                                notificationPermissionLauncher.launch(
                                    Manifest.permission.POST_NOTIFICATIONS
                                )
                            }
                        }

                    } else {
                        viewModel.showTimePicker()
                    }
                },
                onAddClick = { viewModel.onAddClick() }
            )
        }
    ) { paddingValues ->
        when (todoState) {
            is TodoUiState.Loading -> CircularProgressIndicator()
            is TodoUiState.Success -> TodoListScreen(
                todos = (todoState as TodoUiState.Success).todos,
                onCardClick = { todo ->
                    viewModel.onTodoClick(todo)
                },
                onCheckBoxChange = { todo, isChecked ->
                    viewModel.updateTodo(todo.copy(isCompleted = isChecked))
                },
                onDelete = { todo ->
                    viewModel.deleteTodo(todo)
                },
                Modifier.padding(paddingValues)
            )
        }

        if (viewModel.showDialog) {
            TodoDialog(
                title = viewModel.title,
                isError = viewModel.isError,
                onTitleChange = { title ->
                    viewModel.onTitleChange(title)
                },
                onErrorChange = { isError -> viewModel.updateErrorStatus(isError) },
                todo = viewModel.selectedTodo,
                onDismiss = { viewModel.dismissDialog() },
                onConfirm = { title ->
                    viewModel.saveTodo(title)
                }
            )
        }

        if (viewModel.showTimePicker) {
            DialExample(
                onConfirm = { viewModel.dismissTimePicker() },
                onDismiss = { viewModel.dismissTimePicker() }
            )
        }

        if (viewModel.showPermissionDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissPermissionDialog() },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.dismissPermissionDialog()
                            openAppSettings(context)
                        }
                    ) {
                        Text("Open Settings")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { viewModel.dismissPermissionDialog() }
                    ) {
                        Text("Cancel")
                    }
                },
                title = { Text("Notification Permission Required") },
                text = {
                    Text("To set reminders, notification permission is required. Please enable it in settings.")
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
    onDelete: (Todo) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(
            bottom = 80.dp,
        ),
        modifier = modifier,
    ) {
        items(todos) { todo ->
            TodoCard(
                todo = todo,
                onCheckBoxChange = onCheckBoxChange,
                onCardClick = {
                    onCardClick(todo)
                },
                onDelete = {
                    onDelete(todo)
                },
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}


@Composable
fun TodoCard(
    todo: Todo,
    onCheckBoxChange: (Todo, Boolean) -> Unit,
    onCardClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onCardClick,
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

            IconButton(
                onClick = onDelete
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Todo",
                    tint = MaterialTheme.colorScheme.error
                )
            }


        }
    }
}

@Composable
fun FloatingActionMenu(
    onReminderClick: () -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Column()
    {
        if (expanded) {

            SmallFloatingActionButton(
                onClick = {
                    expanded = false
                    onReminderClick()
                }

            ) {
                Icon(Icons.Default.Alarm, "Set Alarm")

            }

            Spacer(Modifier.height(8.dp))

            SmallFloatingActionButton(
                onClick = {
                    expanded = false
                    onAddClick()
                }

            ) {
                Icon(Icons.Default.Add, "Add Todo")

            }

        }

        Spacer(Modifier.height(8.dp))

        FloatingActionButton(
            onClick = { expanded = !expanded }
        ) {
            Icon(
                imageVector = if (expanded) Icons.Default.Close else Icons.Default.Add,
                contentDescription = if (expanded) "Close" else "Add"
            )
        }
    }
}

fun openAppSettings(context: Context) {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", context.packageName, null)
    )
    context.startActivity(intent)
}


@Preview(showBackground = true)
@Composable
fun TodoScreenPreview() {
    TodoTheme {
        TodoScreen()
    }
}