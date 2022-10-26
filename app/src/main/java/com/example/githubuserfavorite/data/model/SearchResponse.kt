package com.example.githubuserfavorite.data.model

import com.google.gson.annotations.SerializedName

data class SearchResponse(

    @field:SerializedName("total_count")
    val totalCount: Int,

    @field:SerializedName("items")
    val items: List<SearchItem>
)

data class SearchItem(

    @field:SerializedName("login")
    val username: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("avatar_url")
    val avatarUrl: String
)