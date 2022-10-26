package com.example.githubuserfavorite.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.githubuserfavorite.data.local.Favorite
import com.example.githubuserfavorite.data.local.FavoriteDao
import com.example.githubuserfavorite.data.local.FavoriteDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {

    private var mFavoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteDatabase.getDatabase(application)
        mFavoriteDao = db.favoriteDao()
    }

    fun getAllFavoriteUser(): LiveData<List<Favorite>> = mFavoriteDao.getAllFavoriteUser()

    fun getAllLoginUser(): LiveData<List<String>> = mFavoriteDao.getAllLoginUser()

    fun addFavorite(favorite: Favorite) {
        executorService.execute { mFavoriteDao.addFavorite(favorite) }
    }

    fun removeFavorite(loginUser: String) {
        executorService.execute { mFavoriteDao.removeFavorite(loginUser) }
    }
}