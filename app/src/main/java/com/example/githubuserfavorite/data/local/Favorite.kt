package com.example.githubuserfavorite.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_user")

data class Favorite(
    @PrimaryKey
    @ColumnInfo(name = "login")
    var username: String = "",

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "avatar_url")
    var avatarUrl: String? = null
)