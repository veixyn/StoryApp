package com.dicoding.storyapp.data.di

import android.content.Context
import com.dicoding.storyapp.data.Repository
import com.dicoding.storyapp.data.pref.UserPreference
import com.dicoding.storyapp.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = UserPreference.getInstance(context.dataStore)
//        val user = runBlocking { pref.getSession().first() }
//        val apiService = ApiConfig.getApiService(user.token)
        return Repository.getInstance(pref)
    }
}