package com.dicoding.storyapp.view.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.Repository
import com.dicoding.storyapp.data.api.ApiConfig
import com.dicoding.storyapp.data.api.RegisterResponse
import com.google.gson.Gson
import retrofit2.HttpException

class RegisterViewModel(private val repository: Repository) : ViewModel() {
    private var _isLoading = MutableLiveData<Boolean>()
    var isLoading: LiveData<Boolean> = _isLoading

    private var _message = MutableLiveData<String>()
    var message: LiveData<String> = _message

    private var _success = MutableLiveData<Boolean>()
    var success: LiveData<Boolean> = _success

    suspend fun userRegister(name: String, email: String, password: String) {
        _isLoading.value = true
        try {
            val apiService = ApiConfig.getApiService("")
            val successResponse = apiService.register(name, email, password)
            _isLoading.value = false
            _message.value = successResponse.message.toString()
            _success.value = true
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            _isLoading.value = false
            _message.value = errorResponse.message.toString()
            _success.value = false
        }
    }
}