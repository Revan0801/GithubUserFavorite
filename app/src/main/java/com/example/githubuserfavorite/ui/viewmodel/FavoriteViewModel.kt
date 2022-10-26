package com.example.githubuserfavorite.ui.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.githubuserfavorite.data.local.Favorite
import com.example.githubuserfavorite.repository.FavoriteRepository

class FavoriteViewModel(application: Application): ViewModel(){

    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun getAllFavoriteUser(): LiveData<List<Favorite>> = mFavoriteRepository.getAllFavoriteUser()

    fun getAllLoginUser(): LiveData<List<String>> = mFavoriteRepository.getAllLoginUser()

    fun addFavorite(favorite: Favorite) = mFavoriteRepository.addFavorite(favorite)

    fun removeFavorite(loginUser: String) = mFavoriteRepository.removeFavorite(loginUser)

}