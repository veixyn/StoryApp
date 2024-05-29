package com.dicoding.storyapp.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.storyapp.data.Repository
import com.dicoding.storyapp.data.api.ApiConfig
import com.dicoding.storyapp.data.api.ListStoryItem
import com.dicoding.storyapp.data.api.StoryResponse
import com.dicoding.storyapp.data.pref.UserModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val repository: Repository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    val story: LiveData<PagingData<ListStoryItem>> =
        repository.getStory().cachedIn(viewModelScope)

//    fun getStory(token: String) {
//        val client = ApiConfig.getApiService(token).getStories()
//        client.enqueue(object : Callback<StoryResponse> {
//            override fun onResponse(
//                call: Call<StoryResponse>,
//                response: Response<StoryResponse>,
//            ) {
//                if (response.isSuccessful) {
//                    val responseBody = response.body()
//                    if (responseBody != null) {
//                        _story.value = response.body()?.listStory
//                        println("ini MainViewModel")
//                    }
//                } else {
//                    Log.e(TAG, "onFailure: ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
//                Log.e(TAG, "onFailure: ${t.message}")
//            }
//        })
//    }

    companion object {
        private const val TAG = "MainViewModel"
    }

}