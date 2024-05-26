package com.dicoding.storyapp.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.UserRepository
import com.dicoding.storyapp.data.api.ApiConfig
import com.dicoding.storyapp.data.api.LoginResponse
import com.dicoding.storyapp.data.pref.UserModel
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    private var _success = MutableLiveData<Boolean>()
    var success: LiveData<Boolean> = _success

    private var _isLoading = MutableLiveData<Boolean>()
    var isLoading: LiveData<Boolean> = _isLoading

    private var _userModel = MutableLiveData<UserModel>()
    var userModel: LiveData<UserModel> = _userModel

    private var _message = MutableLiveData<String>()
    var message: LiveData<String> = _message

    suspend fun userLogin(email: String, password: String) {
        _isLoading.value = true
        try {
            val apiService = ApiConfig.getApiService("")
            val successResponse = apiService.login(email, password)
            _isLoading.value = false
            _userModel.value =
                (UserModel(email, successResponse.loginResult?.token.toString(), true))
            _success.value = true
            _message.value = ""
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            _isLoading.value = false
            _message.value = errorResponse.message.toString()
            _success.value = false
        }
    }
}