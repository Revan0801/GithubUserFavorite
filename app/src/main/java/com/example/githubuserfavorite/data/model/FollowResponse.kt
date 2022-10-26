package com.example.githubuserfavorite.data.model

import com.google.gson.annotations.SerializedName

data class FollowItem(

    @field:SerializedName("avatar_url")
    val avatarUrl: String? = null,

    @field:SerializedName("login")
    val username: String? = null,

    @field:SerializedName("id")
    val id: Int? = null
)