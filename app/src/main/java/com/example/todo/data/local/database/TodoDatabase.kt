package com.example.todo.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todo.data.local.dao.TodoDao
import com.example.todo.data.local.entity.TodoEntity


@Database(
    entities = [TodoEntity::class],
    version = 1,
    exportSchema = true
)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}