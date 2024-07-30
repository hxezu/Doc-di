package com.example.doc_di.di

import com.example.doc_di.domain.repository.TaskRepository
import com.example.doc_di.domain.usecases.DeleteTaskUseCase
import com.example.doc_di.domain.usecases.GetTaskListUseCase
import com.example.doc_di.domain.usecases.StoreTaskUseCase
import com.example.doc_di.viewmodel.TaskViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideStoreTaskUseCase(taskRepository: TaskRepository): StoreTaskUseCase {
        return StoreTaskUseCase(taskRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetTaskListUseCase(taskRepository: TaskRepository): GetTaskListUseCase {
        return GetTaskListUseCase(taskRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideDeleteTaskUseCase(taskRepository: TaskRepository): DeleteTaskUseCase {
        return DeleteTaskUseCase(taskRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideTaskViewModel(
        storeTaskUseCase: StoreTaskUseCase,
        getTaskListUseCase: GetTaskListUseCase,
        deleteTaskUseCase: DeleteTaskUseCase
    ): TaskViewModel {
        return TaskViewModel(storeTaskUseCase, getTaskListUseCase, deleteTaskUseCase)
    }
}