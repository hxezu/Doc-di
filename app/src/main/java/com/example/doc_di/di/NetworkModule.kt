package com.example.doc_di.di

import com.example.doc_di.domain.RetrofitInstance
import com.example.doc_di.domain.reminder.ReminderApi
import dagger.hilt.InstallIn
import dagger.Provides
import dagger.Module
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideReminderApi(): ReminderApi {
        return RetrofitInstance.reminderApi
    }
}