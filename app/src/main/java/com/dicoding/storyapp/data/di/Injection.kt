package com.dicoding.storyapp.data.di

import android.content.Context
import com.dicoding.storyapp.data.Repository
import com.dicoding.storyapp.data.api.ApiConfig
import com.dicoding.storyapp.data.pref.UserPreference
import com.dicoding.storyapp.data.pref.dataStore
import com.dicoding.storyapp.database.StoryDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        val storyDatabase = StoryDatabase.getDatabase(context)
        return Repository.getInstance(storyDatabase, apiService, pref)
    }
}