package com.dicoding.storyapp.view.maps

import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.storyapp.data.Repository
import com.dicoding.storyapp.data.api.ApiConfig
import com.dicoding.storyapp.data.api.ApiService
import com.dicoding.storyapp.data.api.ListStoryItem
import com.dicoding.storyapp.data.api.RegisterResponse
import com.dicoding.storyapp.data.api.StoryResponse
import com.dicoding.storyapp.data.api.StoryUploadResponse
import com.dicoding.storyapp.data.pref.UserModel
import com.dicoding.storyapp.view.ViewModelFactory
import com.dicoding.storyapp.view.main.MainViewModel
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class MapsViewModel(private val repository: Repository) : ViewModel() {
    private val _story = MutableLiveData<List<ListStoryItem>>()
    val story: LiveData<List<ListStoryItem>> = _story

    suspend fun getStoryWithLocation(token: String) {
        try {
            val apiService = ApiConfig.getApiService(token).getStoriesWithLocation()
            _story.value = apiService.listStory
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, StoryUploadResponse::class.java)
        }
    }

    companion object {
        const val TAG = "MapsActivity"
    }
}