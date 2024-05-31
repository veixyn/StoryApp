package com.dicoding.storyapp.view.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.Repository
import com.dicoding.storyapp.data.api.ApiConfig
import com.dicoding.storyapp.data.api.ListStoryItem
import retrofit2.HttpException

class MapsViewModel(private val repository: Repository) : ViewModel() {
    private val _story = MutableLiveData<List<ListStoryItem>>()
    val story: LiveData<List<ListStoryItem>> = _story

    suspend fun getStoryWithLocation(token: String) {
        try {
            val apiService = ApiConfig.getApiService(token).getStoriesWithLocation()
            _story.value = apiService.listStory
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Log.d(TAG, errorBody.toString())
        }
    }

    companion object {
        const val TAG = "MapsActivity"
    }
}