package com.example.githubuserfavorite.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserfavorite.api.ApiConfig
import com.example.githubuserfavorite.data.model.FollowItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel: ViewModel() {
    private val _userFollowers = MutableLiveData<ArrayList<FollowItem>>()
    val userFollowers: LiveData<ArrayList<FollowItem>> = _userFollowers

    private val _userFollowing = MutableLiveData<ArrayList<FollowItem>>()
    val userFollowing: LiveData<ArrayList<FollowItem>> = _userFollowing

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getFollowers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollow(username, typeFollow = "followers")
        client.enqueue(object : Callback<ArrayList<FollowItem>> {
            override fun onResponse(call: Call<ArrayList<FollowItem>>, response: Response<ArrayList<FollowItem>>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userFollowers.value = response.body()

                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<FollowItem>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getFollowing(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollow(username, typeFollow = "following")
        client.enqueue(object : Callback<ArrayList<FollowItem>> {
            override fun onResponse(
                call: Call<ArrayList<FollowItem>>,
                response: Response<ArrayList<FollowItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userFollowing.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<FollowItem>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "FollowersViewModel"
    }
}