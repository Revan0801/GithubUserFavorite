package com.example.githubuserfavorite.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserfavorite.api.ApiConfig
import com.example.githubuserfavorite.data.model.SearchItem
import com.example.githubuserfavorite.data.model.SearchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _userGitSearch = MutableLiveData<List<SearchItem>>()
    val userGitSearch: LiveData<List<SearchItem>> = _userGitSearch

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isDataFound = MutableLiveData<Boolean>()
    val isDataFound: LiveData<Boolean> = _isDataFound

    fun findUser(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSearchUser(query)
        client.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()?.items
                    if (responseBody.isNullOrEmpty()) {
                        _isDataFound.value = false
                    } else {
                        _userGitSearch.value = response.body()?.items
                        _isDataFound.value = true
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}