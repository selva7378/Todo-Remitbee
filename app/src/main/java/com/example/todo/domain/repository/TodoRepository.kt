package com.example.todo.domain.repository

import com.example.todo.domain.model.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository{

    fun getTodos(): Flow<List<Todo>>
    suspend fun insert(todo: Todo)
    suspend fun update(todo: Todo)
    suspend fun delete(todo: Todo)

}