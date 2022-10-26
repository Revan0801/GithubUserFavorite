package com.example.githubuserfavorite.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addFavorite(favorite: Favorite)

    @Query("SELECT login FROM favorite_user")
    fun getAllLoginUser(): LiveData<List<String>>

    @Query("SELECT * FROM favorite_user")
    fun getAllFavoriteUser(): LiveData<List<Favorite>>

    @Query("DELETE FROM favorite_user WHERE login = :login")
    fun removeFavorite(login: String)
}