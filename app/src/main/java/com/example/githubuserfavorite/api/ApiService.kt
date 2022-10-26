package com.example.githubuserfavorite.api

import com.example.githubuserfavorite.data.model.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("search/users")
    fun getSearchUser(
        @Query("q") query: String,
    ): Call<SearchResponse>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<UserDetail>

    @GET("users/{username}/{typeFollow}")
    fun getFollow(
        @Path("username") username: String,
        @Path("typeFollow") typeFollow: String
    ): Call<ArrayList<FollowItem>>

}