package com.example.todo.data.repository

import com.example.todo.data.local.dao.TodoDao
import com.example.todo.data.mapper.toDomain
import com.example.todo.data.mapper.toEntity
import com.example.todo.domain.model.Todo
import com.example.todo.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val dao: TodoDao
) : TodoRepository {
    override fun getTodos(): Flow<List<Todo>> {
        return dao.getTodos().map { list ->
            list.map { it.toDomain() }
        }
    }


    override suspend fun insert(todo: Todo) {
        dao.insert(todo.toEntity())
    }

    override suspend fun update(todo: Todo) {
        dao.update(todo.toEntity())
    }

    override suspend fun delete(todo: Todo) {
        dao.delete(todo.toEntity())
    }
}