package com.example.todo.di

import android.content.Context
import androidx.room.Room
import com.example.todo.data.local.dao.TodoDao
import com.example.todo.data.local.database.TodoDatabase
import com.example.todo.data.repository.TodoRepositoryImpl
import com.example.todo.domain.repository.TodoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideTodoDatabase(
        @ApplicationContext context: Context
    ): TodoDatabase =
        Room.databaseBuilder(
            context,
            TodoDatabase::class.java,
            "todo_db"
        ).build()

    @Provides
    fun provideTodoDao(
        todoDatabase: TodoDatabase
    ) = todoDatabase.todoDao()

    @Provides
    @Singleton
    fun provideTodoRepository(
        todoDao: TodoDao
    ): TodoRepository = TodoRepositoryImpl(todoDao)


}