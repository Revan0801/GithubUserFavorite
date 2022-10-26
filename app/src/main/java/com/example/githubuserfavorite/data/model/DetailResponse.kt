package com.example.githubuserfavorite.data.model

import com.google.gson.annotations.SerializedName

data class UserDetail(

    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("login")
    val username: String,

    @field:SerializedName("followers")
    val followers: Int,

    @field:SerializedName("following")
    val following: Int,

    @field:SerializedName("company")
    val company: String,

    @field:SerializedName("location")
    val location: String,

    @field:SerializedName("public_repos")
    val publicRepos: Int
)